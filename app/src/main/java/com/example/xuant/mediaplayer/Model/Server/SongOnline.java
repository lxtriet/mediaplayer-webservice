package com.example.xuant.mediaplayer.Model.Server;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by xuant on 29/05/2017.
 */

public class SongOnline {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("source")
    @Expose
    private String DATA;
    @SerializedName("thumbnail")
    @Expose
    private String IMG;
    @SerializedName("title")
    @Expose
    private String TITLE;
    @SerializedName("id_artist")
    @Expose
    private int id_artist;
    @SerializedName("id_country")
    @Expose
    private int id_country;
    @SerializedName("name_artist")
    @Expose
    private String ARTIST;

    public SongOnline(){

    }

    public SongOnline(int id, String DATA, String IMG, String TITLE, int id_artist, int id_country, String ARTIST) {
        this.id = id;
        this.DATA = DATA;
        this.IMG = IMG;
        this.TITLE = TITLE;
        this.id_artist = id_artist;
        this.id_country = id_country;
        this.ARTIST = ARTIST;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public String getIMG() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public int getId_artist() {
        return id_artist;
    }

    public void setId_artist(int id_artist) {
        this.id_artist = id_artist;
    }

    public int getId_country() {
        return id_country;
    }

    public void setId_country(int id_country) {
        this.id_country = id_country;
    }

    public String getARTIST() {
        return ARTIST;
    }

    public void setARTIST(String ARTIST) {
        this.ARTIST = ARTIST;
    }
}
