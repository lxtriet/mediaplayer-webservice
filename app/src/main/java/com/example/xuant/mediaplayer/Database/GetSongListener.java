package com.example.xuant.mediaplayer.Database;

import com.example.xuant.mediaplayer.Model.Server.SongOnline;

import java.util.ArrayList;

/**
 * Created by xuant on 29/05/2017.
 */

public interface GetSongListener {
    void  getListSong(ArrayList<SongOnline> songOnlines);
}
