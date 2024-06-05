package com.example.my_musicplayer;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import java.util.Objects;

public class MusixService extends Service {

    //player
    ExoPlayer player;
    MediaPlayer mediaPlayer;
    PlayerNotificationManager playerNotificationManager;
    player_window pw;
    private String CHANNEL_ID;
    private int requestCode;
    private final IBinder serviceBinder = new ServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        CHANNEL_ID = getString(R.string.channel_id);
        requestCode = getResources().getInteger(R.integer.request_code);
        player = new ExoPlayer.Builder(getApplicationContext()).build();

        //audio focus attributes
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(C.USAGE_MEDIA).setContentType(C.CONTENT_TYPE_MUSIC).build();

        //set audio attributes to the player
        player.setAudioAttributes(audioAttributes, true);

        //notification manager
        final int notificationId = 11111;
        playerNotificationManager = new PlayerNotificationManager.Builder(this, notificationId, CHANNEL_ID).setNotificationListener(notificationListener).setChannelImportance(IMPORTANCE_HIGH).setSmallIconResourceId(R.drawable.music_player_3).setChannelDescriptionResourceId(R.string.app_name).setNextActionIconResourceId(R.drawable.next_btn).setPreviousActionIconResourceId(R.drawable.previous_btn).setPauseActionIconResourceId(R.drawable.pause_icon_).setPlayActionIconResourceId(R.drawable.play_icon_filled).setChannelNameResourceId(R.string.notification_channelName).setMediaDescriptionAdapter(descriptionAdapter).build();

        //set player to the notification manager
        playerNotificationManager.setPlayer(player);
        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_MAX);
        playerNotificationManager.setUseRewindAction(false);
        playerNotificationManager.setUseFastForwardAction(false);
    }

    //notification listener
    PlayerNotificationManager.NotificationListener notificationListener = new PlayerNotificationManager.NotificationListener() {
        @Override
        public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
            PlayerNotificationManager.NotificationListener.super.onNotificationCancelled(notificationId, dismissedByUser);
            stopForeground(true);
            if(player.isPlaying()){
                player.pause();
            }
        }

        @Override
        public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
            PlayerNotificationManager.NotificationListener.super.onNotificationPosted(notificationId, notification, ongoing);
            startForeground(notificationId,notification);
        }
    };

    //notification description adapter
    PlayerNotificationManager.MediaDescriptionAdapter descriptionAdapter = new PlayerNotificationManager.MediaDescriptionAdapter() {
        @Override
        public CharSequence getCurrentContentTitle(Player player) {
            return Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title;
        }

        @Nullable
        @Override
        public PendingIntent createCurrentContentIntent(Player player) {
            //intent to open app when clicked
            Intent openAppIntent = new Intent(getApplicationContext(), MainActivity.class);
            return PendingIntent.getActivity(getApplicationContext(), requestCode, openAppIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        }

        @Nullable
        @Override
        public CharSequence getCurrentContentText(Player player) {
            return null;
        }

        @Nullable
        @Override
        public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
            //try creating an Image view on the fly then get its drawable
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageURI(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.artworkUri);

            //get imageView drawable
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            if (bitmapDrawable == null) {
                bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(), R.drawable.unknown);
            }
            assert bitmapDrawable != null;
            return bitmapDrawable.getBitmap();
        }
    };

    @Override
    public void onDestroy() {
        //release the player
        if(player.isPlaying()){
            player.stop();
        }
        playerNotificationManager.setPlayer(null);
        player.release();
        player=null;
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case "ACTION_PLAY_PAUSE":
                    //Handle play/pause action
                    pw.playPauseBtnClicked();
                    break;
                case "ACTION_NEXT":
                    // Handle next action
                    pw.nextBtnClicked();
                    break;
                case "ACTION_PREVIOUS":
                    // Handle previous action
                    pw.previousBtnClicked();
                    break;
            }
        }
        //showNotification();
        return START_NOT_STICKY;
    }

    private void start() {
        Toast.makeText(this, "start method", Toast.LENGTH_SHORT).show();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "music_player").setContentTitle("Music Player").setContentText("content text").setSmallIcon(R.drawable.music_player_3).setContentIntent(pendingIntent);

        startForeground(1, notification.build());
    }

    private void showNotification() {
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_custom);

        // Set data for views
        notificationLayout.setTextViewText(R.id.song_title, "Song Title");
        notificationLayout.setTextViewText(R.id.song_artist, "Artist Name");
        //notificationLayout.setImageViewBitmap(R.id.notification_album_art, BitmapFactory.decodeResource(getResources(), R.drawable.album_art)); // Example album art

        // Set click actions for play pause button
        Intent prevIntent = new Intent(this, MusixService.class);
        prevIntent.setAction("ACTION_PREVIOUS");
        PendingIntent prevPendingIntent = PendingIntent.getService(this, 0, prevIntent, PendingIntent.FLAG_IMMUTABLE);

        // Set click actions for play pause button
        Intent playPauseIntent = new Intent(this, MusixService.class);
        playPauseIntent.setAction("ACTION_PLAY_PAUSE");
        PendingIntent playPausePendingIntent = PendingIntent.getService(this, 0, playPauseIntent, PendingIntent.FLAG_IMMUTABLE);

        // Set click actions for play pause button
        Intent nextIntent = new Intent(this, MusixService.class);
        nextIntent.setAction("ACTION_NEXT");
        PendingIntent nextPendingIntent = PendingIntent.getService(this, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE);


        notificationLayout.setOnClickPendingIntent(R.id.notification_prev, prevPendingIntent);
        notificationLayout.setOnClickPendingIntent(R.id.notification_play_pause, playPausePendingIntent);
        notificationLayout.setOnClickPendingIntent(R.id.notification_next, nextPendingIntent);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.music_player_3).setCustomContentView(notificationLayout).setStyle(new androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle()).setPriority(NotificationCompat.PRIORITY_HIGH).build();

        startForeground(1, notification);
    }

    public class ServiceBinder extends Binder {
        public MusixService getPlayerService() {
            return MusixService.this;
        }
    }
}
