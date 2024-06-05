package com.example.my_musicplayer.RecyclerViewAdapters;

////////////////////////////////////////////////////
//// song's without images /////////////////////////
////////////////////////////////////////////////////

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_musicplayer.R;
import com.example.my_musicplayer.ReadFiles.MusicFiles;
import com.example.my_musicplayer.player_window;

import java.util.ArrayList;

public class MusicAdapter2 extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private final Context context;
    ArrayList<MusicFiles> arrSongs;
    public MusicAdapter2(Context context, ArrayList<MusicFiles> arrSongs){
        this.context=context;
        this.arrSongs=arrSongs;
    }

    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_view_container_2,parent,false);
        MusicAdapter.ViewHolder viewHolder = new MusicAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.song.setText(arrSongs.get(position).getTitle());
        holder.singer.setText(arrSongs.get(position).getArtist());
        //retrieving music image
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, player_window.class);
            intent.putExtra("position",position);
            context.startActivity(intent);
        });
        holder.menuMore.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context,v);
            popupMenu.getMenuInflater().inflate(R.menu.options_menu, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.delete_icon) {
                    deleteFile(position, v);
                }
                return false;
            });
        });
    }

    private void deleteFile (int position,View v){
        arrSongs.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position,arrSongs.size());
    }

    @Override
    public int getItemCount() {
        return arrSongs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView song,singer;
        ImageView menuMore;
        @SuppressLint("SetTextI18n")
        public ViewHolder(View itemView){
            super(itemView);
            song=itemView.findViewById(R.id.songs_name);
            singer=itemView.findViewById(R.id.singer);
            menuMore=itemView.findViewById(R.id.songs_more_option);
        }
    }
}
