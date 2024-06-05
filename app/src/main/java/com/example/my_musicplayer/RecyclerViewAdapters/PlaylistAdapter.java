package com.example.my_musicplayer.RecyclerViewAdapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_musicplayer.ReadFiles.MusicFiles;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{
//
//    private final Context context;
//    ArrayList<MusicFiles> arrSongs;

    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
