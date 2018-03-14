package com.example.xuant.mediaplayer.View;

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

import com.example.xuant.mediaplayer.Adapter.Song_OnlineAdd_Adapter;
import com.example.xuant.mediaplayer.Database.DBSongOnline;
import com.example.xuant.mediaplayer.Database.GetSongListener;
import com.example.xuant.mediaplayer.Model.Server.SongOnline;
import com.example.xuant.mediaplayer.R;

import java.util.ArrayList;

/**
 * Created by xuant on 30/05/2017.
 */

public class Addmusic_toPlaylist extends AppCompatActivity implements GetSongListener,AdapterView.OnItemClickListener,View.OnClickListener{

    Toolbar mToolbar;
    public static TabHost tabHost;
    TabWidget tabWidget;
    ListView lv_VPOP, lv_USUK, lv_KPOP;
    public static int CurrentTab = 4;
    public static boolean isListOpen = false;

    public static Boolean isPlaying = false;

    public static FrameLayout rootLayout;
    public static int idPlaylist=0;

    DBSongOnline dbSongOnline;
    public static ArrayList<SongOnline> songOnlineArrayList;
    Song_OnlineAdd_Adapter song_onlineAdd_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmusic_toplaylistt);
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
        // Khi chuyển quan tab VPOP
        tabWidget.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbSongOnline.getSongByContry(1);
                changeTab(0);
            }
        });
        // Khi chuyển quan tab US-UK
        tabWidget.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbSongOnline.getSongByContry(2);
                changeTab(1);
            }
        });
        // Khi chuyển quan tab KPOP
        tabWidget.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbSongOnline.getSongByContry(3);
                changeTab(2);
            }
        });
        idPlaylist = Music_Playlist.playlistArrayList.get(Music_Playlist.positionplaylist).getId();
    }

    @Override
    public void onClick(View v) {

    }
        // Sự kiện khi click vào item music
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getAdapter() == song_onlineAdd_adapter){
            onItemSongClick(position);
        }
    }

    public void onItemSongClick(int pos){

    }
        // Sự kiện ẩn list bài hat
    public static void hideList(){
        isListOpen = false;
        CurrentTab = 3;
        tabHost.setCurrentTab(CurrentTab);
    }
        // Sự kiện hiển thi danh sách bài hát
    private void showList(int tab){
        isListOpen = true;
        CurrentTab = tab;
        tabHost.setCurrentTab(tab);
    }
    // Sự kiện thay đổi tab hiện tại
    private void changeTab(int tab) {
        if (CurrentTab == tab) {
            hideList();
        } else {
            showList(tab);
        }
    }
        // Cài đặt tabhost cho activity
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

    // Sự kiện get item bài hát khi chuyển tab
    @Override
    public void getListSong(ArrayList<SongOnline> songOnlines) {
        songOnlineArrayList = songOnlines;
        song_onlineAdd_adapter = new Song_OnlineAdd_Adapter(getApplicationContext(),songOnlineArrayList);
        if(CurrentTab == 0){
            lv_VPOP.setAdapter(song_onlineAdd_adapter);
        }else if(CurrentTab == 1){
            lv_USUK.setAdapter(song_onlineAdd_adapter);
        }else{
            lv_KPOP.setAdapter(song_onlineAdd_adapter);
        }
    }
}
