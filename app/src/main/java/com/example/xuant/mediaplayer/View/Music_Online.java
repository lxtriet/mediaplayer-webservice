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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.xuant.mediaplayer.Adapter.Song_Online_Adapter;
import com.example.xuant.mediaplayer.Database.DBSongOnline;
import com.example.xuant.mediaplayer.Database.GetSongListener;
import com.example.xuant.mediaplayer.Model.Server.SongOnline;
import com.example.xuant.mediaplayer.R;

import java.util.ArrayList;

/**
 * Created by xuant on 29/05/2017.
 */

public class Music_Online extends AppCompatActivity implements AdapterView.OnItemClickListener,View.OnClickListener,GetSongListener {

    Toolbar mToolbar;
    public static TabHost tabHost;
    TabWidget tabWidget;
    ListView lv_VPOP, lv_USUK, lv_KPOP;
    public static int CurrentTab = 4;
    public static boolean isListOpen = false;

    public static Boolean isPlaying = false;

    public static FrameLayout rootLayout;

    DBSongOnline dbSongOnline;
    public static ArrayList<SongOnline> songOnlineArrayList;
    Song_Online_Adapter song_online_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_online);
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

        lv_VPOP = (ListView) findViewById(R.id.recycler_VPop);
        lv_USUK = (ListView) findViewById(R.id.recycler_US_UK);
        lv_KPOP = (ListView) findViewById(R.id.recycler_KPop);
        lv_VPOP.setDivider(null);
        lv_VPOP.setDividerHeight(0);
        lv_USUK.setDivider(null);
        lv_USUK.setDividerHeight(0);
        lv_KPOP.setDivider(null);
        lv_KPOP.setDividerHeight(0);

        lv_VPOP.setOnItemClickListener(this);
        lv_USUK.setOnItemClickListener(this);
        lv_KPOP.setOnItemClickListener(this);

        dbSongOnline = new DBSongOnline(this);
        dbSongOnline.getSongByContry(1);

        tabHostSetup();
        // Sự kiện xử lý khi click vào các tabwidget
        tabWidget.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbSongOnline.getSongByContry(1);
                changeTab(0);
            }
        });
        tabWidget.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbSongOnline.getSongByContry(2);
                changeTab(1);
            }
        });
        tabWidget.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbSongOnline.getSongByContry(3);
                changeTab(2);
            }
        });
        // Ẩn mucsic bottom
        rootLayout = (FrameLayout)findViewById(R.id.music_bottom);
        View.inflate(this, R.layout.music_bottom, rootLayout);
        rootLayout.setVisibility(View.GONE);
//        try {
//            if(Screen_PlayMusic.mediaPlayer!=null)
//            {
//                Toast.makeText(getApplicationContext(),"Tao da di qua day",Toast.LENGTH_LONG).show();
//                Music_Bottom pb= new Music_Bottom();
//                android.app.FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.add(R.id.music_bottom, pb);
//                transaction.commit();
//            }else
//                rootLayout.setVisibility(View.GONE);
//
//        }catch (IllegalStateException e){
//            e.printStackTrace();
//        }

    }

    @Override
    public void onClick(View v) {

    }
    // Sự kiện khi click vào item danh sách bài hát online
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getAdapter() == song_online_adapter){
            onItemSongClick(position);
        }
    }
    // Hàm xử lý khi click vào item danh sách bài hát online
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
        Intent intent = new Intent(Music_Online.this,Screen_PlayMusic.class).putExtra("position",pos)
                .putExtra("time",0).putExtra("play","play").putExtra("type","online");
        startActivity(intent);
    }
    // Hàm xử lý ẩn danh sách bài hát
    public static void hideList(){
        isListOpen = false;
        CurrentTab = 3;
        tabHost.setCurrentTab(CurrentTab);
    }
    // Hàm xử lý hiển thị danh sách bài hát
    private void showList(int tab){
        isListOpen = true;
        CurrentTab = tab;
        tabHost.setCurrentTab(tab);
    }
        // Hàm thay đổi danh sách bài hát hiện tai
    private void changeTab(int tab) {
        if (CurrentTab == tab) {
            hideList();
        } else {
            showList(tab);
        }
    }
        // Hàm cái đặt cho tabhost
    public void tabHostSetup() {


        tabHost.setup();
        TabHost.TabSpec tabVPOP = tabHost.newTabSpec("V-POP");
        tabVPOP.setContent(R.id.tab_VPop);
        tabVPOP.setIndicator("V-POP");
        tabHost.addTab(tabVPOP);


        TabHost.TabSpec tabUSUK = tabHost.newTabSpec("US-UK");
        tabUSUK.setIndicator( "US-UK");
        tabUSUK.setContent(R.id.tab_US_UK);
        tabHost.addTab(tabUSUK);


        TabHost.TabSpec tabKPOP = tabHost.newTabSpec("K-POP");
        tabKPOP.setIndicator("K-POP");
        tabKPOP.setContent(R.id.tab_KPop);
        tabHost.addTab(tabKPOP);


        TextView x1 = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        x1.setTextSize(13);
        TextView x2 = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        x2.setTextSize(13);
        TextView x3 = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
        x3.setTextSize(13);


        tabWidget = tabHost.getTabWidget();

        tabWidget.getChildAt(0).setBackgroundResource(R.drawable.tab_selector);
        tabWidget.getChildAt(1).setBackgroundResource(R.drawable.tab_selector);
        tabWidget.getChildAt(2).setBackgroundResource(R.drawable.tab_selector);


        tabHost.setCurrentTab(0);
    }


    //Hàm lấy danh sách bài hát dựa theo hàm get trước
    @Override
    public void getListSong(ArrayList<SongOnline> songOnlines) {
        songOnlineArrayList = songOnlines;
        song_online_adapter = new Song_Online_Adapter(getApplicationContext(),songOnlineArrayList);
        if(CurrentTab == 0){
            lv_VPOP.setAdapter(song_online_adapter);
        }else if(CurrentTab == 1){
            lv_USUK.setAdapter(song_online_adapter);
        }else{
            lv_KPOP.setAdapter(song_online_adapter);
        }
    }
}
