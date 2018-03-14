package com.example.xuant.mediaplayer.Database;

import com.example.xuant.mediaplayer.Model.Server.User;

import java.util.ArrayList;

/**
 * Created by xuant on 30/05/2017.
 */

public interface GetUserListener {
    void  getUser(ArrayList<User> userArrayList);
}
