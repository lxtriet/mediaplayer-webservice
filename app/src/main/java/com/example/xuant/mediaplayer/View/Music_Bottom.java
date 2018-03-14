package com.example.xuant.mediaplayer.View;

import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xuant.mediaplayer.Model.Server.SongOnline;
import com.example.xuant.mediaplayer.Model.SongOffline;
import com.example.xuant.mediaplayer.R;
import com.example.xuant.mediaplayer.Service.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by xuant on 21/05/2017.
 */

public class Music_Bottom extends Fragment implements View.OnClickListener{
    public static View view;
    ArrayList<SongOffline> songOfflineArrayList;
    ArrayList<SongOnline> songOnlineArrayList;
    LinearLayout music_bottom_main;
    ImageView music_bottom_play, music_bottom_prev,music_bottom_next;
    TextView music_bottom_name, music_bottom_artist;
    String play = "play";
    public static String url_mp3= Utils.BASE_URL+"getmp3?namemp3=",type="";
    int pos = 0, time = 0;
    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.music_bottom, container, false);

        music_bottom_main = (LinearLayout)view.findViewById(R.id.music_bottom_main);
        music_bottom_play = (ImageView)view.findViewById(R.id.music_bottom_play);
        music_bottom_prev = (ImageView)view.findViewById(R.id.music_bottom_prev);
        music_bottom_next = (ImageView)view.findViewById(R.id.music_bottom_next);
        music_bottom_name = (TextView)view.findViewById(R.id.music_bottom_name);
        music_bottom_artist = (TextView)view.findViewById(R.id.music_bottom_artist);
        Log.e("","tui duoc tao");
        music_bottom_main.setOnClickListener(this);
        music_bottom_play.setOnClickListener(this);
        music_bottom_prev.setOnClickListener(this);
        music_bottom_next.setOnClickListener(this);

        // Kiểm tra trình chơi nhạc chính đang play hay pause
        if(Screen_PlayMusic.mediaPlayer.isPlaying())
        {
            play = "play";
            music_bottom_play.setImageResource(R.drawable.ic_playbar_pause);
        }else{
            play = "pause";
            music_bottom_play.setImageResource(R.drawable.ic_playbar_play);
        }

        // Lấy list bài hát dựa theo thể loại nhạc đang chơi
        if(Screen_PlayMusic.type.equals("offline")){
            songOfflineArrayList = Music_Offline.songOfflineArrayList;
        }
        else if(Screen_PlayMusic.type.equals("online")){
            songOnlineArrayList = Music_Online.songOnlineArrayList;
        }else if(Screen_PlayMusic.type.equals("playlist")){
            songOnlineArrayList = Playlist_Song.songOnlineArrayList;
        }
        type = Screen_PlayMusic.type;
        pos = Screen_PlayMusic.position;
        mediaPlayer = Screen_PlayMusic.mediaPlayer;
        time = Screen_PlayMusic.mediaPlayer.getCurrentPosition();
        resizeText();
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.music_bottom_main:  // Sự kiện click để hiển thị trình chơi nhạc

                time = mediaPlayer.getCurrentPosition();
                Screen_PlayMusic.mediaPlayer.stop();
                if(play.equals("pause"))
                    pos = pos-1;
                if(type.equals("offline")){
                    Intent intent = new Intent(getActivity(),Screen_PlayMusic.class).putExtra("position",pos)
                            .putExtra("time",time).putExtra("play",play).putExtra("type","offline");
                    startActivity(intent);
                }

                break;
            case R.id.music_bottom_play:  // Sự kiện click vào play pause trên trình chơi nhạc bottom
                if(Screen_PlayMusic.mediaPlayer.isPlaying())
                {
                    play = "pause";
                    time = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                    music_bottom_play.setImageResource(R.drawable.ic_playbar_play);
                }else{
                    play = "play";
                    music_bottom_play.setImageResource(R.drawable.ic_playbar_pause);
                    mediaPlayer.start();
                }

                break;
            case R.id.music_bottom_prev:   // Sự kiện click vào previous trên trình chơi nhạc bottm
                music_bottom_play.setImageResource(R.drawable.ic_playbar_pause);
                if(Screen_PlayMusic.isShuffle)
                {
                    Random rand = new Random();
                    if(type.equals("offline")){
                        pos = rand.nextInt(songOfflineArrayList.size()-1) + 0;
                    }
                    else if(type.equals("online")){
                        pos = rand.nextInt(songOnlineArrayList.size()-1) + 0;
                    }
                }
                else{
                    if(type.equals("offline")){
                        pos = (pos-1<0)? songOfflineArrayList.size()-1:pos-1;
                    }
                    else if(type.equals("online")){
                        pos = (pos-1<0)? songOnlineArrayList.size()-1:pos-1;
                    }
                }
                try {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    setData();
                    resizeText();
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.music_bottom_next:  // Sự kiện khi click vào next trên trình chơi nhạc bottom
                music_bottom_play.setImageResource(R.drawable.ic_playbar_pause);
                if(Screen_PlayMusic.isShuffle)
                {
                    Random rand = new Random();
                    if(type.equals("offline")){
                        pos = rand.nextInt(songOfflineArrayList.size()-1) + 0;
                    }
                    else if(type.equals("online")){
                        pos = rand.nextInt(songOnlineArrayList.size()-1) + 0;
                    }
                }
                else{
                    if(type.equals("offline")){
                        pos = (pos+1)%songOfflineArrayList.size();
                    }
                    else if(type.equals("online")){
                        pos = (pos+1)%songOnlineArrayList.size();
                    }
                }

                try {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    setData();
                    resizeText();
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    // Sự kiện set dữ liệu cho mediaplayer
    public void setData() throws IOException {
        if(type.equals("offline")){
            mediaPlayer.setDataSource(songOfflineArrayList.get(pos).getDATA());
        }
        else if(type.equals("online")){
            mediaPlayer.setDataSource(url_mp3+songOnlineArrayList.get(pos).getDATA()+".mp3");
        }
    }
    // Hàm chuyển độ dài của tên bài hát với artist cho phù hợp
    public void resizeText(){
        String name="",artist="";
        if(type.equals("online")){
            name = songOnlineArrayList.get(pos).getTITLE();
            artist = songOnlineArrayList.get(pos).getARTIST();
        }else if(type.equals("offline")){
            name = songOfflineArrayList.get(pos).getTITLE();
            artist = songOfflineArrayList.get(pos).getARTIST();
        }
        if(name.length()>20)
            name =name.substring(0,20)+"...";
        if(artist.length()>20)
            artist =artist.substring(0,20)+"...";
        music_bottom_name.setText(name);
        music_bottom_artist.setText(artist);
    }

}
