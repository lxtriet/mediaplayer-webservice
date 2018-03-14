package com.example.xuant.mediaplayer.Database;

import android.util.Log;

import com.example.xuant.mediaplayer.Model.Server.Playlist;
import com.example.xuant.mediaplayer.Service.Service;
import com.example.xuant.mediaplayer.Service.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xuant on 30/05/2017.
 */

public class DBPlaylist {

    Service mService;

    GetPlaylistListener getPlaylistListener;

    public DBPlaylist(GetPlaylistListener getPlaylistListener ) {

        this.getPlaylistListener = getPlaylistListener;
    }
    // Hàm lấy user theo mail
    public void getPlaylist(int id_users){
        mService = Utils.getService();
        Call<ArrayList<Playlist>> call = mService.getPlaylist(id_users);
        Log.e("Response",call.request().url().toString());
        call.enqueue(new Callback<ArrayList<Playlist>>() {
            @Override
            public void onResponse(Call<ArrayList<Playlist>> call, Response<ArrayList<Playlist>> response) {
                getPlaylistListener.getPlaylist(response.body());
                Log.e("","Lấy Playlist thành công");
            }

            @Override
            public void onFailure(Call<ArrayList<Playlist>> call, Throwable t) {
                Log.e("","Lấy Playlist thất bại "+t.getMessage());
            }
        });
    }

    public void insertPlaylist(String name,int id_users){
        mService = Utils.getService();
        Call<ArrayList<Playlist>> call = mService.insertPlaylist(name,id_users);
        Log.e("Response",call.request().url().toString());
        call.enqueue(new Callback<ArrayList<Playlist>>() {
            @Override
            public void onResponse(Call<ArrayList<Playlist>> call, Response<ArrayList<Playlist>> response) {
                getPlaylistListener.getPlaylist(response.body());
                Log.e("","Thêm Playlist thành công");
            }

            @Override
            public void onFailure(Call<ArrayList<Playlist>> call, Throwable t) {
                Log.e("","Thêm Playlist thất bại "+t.getMessage());
            }
        });
    }


}
