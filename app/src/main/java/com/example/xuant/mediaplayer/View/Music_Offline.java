package com.example.xuant.mediaplayer.View;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.xuant.mediaplayer.Adapter.Song_Offline_Adapter;
import com.example.xuant.mediaplayer.Model.SongOffline;
import com.example.xuant.mediaplayer.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by xuant on 21/05/2017.
 */

public class Music_Offline extends AppCompatActivity implements AdapterView.OnItemClickListener,View.OnClickListener{
    Toolbar mToolbar;
    public static TabHost tabHost;
    TabWidget tabWidget;
    ListView lv_Song, lv_Playlist, lv_Album, lv_Singer;
    public static int CurrentTab = 4;
    public static boolean isListOpen = false;

    ImageView nothingPlaylist,nothingPlaylist2,nothingPlaylist3;

    String[] songs;
    ArrayList<File> mySongs;
    public static ArrayList<SongOffline> songOfflineArrayList;

    public static Boolean isPlaying = false;

    Song_Offline_Adapter songOfflineAdapter;
    MediaMetadataRetriever metaRetriver;
    public static FrameLayout rootLayout;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_offline);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);// Tạo nút trở về activity trước
        }
        tabHost = (TabHost) findViewById(R.id.tabhost);

        lv_Song = (ListView) findViewById(R.id.recycler_Song);
        lv_Song.setDivider(null);
        lv_Song.setDividerHeight(0);
        lv_Playlist = (ListView) findViewById(R.id.recycler_Playlist);
        lv_Album = (ListView) findViewById(R.id.recycler_Album);
        lv_Singer = (ListView) findViewById(R.id.recycler_Song);

        nothingPlaylist = (ImageView) findViewById(R.id.nothingPlaylist);
        nothingPlaylist2 = (ImageView) findViewById(R.id.nothingPlaylist2);
        nothingPlaylist3 = (ImageView) findViewById(R.id.nothingPlaylist3);

        nothingPlaylist.setVisibility(View.GONE);
        nothingPlaylist2.setVisibility(View.GONE);
        nothingPlaylist3.setVisibility(View.GONE);

        tabHostSetup();
        // Sư kiện khi chuyển tabwidget
        tabWidget.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTab(0);
            }
        });
        tabWidget.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nothingPlaylist.setVisibility(View.VISIBLE);
                changeTab(1);
            }
        });
        tabWidget.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nothingPlaylist2.setVisibility(View.VISIBLE);
                changeTab(2);
            }
        });
        tabWidget.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nothingPlaylist3.setVisibility(View.VISIBLE);
                changeTab(3);
            }
        });
        // Ẩn music bottom
        rootLayout = (FrameLayout)findViewById(R.id.music_bottom);
        View.inflate(this, R.layout.music_bottom, rootLayout);
        try {
            if(Screen_PlayMusic.mediaPlayer!=null)
            {
                Music_Bottom pb= new Music_Bottom();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.music_bottom, pb);
                transaction.commit();
                //rootLayout.setVisibility(View.VISIBLE);
            }else
                rootLayout.setVisibility(View.GONE);

        }catch (IllegalStateException e){
            e.printStackTrace();
        }

        setItemSongOffline();

        lv_Song.setOnItemClickListener(this);

    }
    // Set data cho danh sách bài hát offline
    public void setItemSong(){
        mySongs = findSongs(Environment.getExternalStorageDirectory());
        songs = new String[ mySongs.size()];
        for(int i=0;i<mySongs.size();i++)
        {
            songs[i] = mySongs.get(i).getName().toString().replace(".mp3","");
        }
//        songOfflineAdapter = new Song_Offline_Adapter(getApplication(),songs);
//        lv_Song.setAdapter(songOfflineAdapter);
    }
    // Lấy danh sách bài hát từ sdcard
    public void setItemSongOffline(){
        songOfflineArrayList = new ArrayList<SongOffline>();
        ContentResolver cr = getApplicationContext().getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cur = cr.query(uri, null, selection, null, sortOrder);
        int count = 0;
        int tempid = 1;
        if(cur != null)
        {
            count = cur.getCount();

            if(count > 0)
            {
                while(cur.moveToNext())
                {
                    SongOffline songOffline = new SongOffline();

                    songOffline.setTITLE(cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                    songOffline.setId(tempid);

                    songOffline.setDATA(cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));

                    songOffline.setDURATION(cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                    Log.e("Size list: ",""+MediaStore.Audio.Media.DATA);
                    songOffline.setARTIST(cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                    songOfflineArrayList.add(songOffline);
                    tempid++;
                }

            }
        }

        Log.e("Size list: ",""+songOfflineArrayList.size());
        songOfflineAdapter = new Song_Offline_Adapter(getApplication(),songOfflineArrayList);
        lv_Song.setAdapter(songOfflineAdapter);
     }

    // Tìm bài hát từ danh sách file trong sdcard
    public ArrayList<File> findSongs(File root){
        ArrayList<File> fileArrayList = new ArrayList<File>();
        File[] files = root.listFiles();
        for(File singFile: files){
            if(singFile.isDirectory() && !singFile.isHidden()){
                fileArrayList.addAll(findSongs(singFile));
            }
            else{
                if(singFile.getName().endsWith(".mp3") || singFile.getName().endsWith(".wav")){
                    fileArrayList.add(singFile);
                }
            }
        }
        return fileArrayList;
    }
    // Hàm ẩn danh sách hiện tại
    public static void hideList(){
        isListOpen = false;
        CurrentTab = 3;
        tabHost.setCurrentTab(CurrentTab);
    }
    // Hàm hiện danh sách đã chọn
    private void showList(int tab){
        isListOpen = true;
        CurrentTab = tab;
        tabHost.setCurrentTab(tab);
    }
    // Hàm thay đổi danh sách
    private void changeTab(int tab) {
        if (CurrentTab == tab) {
            hideList();
        } else {
            showList(tab);
        }
    }
        // Hàm cài đặt tabhost
    public void tabHostSetup() {


        tabHost.setup();
        TabHost.TabSpec tabSong = tabHost.newTabSpec("Song");
        tabSong.setContent(R.id.tab_Song);
        tabSong.setIndicator("BÀI HÁT");
        tabHost.addTab(tabSong);


        TabHost.TabSpec tabPlaylist = tabHost.newTabSpec("Playlist");
        tabPlaylist.setIndicator( "PLAYLIST");
        tabPlaylist.setContent(R.id.tab_Playlist);
        tabHost.addTab(tabPlaylist);


        TabHost.TabSpec tabAlbum = tabHost.newTabSpec("Album");
        tabAlbum.setIndicator("ALBUM");
        tabAlbum.setContent(R.id.tab_Album);
        tabHost.addTab(tabAlbum);

        TabHost.TabSpec tabSinger = tabHost.newTabSpec("Singer");
        tabSinger.setIndicator("CA SĨ");
        tabSinger.setContent(R.id.tab_Singer);
        tabHost.addTab(tabSinger);

        TabHost.TabSpec tabNothing = tabHost.newTabSpec("Nothing");
        tabNothing.setIndicator("Nothing");
        tabNothing.setContent(R.id.tab_Nothing);
        tabHost.addTab(tabNothing);

        TextView x1 = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        x1.setTextSize(13);
        TextView x2 = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        x2.setTextSize(13);
        TextView x3 = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
        x3.setTextSize(13);
        TextView x4 = (TextView) tabHost.getTabWidget().getChildAt(3).findViewById(android.R.id.title);
        x4.setTextSize(13);



        tabWidget = tabHost.getTabWidget();

        tabWidget.getChildAt(0).setBackgroundResource(R.drawable.tab_selector);
        tabWidget.getChildAt(1).setBackgroundResource(R.drawable.tab_selector);
        tabWidget.getChildAt(2).setBackgroundResource(R.drawable.tab_selector);
        tabWidget.getChildAt(3).setBackgroundResource(R.drawable.tab_selector);

        tabWidget.getChildAt(4).setVisibility(View.GONE);

        tabHost.setCurrentTab(0);
    }
    // Sự kiện click vào item bài hát
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getAdapter() == songOfflineAdapter){
            onItemSongClick(position);
        }
    }
    // Hàm xử lý khi click vào danh sách bài hát
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
        songOfflineAdapter.setPosition(pos);
        Intent intent = new Intent(Music_Offline.this,Screen_PlayMusic.class).putExtra("position",pos)
                .putExtra("time",0).putExtra("play","play").putExtra("type","offline");
        startActivity(intent);
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.music_bottom_main:
//                int pos = 0, time = 0;
//                pos = Screen_PlayMusic.position;
//                time = Screen_PlayMusic.mediaPlayer.getCurrentPosition();
//                Screen_PlayMusic.mediaPlayer.stop();
//                Intent intent = new Intent(Music_Offline.this,Screen_PlayMusic.class).putExtra("position",pos)
//                        .putExtra("time",time+1000);
//                startActivity(intent);
                break;

        }
    }
}
