package com.example.xuant.mediaplayer.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xuant on 13/05/2017.
 */

public class RetrofitClient {
    private static Retrofit retrofit = null;
    // Chuyển
    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl) // parse và map json tới object
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
