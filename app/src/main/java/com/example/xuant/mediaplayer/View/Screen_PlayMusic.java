package com.example.xuant.mediaplayer.View;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xuant.mediaplayer.Model.Server.SongOnline;
import com.example.xuant.mediaplayer.Model.SongOffline;
import com.example.xuant.mediaplayer.R;
import com.example.xuant.mediaplayer.Service.Utils;
import com.example.xuant.mediaplayer.View.Fragment.Fragment_Left;
import com.example.xuant.mediaplayer.View.Fragment.Fragment_Mid;
import com.example.xuant.mediaplayer.View.Fragment.Fragment_Right;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by xuant on 21/05/2017.
 */

public class Screen_PlayMusic extends AppCompatActivity implements View.OnClickListener{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Toolbar mToolbar;

    public static ImageView screen_play_shuffle, screen_play_prev, screen_play_play, screen_play_next, screen_play_repeat;
    public static TextView screen_play_timecurrent, screen_play_timeduration, screen_play_name, screen_play_artist;

    ArrayList<File> mysongs;
    public static int position;
    public static String play = "",type="",url_mp3=Utils.BASE_URL+"getmp3?namemp3=";
    public static int timecurent=0;
    public static MediaPlayer mediaPlayer;
    public static SeekBar screen_play_seekbar;
    Uri uri;

    Thread update_seekbar;
    int totaltime = 0;
    private Handler mHandler;

     int HOUR = 60*60*1000;
     int MINUTE = 60*1000;
     int SECOND = 1000;

    public static boolean isShuffle = false;
    public static boolean isLoopMore = false;
    ArrayList<SongOffline> songOfflineArrayList;
    ArrayList<SongOnline> songOnlineArrayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_play_music);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);// Tạo nút trở về activity trước
        }

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        // Thêm phần tử vào tabLayout ở home
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(1);

        tabLayout.setupWithViewPager(viewPager);

        screen_play_shuffle = (ImageView) findViewById(R.id.screen_play_shuffle);
        screen_play_prev = (ImageView) findViewById(R.id.screen_play_prev);
        screen_play_play = (ImageView) findViewById(R.id.screen_play_play);
        screen_play_next = (ImageView) findViewById(R.id.screen_play_next);
        screen_play_repeat = (ImageView) findViewById(R.id.screen_play_repeat);

        screen_play_timecurrent = (TextView) findViewById(R.id.screen_play_timecurrent);
        screen_play_timeduration = (TextView) findViewById(R.id.screen_play_timeduration);
        screen_play_name = (TextView) findViewById(R.id.screen_play_name);
        screen_play_artist = (TextView) findViewById(R.id.screen_play_artist);

        screen_play_seekbar = (SeekBar) findViewById(R.id.screen_play_seekbar);

        screen_play_shuffle.setOnClickListener(this);
        screen_play_prev.setOnClickListener(this);
        screen_play_play.setOnClickListener(this);
        screen_play_next.setOnClickListener(this);
        screen_play_repeat.setOnClickListener(this);

//        update_seekbar = new Thread(){
//            @Override
//            public void run() {
//                totaltime = mediaPlayer.getDuration();
//                int currenttime = 0;
//                screen_play_seekbar.setMax(totaltime);
//                while(currenttime < totaltime) {
//                    try{
//                        sleep(500);
//                        currenttime = mediaPlayer.getCurrentPosition();
//                        screen_play_seekbar.setProgress(currenttime);
//                    } catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
//                } screen_play_next.performClick();
//            }
//        };

        screen_play_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    mediaPlayer.seekTo(progress);
                    screen_play_seekbar.setProgress(progress);
                }
//                if(mediaPlayer != null && fromUser){
//                    mediaPlayer.seekTo(progress * 1000);
//                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Intent i = getIntent();
        Bundle b = i.getExtras();
        position = b.getInt("position",0);
        timecurent = b.getInt("time",0);
        play = b.getString("play","");
        type = b.getString("type","");

        if(type.equals("offline"))
            songOfflineArrayList = Music_Offline.songOfflineArrayList;
        else if(type.equals("online"))
            songOnlineArrayList = Music_Online.songOnlineArrayList;
        else if(type.equals("favourite"))
            songOnlineArrayList = Music_Favourite.songOnlineArrayList;
        else if(type.equals("playlist"))
            songOnlineArrayList = Playlist_Song.songOnlineArrayList;
//        mysongs = findSongs(Environment.getExternalStorageDirectory());
//        uri = Uri.parse(mysongs.get(position).toString());

//        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer = new MediaPlayer();

        if(play.equals("play")){
            try {
                mediaPlayer.stop();
                mediaPlayer.reset();
                setData();
                resizeText();

                mediaPlayer.prepare();
                mediaPlayer.seekTo(timecurent);
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            screen_play_play.setImageResource(R.drawable.ic_player_pause);
            try{
                if(mediaPlayer!=null)
                {
                    screen_play_timeduration.setText(convertTime(mediaPlayer.getDuration()));
                    mHandler = new Handler();
                    Screen_PlayMusic.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                totaltime = mediaPlayer.getDuration();
                            }catch (NullPointerException e){ e.printStackTrace();}
                            int currenttime = 0;
                            screen_play_seekbar.setMax(totaltime);
                            if(mediaPlayer != null){
                                currenttime = mediaPlayer.getCurrentPosition();
                                screen_play_timecurrent.setText(convertTime(currenttime));
                                screen_play_seekbar.setProgress(currenttime);
                            }
                            if(currenttime>=totaltime)
                            {
                                if(isLoopMore){
                                    screen_play_next.performClick();
                                    currenttime = mediaPlayer.getCurrentPosition();
                                    resizeText();
                                    screen_play_timeduration.setText(convertTime(mediaPlayer.getDuration()));
                                    screen_play_timecurrent.setText(convertTime(currenttime));
                                    screen_play_seekbar.setProgress(currenttime);
                                }else{
                                    if(mediaPlayer.isLooping()){

                                    }else
                                    {
                                        int postemp= position;
                                        screen_play_next.performClick();

                                        currenttime = mediaPlayer.getCurrentPosition();
                                        resizeText();
                                        screen_play_timeduration.setText(convertTime(mediaPlayer.getDuration()));
                                        screen_play_timecurrent.setText(convertTime(currenttime));
                                        screen_play_seekbar.setProgress(currenttime);
                                        if(type.equals("offline")){
                                            if(postemp==songOfflineArrayList.size()-1){
                                                mediaPlayer.pause();
                                                screen_play_play.setImageResource(R.drawable.ic_player_play);
                                                Fragment_Mid.fragment_mid_imagedisk.clearAnimation();
                                                Fragment_Mid.fragment_mid_equalizer_view.stopBars();
                                            }
                                        }
                                        else if(type.equals("online") || type.equals("favourite") || type.equals("playlist")){
                                            if(postemp==songOnlineArrayList.size()-1){
                                                mediaPlayer.pause();
                                                screen_play_play.setImageResource(R.drawable.ic_player_play);
                                                Fragment_Mid.fragment_mid_imagedisk.clearAnimation();
                                                Fragment_Mid.fragment_mid_equalizer_view.stopBars();
                                            }
                                        }
                                    }
                                }
                                changeImgFragmentMid();

                            }
                            mHandler.postDelayed(this, 1000);
                        }
                    });
                }
            }catch (IllegalStateException e){
                e.printStackTrace();
            }catch (NullPointerException e) {
                e.printStackTrace();
                e.getMessage();
            }

        }else{
            screen_play_timecurrent.setText(convertTime(timecurent));
            try {
                setData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            resizeText();
            screen_play_play.setImageResource(R.drawable.ic_player_play);
            Fragment_Mid.fragment_mid_imagedisk.clearAnimation();
            Fragment_Mid.fragment_mid_equalizer_view.stopBars();
           // mediaPlayer.seekTo(timecurent);
            mediaPlayer.pause();
            screen_play_seekbar.setProgress(timecurent);
            screen_play_timeduration.setText(convertTime(mediaPlayer.getDuration()));

            Toast.makeText(Screen_PlayMusic.this,String.valueOf(screen_play_timecurrent.getText()),Toast.LENGTH_LONG).show();
        }



    }

    public String convertTime(long duration) {
       String out = "";
        final int HOUR = 60*60*1000;
        final int MINUTE = 60*1000;
        final int SECOND = 1000;


        int minute = (int)(duration%HOUR)/MINUTE;
        int sec = (int)(duration%MINUTE)/SECOND;
        if(minute <10)
            out = "0"+minute+":";
        else
            out = minute+":";
        if(sec <10)
            out += "0"+sec;
        else
            out+=sec;
        return out;
    }

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.screen_play_shuffle:
                if(isShuffle)
                {
                    Toast.makeText(Screen_PlayMusic.this,"Non Shuffle",Toast.LENGTH_LONG).show();
                    isShuffle = false;
                    screen_play_shuffle.setImageResource(R.drawable.ic_player_shuffle);
                }
                else{
                    Toast.makeText(Screen_PlayMusic.this,"Shuffle",Toast.LENGTH_LONG).show();
                    isShuffle = true;
                    screen_play_shuffle.setImageResource(R.drawable.ic_player_shuffle_selected);
                }
                break;
            case R.id.screen_play_prev:
                screen_play_play.setImageResource(R.drawable.ic_player_pause);
                changeImgFragmentMid();
                if(isShuffle)
                {
                    Random rand = new Random();
                    if(type.equals("offline")){
                        position = rand.nextInt(songOfflineArrayList.size()-1) + 0;
                    }
                    else if(type.equals("online") || type.equals("favourite") || type.equals("playlist")){
                        position = rand.nextInt(songOnlineArrayList.size()-1) + 0;
                    }
                }
                else
                {
                    if(type.equals("offline")){
                        position = (position-1<0)? songOfflineArrayList.size()-1:position-1;
                    }
                    else if(type.equals("online") || type.equals("favourite") || type.equals("playlist")){
                        position = (position-1<0)? songOnlineArrayList.size()-1:position-1;
                    }
                }
                //uri = Uri.parse(mysongs.get(position).toString());
                //mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);

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
                Fragment_Mid.fragment_mid_imagedisk.startAnimation(Fragment_Mid.rotateAnimation);
                Fragment_Mid.fragment_mid_equalizer_view.animateBars();
                screen_play_timeduration.setText(convertTime(mediaPlayer.getDuration()));
                totaltime = mediaPlayer.getDuration();

                break;
            case R.id.screen_play_play:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    screen_play_play.setImageResource(R.drawable.ic_player_play);
                    Fragment_Mid.fragment_mid_imagedisk.clearAnimation();
                    Fragment_Mid.fragment_mid_equalizer_view.stopBars();
                }else{
                    mediaPlayer.start();
                    screen_play_play.setImageResource(R.drawable.ic_player_pause);
                    Fragment_Mid.fragment_mid_imagedisk.startAnimation(Fragment_Mid.rotateAnimation);
                    Fragment_Mid.fragment_mid_equalizer_view.animateBars();
                }
                break;
            case R.id.screen_play_next:
                screen_play_play.setImageResource(R.drawable.ic_player_pause);
                changeImgFragmentMid();
                if(isShuffle)
                {
                    Random rand = new Random();
                    if(type.equals("offline")){
                        position = rand.nextInt(songOfflineArrayList.size()-1) + 0;
                    }
                    else if(type.equals("online") || type.equals("favourite") || type.equals("playlist")){
                        position = rand.nextInt(songOnlineArrayList.size()-1) + 0;
                    }

                }
                else{
                    if(type.equals("offline")){
                        position = (position+1)%songOfflineArrayList.size();
                    }
                    else if(type.equals("online") || type.equals("favourite") || type.equals("playlist")){
                        position = (position+1)%songOnlineArrayList.size();
                    }
                }


//                uri = Uri.parse(mysongs.get(position).toString());
//                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);

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
                Fragment_Mid.fragment_mid_imagedisk.startAnimation(Fragment_Mid.rotateAnimation);
                Fragment_Mid.fragment_mid_equalizer_view.animateBars();
                screen_play_timeduration.setText(convertTime(mediaPlayer.getDuration()));
                totaltime = mediaPlayer.getDuration();
                break;
            case R.id.screen_play_repeat:
                if(isLoopMore){
                    screen_play_repeat.setImageResource(R.drawable.ic_player_repeat_1);
                    isLoopMore = false;
                    mediaPlayer.setLooping(true);
                }else{
                    if(mediaPlayer.isLooping()){
                        mediaPlayer.setLooping(false);
                        screen_play_repeat.setImageResource(R.drawable.ic_player_repeat);
                    }else
                    {
                        isLoopMore = true;
                        screen_play_repeat.setImageResource(R.drawable.ic_player_repeat_all);
                    }
                }
                break;
        }
    }

    public void changeImgFragmentMid(){
        if(type.equals("online") || type.equals("favourite") || type.equals("playlist")){
            String url_imgitem= Utils.BASE_URL+"getimg?nameimg=";
            try{
                Picasso.with(getApplicationContext()).load(url_imgitem+ Music_Online.songOnlineArrayList
                        .get(position).getIMG()+".jpg")
                        .placeholder(R.drawable.song_empty)
                        .error(R.drawable.song_empty)
                        .into(Fragment_Mid.fragment_mid_imagedisk);
            }catch (NullPointerException e){e.printStackTrace();
            }
        }
    }
    public void setData() throws IOException {
        if(type.equals("offline")){
            mediaPlayer.setDataSource(songOfflineArrayList.get(position).getDATA());
        }
        else if(type.equals("online") || type.equals("favourite") || type.equals("playlist")){
            mediaPlayer.setDataSource(url_mp3+songOnlineArrayList.get(position).getDATA()+".mp3");
        }
    }
    public void resizeText(){
        String name="",artist="";
       if(type.equals("online") || type.equals("favourite") || type.equals("playlist")){
            name = songOnlineArrayList.get(position).getTITLE();
            artist = songOnlineArrayList.get(position).getARTIST();
        }else if(type.equals("offline")){
            name = songOfflineArrayList.get(position).getTITLE();
            artist = songOfflineArrayList.get(position).getARTIST();
       }

        if(name.length()>25)
            name =name.substring(0,25)+"...";
        if(artist.length()>35)
            artist =artist.substring(0,35)+"...";
        screen_play_name.setText(name);
        screen_play_artist.setText(artist);
    }

    public class Pager extends FragmentStatePagerAdapter {
        //Constructor to the class
        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
        }

        //Overriding method getItem
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    Fragment_Left tab1 = new Fragment_Left();
                    return tab1;

                case 1:
                    Fragment_Mid tab2 = new Fragment_Mid();
                    return tab2;
                case 2:
                    Fragment_Right tab3 = new Fragment_Right();
                    return tab3;
                default:
                    return null;
            }

        }

        //Overriden method getCount to get the number of tabs
        @Override
        public int getCount() {
            return 3;
        }
    }

}
