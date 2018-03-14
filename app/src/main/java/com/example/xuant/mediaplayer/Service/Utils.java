package com.example.xuant.mediaplayer.Service;

/**
 * Created by xuant on 13/05/2017.
 */

public class Utils {
    public static final String BASE_URL = "https://triet-14110208-mediaplayer.herokuapp.com/";
   //public static String BASE_URL = "http://192.168.1.4:8000/";
    // đường dẫn tới webservice
    public static Service getService() {   // Hàm get đương dẫn của service
        return RetrofitClient.getClient(BASE_URL).create(Service.class);
    }
}
