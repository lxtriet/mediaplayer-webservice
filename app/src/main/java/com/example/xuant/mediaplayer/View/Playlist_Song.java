package com.example.xuant.mediaplayer.View;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xuant.mediaplayer.Adapter.Playlist_Adapter;
import com.example.xuant.mediaplayer.Adapter.Song_Online_Adapter;
import com.example.xuant.mediaplayer.Database.DBPlaylist;
import com.example.xuant.mediaplayer.Database.DBSongOnline;
import com.example.xuant.mediaplayer.Database.GetSongListener;
import com.example.xuant.mediaplayer.Model.Server.Playlist;
import com.example.xuant.mediaplayer.Model.Server.SongOnline;
import com.example.xuant.mediaplayer.R;
import com.example.xuant.mediaplayer.Session.SessionManager;

import java.util.ArrayList;

/**
 * Created by xuant on 30/05/2017.
 */

public class Playlist_Song extends AppCompatActivity implements AdapterView.OnItemClickListener,GetSongListener{

    Toolbar mToolbar;
    ListView lv_Playlist;
    LinearLayout add_music;
    ImageView nothingPlaylist;
    TextView name_playlist;

    DBSongOnline dbSongOnline;
    SessionManager sessionManager;
    public static ArrayList<SongOnline> songOnlineArrayList;
    Song_Online_Adapter song_online_adapter;
    DBPlaylist dbPlaylist;
    public static ArrayList<Playlist> playlistArrayList;
    Playlist_Adapter playlist_adapter;
    public static int positionplaylist=0;

    AlertDialog.Builder dialog;
    AlertDialog alertDialog;
    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);// Tạo nút trở về activity trước
        }

        lv_Playlist = (ListView) findViewById(R.id.recycler_playlist_songs);
        add_music = (LinearLayout) findViewById(R.id.add_music);
        nothingPlaylist = (ImageView) findViewById(R.id.nothingPlaylist);
        name_playlist = (TextView) findViewById(R.id.name_playlist);

        lv_Playlist.setDivider(null);
        lv_Playlist.setDividerHeight(0);
        lv_Playlist.setOnItemClickListener(this);
        dbSongOnline = new DBSongOnline(this);
        // Lấy bài hát theo playlist hiện tại
        try{
            dbSongOnline.getSongPlaylist(Music_Playlist.playlistArrayList.get(Music_Playlist.positionplaylist).getId());
            name_playlist.setText(Music_Playlist.playlistArrayList.get(Music_Playlist.positionplaylist).getName());
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        // Thêm bài hát vào danh sách playlist
        add_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Addmusic_toPlaylist.class);
                startActivity(intent);
            }
        });
    }
    // Sự kiện khi click vào danh sách bài trong playlist
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getAdapter() == song_online_adapter){
            onItemSongClick(position);
        }
    }
    // Hàm xử lý khi click vào danh sách bài hát trong playlist
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
        song_online_adapter.setPosition(pos);
        Intent intent = new Intent(Playlist_Song.this,Screen_PlayMusic.class).putExtra("position",pos)
                .putExtra("time",0).putExtra("play","play").putExtra("type","playlist");
        startActivity(intent);
    }
    // Hàm lấy danh sách bài hát trog playlist
    @Override
    public void getListSong(ArrayList<SongOnline> songOnlines) {
        if(!songOnlines.isEmpty() || songOnlines!=null){
            nothingPlaylist.setVisibility(View.GONE);
            songOnlineArrayList = songOnlines;
            song_online_adapter = new Song_Online_Adapter(getApplicationContext(),songOnlineArrayList);
            lv_Playlist.setAdapter(song_online_adapter);
        }

    }
}
