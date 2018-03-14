package com.example.xuant.mediaplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xuant.mediaplayer.Model.SongOffline;
import com.example.xuant.mediaplayer.R;

import java.util.ArrayList;

import eu.gsottbauer.equalizerview.EqualizerView;


/**
 * Created by xuant on 18/04/2017.
 */

public class Song_OfflineScreen_Adapter extends BaseAdapter{

    Context context;
    LayoutInflater inflater;
    String[] items;
    ArrayList<SongOffline> arrayList;
    private int pos = -1;
    // Định nghĩa Type_Adapter
    public Song_OfflineScreen_Adapter(Context context, ArrayList<SongOffline> arrayList) {
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
        TextView txtName,txtArtist,txtID;
        EqualizerView equalizerView;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_song_screen_play, parent, false);

        txtName = (TextView) view.findViewById(R.id.custom_song_screen_name);
        txtArtist = (TextView) view.findViewById(R.id.custom_song_screen_artist);
        txtID = (TextView) view.findViewById(R.id.custom_song_screen_id);
        equalizerView = (EqualizerView) view.findViewById(R.id.custom_song_screen_equalizer_view);
        String name = arrayList.get(position).getTITLE();
        String artist = arrayList.get(position).getARTIST();
        equalizerView.setVisibility(View.GONE);
        equalizerView.setBarCount(4);
        equalizerView.setBarWidth(15);
        equalizerView.setMarginRight(5);
//        equalizerView.setBarColor(R.color.colorAccent);
        if(name.length()>=30)
            name =name.substring(0,30)+"...";
        if(artist.length()>30)
            artist =artist.substring(0,30)+"...";
        txtName.setText(name);
        txtArtist.setText(artist);
        txtID.setText(String.valueOf(arrayList.get(position).getId()));

        // Nếu type được chọn thì đổi màu chữ và hiển thị ảnh được chọn
        if(pos==position) {
            txtID.setVisibility(View.GONE);
            equalizerView.setVisibility(View.VISIBLE);
            equalizerView.animateBars();
        }
        return view;
    }
}
