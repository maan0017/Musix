package com.example.my_musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MusixNotifications extends AppCompatActivity {


    Context context;
    public static final int REQUEST_CODE = 1;
    TextView textView;
    //String[] CHANNEL_ID = context.getResources().getStringArray(R.array.musix_notification_channelId);
    private String CHANNEL_ID;

    MusixNotifications() {
    }

    MusixNotifications(Context context) {
        this.context = context;
        showNotification();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        Log.d("MusixNotification_onCreate", "0");
        super.onCreate(savedInstanceState, persistentState);
        CHANNEL_ID= getString(R.string.channel_id);
        Log.d("MusixNotification_onCreate", "1");
        setContentView(R.layout.notification_custom);
        Log.d("MusixNotification_onCreate", "2");
        textView = findViewById(R.id.song_title);

        Log.d("MusixNotification_onCreate", "3");
        String data = getIntent().getStringExtra("name");
        Log.d("MusixNotification_onCreate", "4");
        textView.setText(data);
        Log.d("MusixNotification_onCreate", "5");
    }

    public void showNotification() {
        Log.d("showNotification", "1 : Remote View");
        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_custom);

        // Set data for views
        Log.d("showNotification", "2 : Remote View --> notificationLayout");
        notificationLayout.setTextViewText(R.id.song_title, "Song Title");
        notificationLayout.setTextViewText(R.id.song_artist, "Artist Name");
        //notificationLayout.setImageViewBitmap(R.id.notification_album_art, BitmapFactory.decodeResource(context.getResources(), R.drawable.album_art)); // Example album art

        // Set click actions for buttons
        Log.d("showNotification", "3 : Intent");
        Intent playPauseIntent = new Intent(context, MainActivity.class); // Replace with appropriate class
        Log.d("showNotification", "4 : Pending Intent");
        PendingIntent playPausePendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, playPauseIntent, PendingIntent.FLAG_MUTABLE);
        Log.d("showNotification", "5 : notification layout");
        notificationLayout.setOnClickPendingIntent(R.id.notification_play_pause, playPausePendingIntent);

        Log.d("showNotification", "6 : notification builder");
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.music_player_3).setCustomContentView(notificationLayout).setStyle(new NotificationCompat.DecoratedCustomViewStyle()).setPriority(NotificationCompat.PRIORITY_DEFAULT).build();

        Log.d("showNotification", "7 : notification manager");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Log.d("showNotification", "8 : if--line 8");
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Log.d("showNotification", "9 : ending");
        notificationManager.notify(1, notification);
        Log.d("showNotification", "10 : end");
    }
}
