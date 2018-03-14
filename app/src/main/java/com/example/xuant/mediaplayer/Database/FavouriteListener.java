package com.example.xuant.mediaplayer.Database;

import com.example.xuant.mediaplayer.Model.Server.SongOnline;

import java.util.ArrayList;

/**
 * Created by xuant on 30/05/2017.
 */

public interface FavouriteListener {
    void Insert_Delete_Fav(ArrayList<SongOnline> songOnlines);
}
