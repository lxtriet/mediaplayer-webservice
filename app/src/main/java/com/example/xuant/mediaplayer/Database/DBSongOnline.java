package com.example.xuant.mediaplayer.Database;

import android.util.Log;

import com.example.xuant.mediaplayer.Model.Server.SongOnline;
import com.example.xuant.mediaplayer.Service.Service;
import com.example.xuant.mediaplayer.Service.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xuant on 29/05/2017.
 */

public class DBSongOnline {

    Service mService;

    GetSongListener getSongListener;

    public DBSongOnline(GetSongListener getSongListener ) {
        this.getSongListener = getSongListener;
    }
    // Hàm lấy list item where theo điều kiện
    public void getSongByContry(final int id_country){
        mService = Utils.getService();
        Call<ArrayList<SongOnline>> call = mService.getSongByContry(id_country);
        Log.e("Response",call.request().url().toString());
        call.enqueue(new Callback<ArrayList<SongOnline>>() {
            @Override
            public void onResponse(Call<ArrayList<SongOnline>> call, Response<ArrayList<SongOnline>> response) {
                if(response.isSuccessful()) {
                    getSongListener.getListSong(response.body());
                    Log.e("Response","Lấy được "+response.body().size()+" Song theo country "+id_country);
                }
                else
                    Log.e("Response","Không lấy được Song theo country "+id_country);
            }
            @Override
            public void onFailure(Call<ArrayList<SongOnline>> call, Throwable t) {
                Log.e("Response",t.getMessage());
            }
        });
    }

    public void getSongFavourite(final int user_id){
        mService = Utils.getService();
        Call<ArrayList<SongOnline>> call = mService.getSongFavourite(user_id);
        Log.e("Response",call.request().url().toString());
        call.enqueue(new Callback<ArrayList<SongOnline>>() {
            @Override
            public void onResponse(Call<ArrayList<SongOnline>> call, Response<ArrayList<SongOnline>> response) {
                if(response.isSuccessful()) {
                    getSongListener.getListSong(response.body());
                    Log.e("Response","Lấy được "+response.body().size()+" Song theo user "+user_id);
                }
                else
                    Log.e("Response","Không lấy được Song theo user "+user_id);
            }
            @Override
            public void onFailure(Call<ArrayList<SongOnline>> call, Throwable t) {
                Log.e("Response",t.getMessage());
            }
        });
    }

    public void getSongPlaylist(final int id_playlist){
        mService = Utils.getService();
        Call<ArrayList<SongOnline>> call = mService.getSongPlaylist(id_playlist);
        Log.e("Response",call.request().url().toString());
        call.enqueue(new Callback<ArrayList<SongOnline>>() {
            @Override
            public void onResponse(Call<ArrayList<SongOnline>> call, Response<ArrayList<SongOnline>> response) {
                if(response.isSuccessful()) {
                    getSongListener.getListSong(response.body());
                    Log.e("Response","Lấy được song từ playlist "+id_playlist);
                }
                else
                    Log.e("Response","Không lấy được song từ playlist "+id_playlist);
            }
            @Override
            public void onFailure(Call<ArrayList<SongOnline>> call, Throwable t) {
                Log.e("Response",t.getMessage());
            }
        });
    }

    public void getAllSong(){
        mService = Utils.getService();
        Call<ArrayList<SongOnline>> call = mService.getAllSong();
        Log.e("Response",call.request().url().toString());
        call.enqueue(new Callback<ArrayList<SongOnline>>() {
            @Override
            public void onResponse(Call<ArrayList<SongOnline>> call, Response<ArrayList<SongOnline>> response) {
                if(response.isSuccessful()) {
                    getSongListener.getListSong(response.body());
                    Log.e("Response","Lấy được song từ playlist ");
                }
                else
                    Log.e("Response","Không lấy được song từ playlist ");
            }
            @Override
            public void onFailure(Call<ArrayList<SongOnline>> call, Throwable t) {
                Log.e("Response",t.getMessage());
            }
        });
    }

    public void insertMusictoPlaylist(final int id_playlist,int id_song){
        mService = Utils.getService();
        Call<ArrayList<SongOnline>> call = mService.insertMusictoPlaylist(id_playlist,id_song);
        Log.e("Response",call.request().url().toString());
        call.enqueue(new Callback<ArrayList<SongOnline>>() {
            @Override
            public void onResponse(Call<ArrayList<SongOnline>> call, Response<ArrayList<SongOnline>> response) {
                if(response.isSuccessful()) {
                    getSongListener.getListSong(response.body());
                    Log.e("Response","insert bài hát thành công vào "+id_playlist);
                }
                else
                    Log.e("Response","insert bài hát không thành công vào "+id_playlist);
            }
            @Override
            public void onFailure(Call<ArrayList<SongOnline>> call, Throwable t) {
                Log.e("Response",t.getMessage());
            }
        });
    }

    public void deleteMusicfromPlaylist(final int id_playlist,int id_song){
        mService = Utils.getService();
        Call<ArrayList<SongOnline>> call = mService.deleteMusicFromPlaylist(id_playlist,id_song);
        Log.e("Response",call.request().url().toString());
        call.enqueue(new Callback<ArrayList<SongOnline>>() {
            @Override
            public void onResponse(Call<ArrayList<SongOnline>> call, Response<ArrayList<SongOnline>> response) {
                if(response.isSuccessful()) {
                    getSongListener.getListSong(response.body());
                    Log.e("Response","delete bài hát thành công vào "+id_playlist);
                }
                else
                    Log.e("Response","delete bài hát không thành công vào "+id_playlist);
            }
            @Override
            public void onFailure(Call<ArrayList<SongOnline>> call, Throwable t) {
                Log.e("Response",t.getMessage());
            }
        });
    }
}
