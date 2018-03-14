package com.example.xuant.mediaplayer.Database;

import com.example.xuant.mediaplayer.Model.Server.Playlist;

import java.util.ArrayList;

/**
 * Created by xuant on 30/05/2017.
 */

public interface GetPlaylistListener {
    void getPlaylist(ArrayList<Playlist> playlistArrayList);
}
