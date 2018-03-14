package com.example.xuant.mediaplayer.Model.Server;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by xuant on 30/05/2017.
 */

public class Playlist {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id_users")
    @Expose
    private String id_users;

    public Playlist(){

    }

    public Playlist(Integer id, String name, String id_users) {
        this.id = id;
        this.name = name;
        this.id_users = id_users;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_users() {
        return id_users;
    }

    public void setId_users(String id_users) {
        this.id_users = id_users;
    }
}
