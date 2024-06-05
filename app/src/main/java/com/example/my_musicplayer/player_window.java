package com.example.my_musicplayer;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.my_musicplayer.ReadFiles.GetAllAudio;
import com.example.my_musicplayer.ReadFiles.MusicFiles;

import java.util.ArrayList;
import java.util.Random;

public class player_window extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    TextView song_title;
    TextView song_artist;
    TextView duration_played;
    TextView total_duration;
    int currentSongDuration, totalSongDuration, shuffle, loop;
    ImageView song_img, play_pause_btn, previous_btn, next_btn, replay_10sec_btn, forward_10sec_btn, loop_btn_img, shuffle_btn_img;
    RelativeLayout shuffle_btn, loop_btn;
    SeekBar seekBar;

    int position = -1;
    static ArrayList<MusicFiles> arrayList;
    static Uri uri;
    static MediaPlayer mediaPlayer;

    GetAllAudio getAllAudio = new GetAllAudio();
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_window);

        initViews();
        getIntentMethod();
        song_title.setText(arrayList.get(position).getTitle());
        song_artist.setText(arrayList.get(position).getArtist());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        previous_btn.setOnClickListener(v -> previousBtnClicked());
        play_pause_btn.setOnClickListener(v -> playPauseBtnClicked());
        next_btn.setOnClickListener(v -> nextBtnClicked());

        player_window.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int current_position = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(current_position);
                    duration_played.setText(formattedTime(current_position));
                    currentSongDuration = current_position;
                }
                handler.postDelayed(this, 10);
                if (currentSongDuration == totalSongDuration) {
                    if (loop == 2) {
                        playSameSong();
                    } else {
                        nextBtnClicked();
                    }
                }
                shuffle_btn.setOnClickListener(v -> {
                    if (shuffle == 0) {
                        shuffle_btn_img.setBackgroundResource(R.drawable.shuffle_on_blue);
                        shuffle = 1;
                    } else {
                        shuffle_btn_img.setBackgroundResource(R.drawable.shuffle_fade_off_icon);
                        shuffle = 0;
                    }
                });
                loop_btn.setOnClickListener(v -> {
                    if (loop == 0) {
                        loop_btn_img.setBackgroundResource(R.drawable.loop_all_icon_blue);
                        loop = 1;
                    } else if (loop == 1) {
                        loop_btn_img.setBackgroundResource(R.drawable.right_arrow_blue);
                        loop = 2;
                    } else {
                        loop_btn_img.setBackgroundResource(R.drawable.loop_icon_fade);
                        loop = 0;
                    }
                });
            }
        });
    }

    private void playSameSong() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            uri = Uri.parse(arrayList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            player_window.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int current_position = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(current_position);
                    }
                    handler.postDelayed(this, 10);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            play_pause_btn.setBackgroundResource(R.drawable.pause_icon_);
            mediaPlayer.start();
        } else {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                uri = Uri.parse(arrayList.get(position).getPath());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                metaData(uri);
                seekBar.setMax(mediaPlayer.getDuration() / 1000);
                player_window.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            int current_position = mediaPlayer.getCurrentPosition() / 1000;
                            seekBar.setProgress(current_position);
                        }
                        handler.postDelayed(this, 10);
                    }
                });
                mediaPlayer.setOnCompletionListener(this);
                play_pause_btn.setBackgroundResource(R.drawable.play_icon_filled);
            }
        }
    }

    private void initViews() {
        song_title = findViewById(R.id.song_title);
        song_artist = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.duration_played);
        total_duration = findViewById(R.id.total_duration);
        play_pause_btn = findViewById(R.id.play_pause_btn);
        song_img = findViewById(R.id.song_image);
        seekBar = findViewById(R.id.seek_bar);
        previous_btn = findViewById(R.id.previous_btn);
        next_btn = findViewById(R.id.next_btn);
        shuffle_btn = findViewById(R.id.shuffle_btn);
        shuffle_btn_img = findViewById(R.id.shuffle_icon_img);
        loop_btn = findViewById(R.id.loop_icon);
        loop_btn_img = findViewById(R.id.loop_icon_img);
        replay_10sec_btn = findViewById(R.id.replay_10sec_icon);
        forward_10sec_btn = findViewById(R.id.forward_10sec_icon);
    }


    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        arrayList = getAllAudio.ScanAllAudioFiles();
        if (arrayList != null) {
            play_pause_btn.setBackgroundResource(R.drawable.pause_icon_);
            uri = Uri.parse(arrayList.get(position).getPath());
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        //seekBar
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        //song image
        metaData(uri);
    }
//for changing background according to music
//    private void metaData(Uri uri) {
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(uri.toString());
//        int durationTotal = Integer.parseInt(arrayList.get(position).getDuration())/1000;
//        total_duration.setText(formattedTime(durationTotal));
//
//        byte[] img = retriever.getEmbeddedPicture();
//        Bitmap bitmap;
//        if(img != null){
//            bitmap = BitmapFactory.decodeByteArray(img,0,img.length);
//            ImageAnimation(this,song_img,bitmap);
//            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                @Override
//                public void onGenerated(@Nullable Palette palette) {
//                    Palette.Swatch swatch = palette.getDominantSwatch();
//                    ImageView gradient = findViewById(R.id.song_image);
//                    RelativeLayout relativeLayout = findViewById(R.id.player_window);
//                    gradient.setBackgroundResource(R.drawable.gredient_bg);
//                    relativeLayout.setBackgroundResource(R.drawable.main_bg);
//                    GradientDrawable gradientDrawable;
//                    if(swatch != null){
//                        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{swatch.getRgb(), 0X00000000});
//                        gradient.setBackground(gradientDrawable);
//                        GradientDrawable gradientDrawableBG = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{swatch.getRgb(),swatch.getRgb()});
//                        relativeLayout.setBackground(gradientDrawableBG);
//                        song_title.setTextColor(swatch.getTitleTextColor());
//                        song_artist.setTextColor(swatch.getTitleTextColor());
//                    } else {
//
//                        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0Xff000000, 0X00000000});
//                        gradient.setBackground(gradientDrawable);
//                        GradientDrawable gradientDrawableBG = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{0Xff000000,0Xff000000});
//                        relativeLayout.setBackground(gradientDrawableBG);
//                        song_title.setTextColor(Color.WHITE);
//                        song_artist.setTextColor(Color.DKGRAY);
//                    }
//                }
//            });
//        }else {
//            Glide.with(this).asBitmap()
//                    .load(R.drawable.unknown)
//                    .into(song_img);
//            ImageView gradient = findViewById(R.id.song_image);
//            RelativeLayout relativeLayout = findViewById(R.id.player_window);
//            gradient.setBackgroundResource(R.drawable.gredient_bg);
//            relativeLayout.setBackgroundResource(R.drawable.main_bg);
//            song_title.setTextColor(Color.WHITE);
//            song_artist.setTextColor(Color.DKGRAY);
//        }
//    }

    //normal metaData
    private void metaData(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int totalDuration = Integer.parseInt(arrayList.get(position).getDuration()) / 1000;
        totalSongDuration = totalDuration;
        total_duration.setText(formattedTime(totalDuration));

        byte[] img = retriever.getEmbeddedPicture();
        if (img != null) {
            Glide.with(this).asBitmap().load(img).into(song_img);
        } else {
            Glide.with(this).asBitmap().load(R.drawable.unknown).into(song_img);
        }
    }

    private String formattedTime(int time) {
        String totalOut, totalNew;
        String seconds = String.valueOf(time % 60);
        String minutes = String.valueOf(time / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;

        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    public void previousBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (arrayList.size() - 1) : (position - 1));
            uri = Uri.parse(arrayList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_title.setText(arrayList.get(position).getTitle());
            song_artist.setText(arrayList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            player_window.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int current_position = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(current_position);
                    }
                    handler.postDelayed(this, 10);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            play_pause_btn.setBackgroundResource(R.drawable.pause_icon_);
            mediaPlayer.start();
        } else {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position - 1) < 0 ? (arrayList.size() - 1) : (position - 1));
                uri = Uri.parse(arrayList.get(position).getPath());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                metaData(uri);
                song_title.setText(arrayList.get(position).getTitle());
                song_artist.setText(arrayList.get(position).getArtist());
                seekBar.setMax(mediaPlayer.getDuration() / 1000);
                player_window.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            int current_position = mediaPlayer.getCurrentPosition() / 1000;
                            seekBar.setProgress(current_position);
                        }
                        handler.postDelayed(this, 10);
                    }
                });
                mediaPlayer.setOnCompletionListener(this);
                play_pause_btn.setBackgroundResource(R.drawable.play_icon_filled);
            }
        }
    }

    public void playPauseBtnClicked() {
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        player_window.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int current_position = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(current_position);
                }
                handler.postDelayed(this, 10);
            }
        });
        if (mediaPlayer.isPlaying()) {
            play_pause_btn.setBackgroundResource(R.drawable.play_icon_filled);
            mediaPlayer.pause();
        } else {
            play_pause_btn.setBackgroundResource(R.drawable.pause_icon_);
            mediaPlayer.start();
        }
    }

    public void nextBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffle == 1) {
                position = getRandom(arrayList.size() - 1);
            } else if (shuffle == 0) {
                position = ((position + 1) % arrayList.size());
            }
            uri = Uri.parse(arrayList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_title.setText(arrayList.get(position).getTitle());
            song_artist.setText(arrayList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            player_window.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int current_position = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(current_position);
                    }
                    handler.postDelayed(this, 10);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            play_pause_btn.setBackgroundResource(R.drawable.pause_icon_);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffle == 1) {
                position = getRandom(arrayList.size() - 1);
            } else if (shuffle == 0) {
                position = ((position + 1) % arrayList.size());
            }
            uri = Uri.parse(arrayList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            song_title.setText(arrayList.get(position).getTitle());
            song_artist.setText(arrayList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            player_window.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int current_position = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(current_position);
                    }
                    handler.postDelayed(this, 10);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            play_pause_btn.setBackgroundResource(R.drawable.play_icon_filled);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnClicked();
    }
}