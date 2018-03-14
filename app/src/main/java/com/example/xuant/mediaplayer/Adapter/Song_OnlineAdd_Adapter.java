package com.example.xuant.mediaplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xuant.mediaplayer.Database.DBSongOnline;
import com.example.xuant.mediaplayer.Database.GetSongListener;
import com.example.xuant.mediaplayer.Model.Server.SongOnline;
import com.example.xuant.mediaplayer.R;
import com.example.xuant.mediaplayer.Service.Utils;
import com.example.xuant.mediaplayer.View.Addmusic_toPlaylist;
import com.example.xuant.mediaplayer.View.Playlist_Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by xuant on 18/04/2017.
 */

public class Song_OnlineAdd_Adapter extends BaseAdapter implements GetSongListener{

    Context context;
    LayoutInflater inflater;
    String[] items;
    ArrayList<SongOnline> arrayList;
    DBSongOnline dbSongOnline;
    boolean isIn = false;
    private int pos = -1;
    // Định nghĩa Type_Adapter
    public Song_OnlineAdd_Adapter(Context context, ArrayList<SongOnline> arrayList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView txtName,txtArtist;
        final ImageView img,add_remove_intoplaylist;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_song_add, parent, false);

        txtName = (TextView) view.findViewById(R.id.custom_songadd_name);
        txtArtist = (TextView) view.findViewById(R.id.custom_songadd_artist);
        img = (ImageView) view.findViewById(R.id.custom_songadd_img);
        add_remove_intoplaylist = (ImageView) view.findViewById(R.id.add_remove_intoplaylist);
        String name = arrayList.get(position).getTITLE();
        String artist = arrayList.get(position).getARTIST();
        String url_imgitem= Utils.BASE_URL+"getimg?nameimg=";
        Picasso.with(context).load(url_imgitem+arrayList.get(position).getIMG()+".jpg")
                .placeholder(R.drawable.song_empty)
                .error(R.drawable.song_empty)
                .into(img);

        if(name.length()>=30)
            name =name.substring(0,30)+"...";
        if(artist.length()>=25)
            artist =artist.substring(0,25)+"...";
        txtName.setText(name);
        txtArtist.setText(artist);

        dbSongOnline = new DBSongOnline(this);
        isIn = false;
        try{
            for(int i=0;i< Playlist_Song.songOnlineArrayList.size();i++){
                if(Playlist_Song.songOnlineArrayList.get(i).getId() == arrayList.get(position).getId()){
                    add_remove_intoplaylist.setImageResource(R.drawable.removem);
                    isIn = true; break;
                }
            }
        }catch (NullPointerException e){ e.printStackTrace();}

        add_remove_intoplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isIn){
                    isIn = false;
                    add_remove_intoplaylist.setImageResource(R.drawable.add);
                    dbSongOnline.deleteMusicfromPlaylist(Addmusic_toPlaylist.idPlaylist,arrayList.get(position).getId());
                }else{
                    isIn = true;
                    add_remove_intoplaylist.setImageResource(R.drawable.removem);
                    dbSongOnline.insertMusictoPlaylist(Addmusic_toPlaylist.idPlaylist,arrayList.get(position).getId());
                }
            }
        });

        return view;
    }

    @Override
    public void getListSong(ArrayList<SongOnline> songOnlines) {

    }
}
