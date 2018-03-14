package com.example.xuant.mediaplayer.View.Fragment;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xuant.mediaplayer.Database.DBFavourite;
import com.example.xuant.mediaplayer.Database.DBSongOnline;
import com.example.xuant.mediaplayer.Database.FavouriteListener;
import com.example.xuant.mediaplayer.Database.GetSongListener;
import com.example.xuant.mediaplayer.Model.Server.SongOnline;
import com.example.xuant.mediaplayer.R;
import com.example.xuant.mediaplayer.Service.Utils;
import com.example.xuant.mediaplayer.Session.SessionManager;
import com.example.xuant.mediaplayer.View.Music_Favourite;
import com.example.xuant.mediaplayer.View.Music_Online;
import com.example.xuant.mediaplayer.View.Playlist_Song;
import com.example.xuant.mediaplayer.View.Screen_PlayMusic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import eu.gsottbauer.equalizerview.EqualizerView;

//import eu.gsottbauer.equalizerview.EqualizerView;

/**
 * Created by xuant on 21/05/2017.
 */

public class Fragment_Mid extends Fragment implements View.OnClickListener,GetSongListener,FavouriteListener{

    public static CircleImageView fragment_mid_imagedisk;
    public static RotateAnimation rotateAnimation;
    public static EqualizerView fragment_mid_equalizer_view;
    SessionManager sessionManager;
    DBSongOnline dbSongOnline;
    DBFavourite dbFavourite;
    ImageView add_favourite,download;
    boolean isfav = false;

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mid, container, false);
        fragment_mid_imagedisk = (CircleImageView)view.findViewById(R.id.fragment_mid_imagedisk);
        fragment_mid_equalizer_view = (EqualizerView) view.findViewById(R.id.fragment_mid_equalizer_view);
        add_favourite = (ImageView) view.findViewById(R.id.add_favourite);
        download = (ImageView) view.findViewById(R.id.download);


        sessionManager = new SessionManager(getContext());   // lấy user đang đăng nhập hiện tại
        dbSongOnline = new DBSongOnline(this);
        dbFavourite = new DBFavourite(this);

        // Lấy id của user đang đăng nhập hiện tại
        try{
            HashMap<String, String> user = sessionManager.getUserDetails();
            dbSongOnline.getSongFavourite(Integer.parseInt(user.get(SessionManager.KEY_ID)));
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        // Đổi ảnh của disk dựa theo loại hình đang phát nhạc hiện tại
        String url_imgitem=Utils.BASE_URL+"getimg?nameimg=";
        if(Screen_PlayMusic.type.equals("online")){   // phát nhạc online
            Picasso.with(getContext()).load(url_imgitem+ Music_Online.songOnlineArrayList
                    .get(Screen_PlayMusic.position).getIMG()+".jpg")
                    .placeholder(R.drawable.song_empty)
                    .error(R.drawable.song_empty)
                    .into(fragment_mid_imagedisk);
        }else if(Screen_PlayMusic.type.equals("favourite")){  // phát nhạc favourite
            Picasso.with(getContext()).load(url_imgitem+ Music_Favourite.songOnlineArrayList
                    .get(Screen_PlayMusic.position).getIMG()+".jpg")
                    .placeholder(R.drawable.song_empty)
                    .error(R.drawable.song_empty)
                    .into(fragment_mid_imagedisk);
            add_favourite.setImageResource(R.drawable.ic_mm_favorites);
        }else if(Screen_PlayMusic.type.equals("playlist")){   // phát nhạc plalist
            Picasso.with(getContext()).load(url_imgitem+ Playlist_Song.songOnlineArrayList
                    .get(Screen_PlayMusic.position).getIMG()+".jpg")
                    .placeholder(R.drawable.song_empty)
                    .error(R.drawable.song_empty)
                    .into(fragment_mid_imagedisk);
            add_favourite.setImageResource(R.drawable.ic_mm_favorites);
        }

        // Tại chuyển động co image disk
        rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(2500);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        // Bắt đầu animation cho disk image
        fragment_mid_imagedisk.startAnimation(rotateAnimation);
        fragment_mid_equalizer_view.animateBars();
        add_favourite.setOnClickListener(this);
        download.setOnClickListener(this);

        sessionManager = new SessionManager(getContext());

        return view;
    }

    @Override
    public void onClick(View v) {
        int id= v.getId();
        switch (id){
            case R.id.add_favourite:  // Sự kiện thêm vào danh sách favourite khi đăng nhập
                int song_id =0, user_id=0;
                if(Screen_PlayMusic.type.equals("favourite"))
                {
                    song_id =  Music_Favourite.songOnlineArrayList
                            .get(Screen_PlayMusic.position).getId();
                    isfav = true;
                }else if(Screen_PlayMusic.type.equals("online"))
                {
                    song_id =  Music_Online.songOnlineArrayList
                            .get(Screen_PlayMusic.position).getId();
                }else if(Screen_PlayMusic.type.equals("playlist"))
                {
                    song_id =  Playlist_Song.songOnlineArrayList
                            .get(Screen_PlayMusic.position).getId();
                }
                HashMap<String, String> user = sessionManager.getUserDetails();

                user_id = Integer.parseInt(user.get(SessionManager.KEY_ID));
                if(Screen_PlayMusic.type.equals("favourite") || Screen_PlayMusic.type.equals("online") || Screen_PlayMusic.type.equals("playlist")){
                    if(isfav){
                        add_favourite.setImageResource(R.drawable.ic_fav_song);
                        dbFavourite.deleteFavourite(song_id,user_id);
                        Toast.makeText(getContext(),"Remove favourite song",Toast.LENGTH_LONG).show();
                        isfav = false;
                    }else{
                        add_favourite.setImageResource(R.drawable.ic_mm_favorites);
                        dbFavourite.insertFavourite(song_id,user_id);
                        Toast.makeText(getContext(),"Add favourite song",Toast.LENGTH_LONG).show();
                        isfav = true;
                    }
                }
                break;
            case R.id.download:  // Sự kiện download bài hát về sdcard
                if(Screen_PlayMusic.type.equals("favourite") || Screen_PlayMusic.type.equals("online")
                        || Screen_PlayMusic.type.equals("playlist")){
                    SongOnline songOnline = new SongOnline();
                    if(Screen_PlayMusic.type.equals("favourite"))
                    {
                        songOnline =  Music_Favourite.songOnlineArrayList
                                .get(Screen_PlayMusic.position);
                    }else if(Screen_PlayMusic.type.equals("online"))
                    {
                        songOnline =  Music_Online.songOnlineArrayList
                                .get(Screen_PlayMusic.position);
                    }
                    else if(Screen_PlayMusic.type.equals("playlist"))
                    {
                        songOnline =  Playlist_Song.songOnlineArrayList
                                .get(Screen_PlayMusic.position);
                    }
                    String url_mp3= Utils.BASE_URL+"getmp3?namemp3=";
                    DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url_mp3+songOnline.getDATA()+".mp3"));

                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                            .setTitle(songOnline.getTITLE()+".mp3")
                            .setDescription(songOnline.getARTIST())
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, songOnline.getTITLE()+".mp3");

                    long enqueue = downloadManager.enqueue(request);
                    Toast.makeText(getContext(),"Tải xuống "+songOnline.getTITLE()+" thành công",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    // Hàm lấy danh sách bài hát theo điều kiện
    @Override
    public void getListSong(ArrayList<SongOnline> songOnlines) {
        if(Screen_PlayMusic.type.equals("online")){
            for(int i=0;i<songOnlines.size();i++){
                if(Music_Online.songOnlineArrayList
                        .get(Screen_PlayMusic.position).getId() == songOnlines.get(i).getId())
                {
                    add_favourite.setImageResource(R.drawable.ic_mm_favorites);
                    isfav = true; break;
                }
            }
        } else if(Screen_PlayMusic.type.equals("playlist")){
            for(int i=0;i<songOnlines.size();i++){
                if(Playlist_Song.songOnlineArrayList
                        .get(Screen_PlayMusic.position).getId() == songOnlines.get(i).getId())
                {
                    add_favourite.setImageResource(R.drawable.ic_mm_favorites);
                    isfav = true; break;
                }
            }
        }
    }

    @Override
    public void Insert_Delete_Fav(ArrayList<SongOnline> songOnlines) {

    }

}
