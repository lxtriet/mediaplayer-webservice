package com.example.xuant.mediaplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xuant.mediaplayer.Model.Server.Playlist;
import com.example.xuant.mediaplayer.R;

import java.util.ArrayList;

/**
 * Created by xuant on 30/05/2017.
 */

public class Playlist_Adapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    String[] items;
    ArrayList<Playlist> arrayList;
    private int pos = -1;
    // Định nghĩa Type_Adapter
    public Playlist_Adapter(Context context, ArrayList<Playlist> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    // Lấy vị trí hiện tại
    public int getPosition() {
        return pos;
    }
    // Khởi tạo vị trí hiện tại
    public void setPosition(int pos) {
        this.pos = pos;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtName,txtArtist;
        ImageView img;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_playlist, parent, false);

        txtName = (TextView) view.findViewById(R.id.custom_playlist_name);
        txtName.setText(arrayList.get(position).getName());

        return view;
    }
}
