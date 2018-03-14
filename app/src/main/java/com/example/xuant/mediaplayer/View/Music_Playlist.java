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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xuant.mediaplayer.Adapter.Playlist_Adapter;
import com.example.xuant.mediaplayer.Adapter.Song_Online_Adapter;
import com.example.xuant.mediaplayer.Database.DBPlaylist;
import com.example.xuant.mediaplayer.Database.DBSongOnline;
import com.example.xuant.mediaplayer.Database.GetPlaylistListener;
import com.example.xuant.mediaplayer.Model.Server.Playlist;
import com.example.xuant.mediaplayer.Model.Server.SongOnline;
import com.example.xuant.mediaplayer.R;
import com.example.xuant.mediaplayer.Session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xuant on 30/05/2017.
 */

public class Music_Playlist extends AppCompatActivity implements GetPlaylistListener,AdapterView.OnItemClickListener,View.OnClickListener{
    Toolbar mToolbar;
    ListView lv_Playlist;
    LinearLayout add_playlist;

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
        setContentView(R.layout.activity_music_playlist);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);// Tạo nút trở về activity trước
        }

        lv_Playlist = (ListView) findViewById(R.id.recycler_playlist);
        add_playlist = (LinearLayout) findViewById(R.id.add_playlist);

        lv_Playlist.setDivider(null);
        lv_Playlist.setDividerHeight(0);
        lv_Playlist.setOnItemClickListener(this);

        dbPlaylist = new DBPlaylist(this);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        dbPlaylist.getPlaylist(Integer.parseInt(user.get(SessionManager.KEY_ID)));

        add_playlist.setOnClickListener(this);

    }
    // Hàm lấy danh sách playlist theo user
    @Override
    public void getPlaylist(ArrayList<Playlist> playlistArrayList) {
        this.playlistArrayList = playlistArrayList;
        playlist_adapter = new Playlist_Adapter(getApplicationContext(),playlistArrayList);
        lv_Playlist.setAdapter(playlist_adapter);
    }
    // Hàm xử lý khi click vào danh sách playlist
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getAdapter() == playlist_adapter){
            onItemSongClick(position);
        }
    }
    // Hàm xử lý khi click vào danh sách playlist
    public void onItemSongClick(int pos){
        positionplaylist = pos;
        Intent intent = new Intent(getApplication(), Playlist_Song.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.add_playlist:  // Sự kiện theo playlist

                dialog = new AlertDialog.Builder(Music_Playlist.this);
                dialog.setCancelable(true);
                layoutInflater = LayoutInflater.from((getBaseContext()));
                v = layoutInflater.inflate(R.layout.dialog_addplaylist, null);

                dialog.setView(v);

                final EditText playlist_name;
                TextView btn_dismiss,create_playlist;
                alertDialog = dialog.create();
                // Đóng dialog
                btn_dismiss = (TextView) v.findViewById(R.id.btn_dismiss);
                playlist_name = (EditText) v.findViewById(R.id.playlist_name);
                create_playlist = (TextView) v.findViewById(R.id.create_playlist);
                btn_dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
                create_playlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nameplaylist = playlist_name.getText().toString();
                        HashMap<String, String> user = sessionManager.getUserDetails();
                        dbPlaylist.insertPlaylist(nameplaylist,Integer.parseInt(user.get(SessionManager.KEY_ID)));
                        dbPlaylist.getPlaylist(Integer.parseInt(user.get(SessionManager.KEY_ID)));
                        alertDialog.dismiss();

                    }
                });
                alertDialog.show();
            break;
        }
    }
}
