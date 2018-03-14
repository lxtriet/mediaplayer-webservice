package com.example.xuant.mediaplayer.View;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xuant.mediaplayer.Adapter.Song_Online_Adapter;
import com.example.xuant.mediaplayer.Database.DBSongOnline;
import com.example.xuant.mediaplayer.Database.GetSongListener;
import com.example.xuant.mediaplayer.Model.Server.SongOnline;
import com.example.xuant.mediaplayer.R;
import com.example.xuant.mediaplayer.Session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xuant on 30/05/2017.
 */

public class Music_Favourite  extends AppCompatActivity implements AdapterView.OnItemClickListener,GetSongListener{
    Toolbar mToolbar;
    ListView lv_favourite;
    ImageView nothingFavourite;

    DBSongOnline dbSongOnline;
    SessionManager sessionManager;
    public static ArrayList<SongOnline> songOnlineArrayList;
    Song_Online_Adapter song_online_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_favourite);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);// Tạo nút trở về activity trước
        }

        lv_favourite = (ListView) findViewById(R.id.recycler_favourite);
        nothingFavourite = (ImageView) findViewById(R.id.nothingFavourite);
        nothingFavourite.setVisibility(View.GONE);

        lv_favourite.setDivider(null);
        lv_favourite.setDividerHeight(0);

        lv_favourite.setOnItemClickListener(this);
        sessionManager = new SessionManager(getApplicationContext());

        dbSongOnline = new DBSongOnline(this);

        // Lấy đanh sách bài hát yêu thích dựa vào id user
        try{
            HashMap<String, String> user = sessionManager.getUserDetails();
            Toast.makeText(Music_Favourite.this,"Lay nhac yeu thich",Toast.LENGTH_LONG).show();
            dbSongOnline.getSongFavourite(Integer.parseInt(user.get(SessionManager.KEY_ID)));
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    // Sự kiện khi click vào item bài hát trong danh sách bài hát favourite
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getAdapter() == song_online_adapter){
            onItemSongClick(position);
        }
    }
    // Hàm xử lý khi click vào danh sách bài hát favourite
    public void onItemSongClick(int pos){

        try {
            if(Screen_PlayMusic.mediaPlayer!=null && Screen_PlayMusic.mediaPlayer.isPlaying())
            {
                Screen_PlayMusic.mediaPlayer.stop();  // dừng trình chơi nhạc hiện tại
                Screen_PlayMusic.mediaPlayer.release();
                Screen_PlayMusic.mediaPlayer = null;
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
        song_online_adapter.setPosition(pos);       // Mở activity nhạc mới với bài hát được chọn
        Intent intent = new Intent(Music_Favourite.this,Screen_PlayMusic.class).putExtra("position",pos)
                .putExtra("time",0).putExtra("play","play").putExtra("type","favourite");
        startActivity(intent);
    }
    // Hàm lấy danh sách bài hát từ hàm get danh sách bài hát trước
    @Override
    public void getListSong(ArrayList<SongOnline> songOnlines) {
        if(songOnlines.isEmpty() || songOnlines==null){
            nothingFavourite.setVisibility(View.VISIBLE);
        }else{
            nothingFavourite.setVisibility(View.GONE);
            songOnlineArrayList = songOnlines;
            song_online_adapter = new Song_Online_Adapter(getApplicationContext(),songOnlineArrayList);
            lv_favourite.setAdapter(song_online_adapter);
        }
    }

}
