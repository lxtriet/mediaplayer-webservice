package com.example.xuant.mediaplayer.Database;

import android.util.Log;

import com.example.xuant.mediaplayer.Model.Server.User;
import com.example.xuant.mediaplayer.Service.Service;
import com.example.xuant.mediaplayer.Service.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xuant on 30/05/2017.
 */

public class DBUser {

    Service mService;

    GetUserListener getUserListener;

    public DBUser(GetUserListener getUserListener ) {

        this.getUserListener = getUserListener;
    }
    // Hàm lấy user theo mail
    public void getUser(String username){
        mService = Utils.getService();
        Call<ArrayList<User>> call = mService.getUser(username);
        Log.e("Response",call.request().url().toString());
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                getUserListener.getUser(response.body());
                Log.e("","Lấy user thành công");
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.e("","Lấy user thất bại "+t.getMessage());
            }
        });
    }
}
