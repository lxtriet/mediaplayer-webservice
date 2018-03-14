package com.example.xuant.mediaplayer.Model;

import java.io.Serializable;

/**
 * Created by xuant on 21/05/2017.
 */

public class SongOffline implements Serializable {
    int id;
    String DISPLAY_NAME,ALBUM,ARTIST,TRACK,TITLE,DATA, DURATION,YEAR;
    String IMG;
    Boolean isFav = false;

    public SongOffline(){

    }

    public SongOffline(int id, String DISPLAY_NAME, String ALBUM, String ARTIST, String TRACK, String TITLE, String DATA, String DURATION, String YEAR, String IMG, Boolean isFav) {
        this.id = id;
        this.DISPLAY_NAME = DISPLAY_NAME;
        this.ALBUM = ALBUM;
        this.ARTIST = ARTIST;
        this.TRACK = TRACK;
        this.TITLE = TITLE;
        this.DATA = DATA;
        this.DURATION = DURATION;
        this.YEAR = YEAR;
        this.IMG = IMG;
        this.isFav = isFav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDISPLAY_NAME() {
        return DISPLAY_NAME;
    }

    public void setDISPLAY_NAME(String DISPLAY_NAME) {
        this.DISPLAY_NAME = DISPLAY_NAME;
    }

    public String getALBUM() {
        return ALBUM;
    }

    public void setALBUM(String ALBUM) {
        this.ALBUM = ALBUM;
    }

    public String getARTIST() {
        return ARTIST;
    }

    public void setARTIST(String ARTIST) {
        this.ARTIST = ARTIST;
    }

    public String getTRACK() {
        return TRACK;
    }

    public void setTRACK(String TRACK) {
        this.TRACK = TRACK;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public String getDURATION() {
        return DURATION;
    }

    public void setDURATION(String DURATION) {
        this.DURATION = DURATION;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public String getIMG() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public Boolean getFav() {
        return isFav;
    }

    public void setFav(Boolean fav) {
        isFav = fav;
    }
}
