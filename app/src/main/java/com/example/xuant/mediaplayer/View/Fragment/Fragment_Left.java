package com.example.xuant.mediaplayer.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.xuant.mediaplayer.Adapter.Song_OfflineScreen_Adapter;
import com.example.xuant.mediaplayer.Adapter.Song_OfflineScreen_Onl_Adapter;
import com.example.xuant.mediaplayer.Model.Server.SongOnline;
import com.example.xuant.mediaplayer.Model.SongOffline;
import com.example.xuant.mediaplayer.R;
import com.example.xuant.mediaplayer.View.Music_Favourite;
import com.example.xuant.mediaplayer.View.Music_Offline;
import com.example.xuant.mediaplayer.View.Music_Online;
import com.example.xuant.mediaplayer.View.Playlist_Song;
import com.example.xuant.mediaplayer.View.Screen_PlayMusic;

import java.util.ArrayList;

/**
 * Created by xuant on 21/05/2017.
 */

public class Fragment_Left  extends Fragment implements AdapterView.OnItemClickListener{

    ArrayList<SongOffline> songOfflineArrayList;
    ArrayList<SongOnline> songOnlineArrayList;
    Song_OfflineScreen_Adapter song_offlineScreen_adapter;
    Song_OfflineScreen_Onl_Adapter song_offlineScreen_onl_adapter;
    ListView fragment_left_lv;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left, container, false);
        fragment_left_lv = (ListView)view.findViewById(R.id.fragment_left_lv);

        // ẩn underline giữa các item trong list view
        fragment_left_lv.setDivider(null);
        fragment_left_lv.setDividerHeight(0);

        // nếu là từ music offline thì set list theo offline
        if(Screen_PlayMusic.type.equals("offline")){
            songOfflineArrayList = Music_Offline.songOfflineArrayList;
            song_offlineScreen_adapter = new Song_OfflineScreen_Adapter(getContext(),songOfflineArrayList);
            fragment_left_lv.setAdapter(song_offlineScreen_adapter);
            song_offlineScreen_adapter.setPosition(Screen_PlayMusic.position);
        }else if(Screen_PlayMusic.type.equals("online")){   // nếu là từ music online thì set list theo online
            songOnlineArrayList = Music_Online.songOnlineArrayList;
            song_offlineScreen_onl_adapter = new Song_OfflineScreen_Onl_Adapter(getContext(),songOnlineArrayList);
            fragment_left_lv.setAdapter(song_offlineScreen_onl_adapter);
            song_offlineScreen_onl_adapter.setPosition(Screen_PlayMusic.position);
        }else if(Screen_PlayMusic.type.equals("favourite")){   // nếu là từ music favourite thì set list theo favourite
            songOnlineArrayList = Music_Favourite.songOnlineArrayList;
            song_offlineScreen_onl_adapter = new Song_OfflineScreen_Onl_Adapter(getContext(),songOnlineArrayList);
            fragment_left_lv.setAdapter(song_offlineScreen_onl_adapter);
            song_offlineScreen_onl_adapter.setPosition(Screen_PlayMusic.position);
        }else if(Screen_PlayMusic.type.equals("playlist")){  // nếu là từ music playlist thì set list theo playlist
            songOnlineArrayList = Playlist_Song.songOnlineArrayList;
            song_offlineScreen_onl_adapter = new Song_OfflineScreen_Onl_Adapter(getContext(),songOnlineArrayList);
            fragment_left_lv.setAdapter(song_offlineScreen_onl_adapter);
            song_offlineScreen_onl_adapter.setPosition(Screen_PlayMusic.position);
        }



        fragment_left_lv.setOnItemClickListener(this);

        return view;
    }

    // Sự kiện khi click vào item các bài hát
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getAdapter() == song_offlineScreen_adapter){
            onItemSongClick(position);
        }else if(parent.getAdapter() == song_offlineScreen_onl_adapter){
            onItemSongOnlClick(position);
        }
    }


    // Khi click vào các item các bài hát offline thì chuyển qua form phát nhạc
    public void onItemSongClick(int pos){
        try {
            if(Screen_PlayMusic.mediaPlayer!=null && Screen_PlayMusic.mediaPlayer.isPlaying())
            {
                Screen_PlayMusic.mediaPlayer.stop();
                Screen_PlayMusic.mediaPlayer.release();
                Screen_PlayMusic.mediaPlayer = null;
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
        song_offlineScreen_adapter.setPosition(pos);
        Intent intent = new Intent(getActivity(),Screen_PlayMusic.class).putExtra("position",pos)
                .putExtra("time",0).putExtra("play","play").putExtra("type","offline");
        startActivity(intent);
    }

    // Khi click vào các bài hát online thì chuyển qua trình phát nhạc các bài hát online
    public void onItemSongOnlClick(int pos){
        try {
            if(Screen_PlayMusic.mediaPlayer!=null && Screen_PlayMusic.mediaPlayer.isPlaying())
            {
                Screen_PlayMusic.mediaPlayer.stop();
                Screen_PlayMusic.mediaPlayer.release();
                Screen_PlayMusic.mediaPlayer = null;
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

        // Chuyển qua trình phát nhạc dành cho danh sách các bài hát favourite
        Intent intent = new Intent();
        if(Screen_PlayMusic.type.equals("favourite")){
            intent = new Intent(getActivity(),Screen_PlayMusic.class).putExtra("position",pos)
                    .putExtra("time",0).putExtra("play","play").putExtra("type","favourite");
        }else if(Screen_PlayMusic.type.equals("online")){    // Chuyển qua trình phát nhạc dành cho danh sách các bài hát online
            intent = new Intent(getActivity(),Screen_PlayMusic.class).putExtra("position",pos)
                    .putExtra("time",0).putExtra("play","play").putExtra("type","online");
        }else if(Screen_PlayMusic.type.equals("playlist")){    // Chuyển qua trình phát nhạc dành cho danh sách các bài hát playlist
            intent = new Intent(getActivity(),Screen_PlayMusic.class).putExtra("position",pos)
                    .putExtra("time",0).putExtra("play","play").putExtra("type","playlist");
        }
        song_offlineScreen_onl_adapter.setPosition(pos);

        startActivity(intent);
    }
}
