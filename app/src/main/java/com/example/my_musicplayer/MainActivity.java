package com.example.my_musicplayer;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.my_musicplayer.ReadFiles.GetAllAudio;
import com.example.my_musicplayer.database.db_connect;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    public static int REQUEST_CODE;

    public int permissionGranted = 0;
    private String[] multiplePermissionsNameList;
    private String CHANNEL_ID;

    ExoPlayer player;
    //is the act. bound?
    boolean isBound = false;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity_onCreate", "app started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CHANNEL_ID = getString(R.string.channel_id);
        REQUEST_CODE = getResources().getInteger(R.integer.request_code);

        if (Build.VERSION.SDK_INT >= 33) {
            multiplePermissionsNameList = new String[]{Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.POST_NOTIFICATIONS};
        } else {
            multiplePermissionsNameList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.POST_NOTIFICATIONS};
        }
        permissions();
        if (permissionGranted == 1) {
//            Toast.makeText(this,"executed",Toast.LENGTH_SHORT).show();
            new GetAllAudio(this);
        }

        //showNotification();
        //MusixNotifications musixNotifications = new MusixNotifications(getApplicationContext());
        //musixNotifications.showNotification();

        //service
        //startService(new Intent(MainActivity.this, MusixService.class));

        Log.d("MainActivity_onCreate", "MusixNotification Reached");
        Log.d("MainActivity_onCreate", "createNotificationChannelReached");
        //createNotificationChannel();
        //MusixNotifications musixNotifications = new MusixNotifications(this);
        //musixNotifications.showNotification();
        Log.d("MainActivity_onCreate", "MusixNotification exited");
        /*MusixNotifications musixNotifications = new MusixNotifications(getApplicationContext());
        musixNotifications.showNotification();*/

        //toolbar
//        Toolbar toolbar=findViewById(R.id.toolbar);

        //fragments
        TabLayout tab = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tab.setupWithViewPager(viewPager);

        db_connect db = new db_connect(this);

        // Change the navigation bar color
        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.black));

        //service
        startService(new Intent(MainActivity.this, MusixService.class));

        //bind to the MusixService , and every thing after the binding
        doBindService();
    }

    private void doBindService() {
        Intent playerServiceIntent = new Intent(this, MusixService.class);
        bindService(playerServiceIntent, playerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection playerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //get the service instance
            MusixService.ServiceBinder binder = (MusixService.ServiceBinder) service;
            player = binder.getPlayerService().player;
            isBound = true;

            //ready to show songs
            //call player control method
            playerControls();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void playerControls() {

    }

    @Override
    public String getPackageName() {
        return super.getPackageName();
    }

    public void permissions() {
        if (Build.VERSION.SDK_INT >= 33 && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this,"1nd way",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, multiplePermissionsNameList, REQUEST_CODE);
        } else if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this,"2nd way",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, multiplePermissionsNameList, REQUEST_CODE);
        } else {
//            Toast.makeText(this,"permission already granted",Toast.LENGTH_SHORT).show();
            permissionGranted = 1;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            // If request is cancelled, the result arrays are empty
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                // Proceed with reading from external storage
//                Toast.makeText(this,"permission granted successfully",Toast.LENGTH_SHORT).show();
                permissionGranted = 1;
            } else {
                // Permission denied
                // Handle permission denied scenario
                // For example, display a message or disable functionality
//                Toast.makeText(this,"permission not granted",Toast.LENGTH_SHORT).show();
                // Permission is not granted
                // Request the permission
                permissionGranted = 0;
            }
        }
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        return super.getSystemService(name);
    }

    private void createNotificationChannel() {
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_custom);
        Log.d("MainActivity", "0");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        Log.d("MainActivity", "1");
        builder.setSmallIcon(R.drawable.music_player_3).setContentTitle("Music Player").setContentText("maan").setAutoCancel(false).setCustomContentView(notificationLayout).setPriority(NotificationCompat.PRIORITY_HIGH);

        Log.d("MainActivity", "2");
        Intent intent = new Intent(getApplicationContext(), MusixNotifications.class);
        Log.d("MainActivity", "3");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.d("MainActivity", "4");
        intent.putExtra("name", "value");

        Log.d("MainActivity", "5");
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);

        Log.d("MainActivity", "6");
        builder.setContentIntent(pendingIntent);

        Log.d("MainActivity", "7");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d("MainActivity", "8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.notification_channelName), NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(notificationChannel);
            }
        }

        Log.d("MainActivity", "9");
        notificationManager.notify(0, builder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //release the player

        doUnbindService();
    }

    private void doUnbindService() {
        if (isBound) {
            unbindService(playerServiceConnection);
            isBound = false;
        }
    }
}