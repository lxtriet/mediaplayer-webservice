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
 * Created by xuant on 30/05/2017.
 */

public class DBFavourite {

    Service mService;

    FavouriteListener favouriteListener;

    public DBFavourite(FavouriteListener favouriteListener ) {

        this.favouriteListener = favouriteListener;
    }

    // Hàm thêm vào favourite
    public void insertFavourite(int song_id,int user_id){
        mService = Utils.getService();
        Call<ArrayList<SongOnline>> call = mService.insertFavourite(song_id,user_id);
        Log.e("Response",call.request().url().toString());
        call.enqueue(new Callback<ArrayList<SongOnline>>() {
            @Override
            public void onResponse(Call<ArrayList<SongOnline>> call, Response<ArrayList<SongOnline>> response) {
                favouriteListener.Insert_Delete_Fav(response.body());
                Log.e("","Thêm vào favourite thành công");
            }

            @Override
            public void onFailure(Call<ArrayList<SongOnline>> call, Throwable t) {
                Log.e("","Thêm vào favourite thất bại "+t.getMessage());
            }
        });
    }

    // Hàm thêm vào favourite
    public void deleteFavourite(int song_id,int user_id){
        mService = Utils.getService();
        Call<ArrayList<SongOnline>> call = mService.deleteFavourite(song_id,user_id);
        Log.e("Response",call.request().url().toString());
        call.enqueue(new Callback<ArrayList<SongOnline>>() {
            @Override
            public void onResponse(Call<ArrayList<SongOnline>> call, Response<ArrayList<SongOnline>> response) {
                favouriteListener.Insert_Delete_Fav(response.body());
                Log.e("","Xóa khỏi favourite thành công");
            }

            @Override
            public void onFailure(Call<ArrayList<SongOnline>> call, Throwable t) {
                Log.e("","Xóa khỏi favourite thất bại "+t.getMessage());
            }
        });
    }
}
