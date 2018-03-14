package com.example.xuant.mediaplayer.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xuant.mediaplayer.Database.DBUser;
import com.example.xuant.mediaplayer.Database.GetUserListener;
import com.example.xuant.mediaplayer.Model.Server.User;
import com.example.xuant.mediaplayer.R;
import com.example.xuant.mediaplayer.Service.Utils;
import com.example.xuant.mediaplayer.Session.SessionManager;
import com.example.xuant.mediaplayer.View.Login;
import com.example.xuant.mediaplayer.View.Music_Favourite;
import com.example.xuant.mediaplayer.View.Music_Offline;
import com.example.xuant.mediaplayer.View.Music_Online;
import com.example.xuant.mediaplayer.View.Music_Playlist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by xuant on 22/05/2017.
 */

public class Fragment_Personal extends Fragment implements GetUserListener{

    LinearLayout personal_library_offline, personal_library_online,personal_login,personal_logout,personal_library_favourite, personal_library_playlist;
    CircleImageView personal_image_user;
    TextView personal_name_user;
    private int LOGIN_REQ = 1;
    SessionManager session;
    DBUser dbUser;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        dbUser = new DBUser(this);
        session = new SessionManager(getContext());

        personal_library_offline = (LinearLayout)view.findViewById(R.id.personal_library_offline);
        personal_library_online = (LinearLayout)view.findViewById(R.id.personal_library_online);
        personal_login = (LinearLayout)view.findViewById(R.id.personal_login);
        personal_logout = (LinearLayout)view.findViewById(R.id.personal_logout);
        personal_library_favourite = (LinearLayout)view.findViewById(R.id.personal_library_favourite);
        personal_library_playlist = (LinearLayout)view.findViewById(R.id.personal_library_playlist);

        personal_image_user = (CircleImageView)view.findViewById(R.id.personal_image_user);
        personal_name_user = (TextView)view.findViewById(R.id.personal_name_user);
        // Mở activity danh sách các bài hát offline
        personal_library_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Music_Offline.class);
                startActivity(intent);
            }
        });
        //Mở danh sách các bài hát online
        personal_library_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Music_Online.class);
                startActivity(intent);
            }
        });

        // Mở activity login
        personal_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(session.isLoggedIn())
                    Toast.makeText(getActivity(),"Chức năng bất ổn !", Toast.LENGTH_LONG).show();
                else
                {
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivityForResult(intent, LOGIN_REQ);
                }

            }
        });
        // mở danh sách các bài hát yêu thích
        personal_library_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(session.isLoggedIn())
                {
                    Intent intent = new Intent(getActivity(), Music_Favourite.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivityForResult(intent, LOGIN_REQ);
                }

            }
        });
        // Mở danh sách các playlist của user
        personal_library_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(session.isLoggedIn())
                {
                    Intent intent = new Intent(getActivity(), Music_Playlist.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivityForResult(intent, LOGIN_REQ);
                }

            }
        });
        // Sự kiện đăng xuất
        personal_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        CheckLogin();
        return view;
    }
    // Sự kiện kiểm tra đăng nhập
    public void CheckLogin()
    {
        if(session.isLoggedIn())
        {  // nếu có đăng nhập hiển thị thanh logout và get lại user theo session email
            personal_logout.setVisibility(View.VISIBLE);
            HashMap<String, String> user = session.getUserDetails();
            dbUser.getUser(user.get(SessionManager.KEY_USERNAME));
        }
        else
        {
            personal_logout.setVisibility(View.GONE);
        }

    }
        // Sự kiện đăng xuất
    public void logOut()
    { // Remove session và ẩn thanh đăng xuất
        session.logoutUser();
        personal_name_user.setText("Đăng nhập");
        personal_image_user.setImageResource(R.drawable.ic_avatar_login);
        personal_logout.setVisibility(View.GONE);
    }
    // Kiểm tra kết quả trả về từ activity đăng nhập
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==LOGIN_REQ){
            if(resultCode==RESULT_OK){
                User user = Login.user;
                personal_name_user.setText(user.getName());
                String url_imgitem= Utils.BASE_URL+"getimg?nameimg=";
                Picasso.with(getContext()).load(url_imgitem+user.getImg()+".png")
                        .placeholder(R.drawable.song_empty)
                        .error(R.drawable.song_empty)
                        .into(personal_image_user);
                session.createLoginSession(user.getUsername(), String.valueOf(user.getId()));  // TRả về thành công thì tạo session
                CheckLogin();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Lấy user theo session đã tạo
    @Override
    public void getUser(ArrayList<User> userArrayList) {
        User user =  userArrayList.get(0);
        personal_name_user.setText(user.getName());
        String url_imgitem="http://192.168.1.2:8000/getimg?nameimg=";
        Picasso.with(getContext()).load(url_imgitem+user.getImg()+".png")
                .placeholder(R.drawable.song_empty)
                .error(R.drawable.song_empty)
                .into(personal_image_user);
    }
}
