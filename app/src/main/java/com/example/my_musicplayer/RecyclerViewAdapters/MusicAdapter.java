package com.example.my_musicplayer.RecyclerViewAdapters;

////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////// song's with images and fast loading ///////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.my_musicplayer.MusixService;
import com.example.my_musicplayer.R;
import com.example.my_musicplayer.ReadFiles.MusicFiles;
import com.example.my_musicplayer.player_window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<MusicFiles> arrSongs;
    public MusicAdapter(Context context, ArrayList<MusicFiles> arrSongs) {
        this.context = context;
        this.arrSongs = arrSongs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_view_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.song.setText(arrSongs.get(position).getTitle());
        holder.singer.setText(arrSongs.get(position).getArtist());
        //retrieving music image

        // Use an AsyncTask to retrieve album art
        new LoadAlbumArtTask(holder.songImg).execute(arrSongs.get(position).getPath());

        try {
//            ((SpecialOfferViewHolder)holder).imageContainer.cancelRequest();
        } catch (Exception e) {

        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, player_window.class);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });
        holder.menuMore.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
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

    private void deleteFile(int position, View v) {
        arrSongs.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, arrSongs.size());
    }

    @Override
    public int getItemCount() {
        return arrSongs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView song, singer;
        ImageView songImg, menuMore;

        @SuppressLint("SetTextI18n")
        public ViewHolder(View itemView) {
            super(itemView);
            song = itemView.findViewById(R.id.songs_name);
            singer = itemView.findViewById(R.id.singer);
            songImg = itemView.findViewById(R.id.song_image);
            menuMore = itemView.findViewById(R.id.songs_more_option);
        }
    }

    class LoadAlbumArtTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView songImg;

        public LoadAlbumArtTask(ImageView imageView) {
            this.songImg = imageView;
        }

        @NonNull
        @Override
        protected Bitmap doInBackground(String... params) {
            String audioFilePath = params[0];
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(audioFilePath);

            byte[] artBytes = mmr.getEmbeddedPicture();
            try {
                mmr.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (artBytes != null) {
                return BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                Glide.with(songImg.getContext()).load(bitmap).override(songImg.getWidth(), songImg.getHeight()).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).placeholder(R.drawable.unknown) // Show a placeholder while the image is loading
                        .into(songImg);
            } else {
                songImg.setImageResource(R.drawable.unknown);
            }
        }
    }
}
