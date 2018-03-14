package com.example.xuant.mediaplayer.Service;

import com.example.xuant.mediaplayer.Model.Server.Playlist;
import com.example.xuant.mediaplayer.Model.Server.SongOnline;
import com.example.xuant.mediaplayer.Model.Server.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by xuant on 13/05/2017.
 */

public interface Service { //Định nghĩa các REST API (Api Services) cho Retrofit

   // Hàm  lấy songs theo countries
   @GET("/getsongbycountry")
   Call<ArrayList<SongOnline>> getSongByContry(@Query("id_country") int id_country);

   // Hàm  lấy songs favourite theo user
   @GET("/getsongfavourite")
   Call<ArrayList<SongOnline>> getSongFavourite(@Query("user_id") int user_id);

   // Hàm  lấy songs playlist
   @GET("/getsongplaylist")
   Call<ArrayList<SongOnline>> getSongPlaylist(@Query("id_playlist") int id_playlist);

   // Hàm  lấy songs
   @GET("/getallsong")
   Call<ArrayList<SongOnline>> getAllSong();

   //Hàm lấy playlist theo user
   @GET("/getplaylist")
   Call<ArrayList<Playlist>> getPlaylist(@Query("id_users") int id_users);

   // Hàm  lấy user theo username
   @GET("/getuser")
   Call<ArrayList<User>> getUser(@Query("username") String username);

   // thêm vào favourite
   @FormUrlEncoded
   @POST("/insertfavourite")
   Call<ArrayList<SongOnline>> insertFavourite(@Field("song_id") int song_id, @Field("user_id") int user_id);

   // thêm vào playlist
   @FormUrlEncoded
   @POST("/insertmusictoplaylist")
   Call<ArrayList<SongOnline>> insertMusictoPlaylist(@Field("id_playlist") int id_playlist, @Field("id_song") int id_song);

   // thêm playlist
   @FormUrlEncoded
   @POST("/insertplaylist")
   Call<ArrayList<Playlist>> insertPlaylist(@Field("name") String name, @Field("id_users") int id_users);

   // thêm vào favourite
   @FormUrlEncoded
   @POST("/deletefavourite")
   Call<ArrayList<SongOnline>> deleteFavourite(@Field("song_id") int song_id, @Field("user_id") int user_id);

   // xóa bài hát khỏi playlist
   @FormUrlEncoded
   @POST("/deletemusictfromplaylist")
   Call<ArrayList<SongOnline>> deleteMusicFromPlaylist(@Field("id_playlist") int id_playlist, @Field("id_song") int id_song);
}
