package com.example.my_musicplayer.ReadFiles;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GetAllAudio extends AppCompatActivity {

    static Context context;

    public GetAllAudio() {
    }

    public GetAllAudio(Context context) {
        this.context = context;
//        Toast.makeText(context,"reached to constructor",Toast.LENGTH_SHORT).show();
        ScanAllAudioFiles();
    }

    public ArrayList<MusicFiles> ScanAllAudioFiles() {
//        Toast.makeText(context,"reached to scan files",Toast.LENGTH_SHORT).show();
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA,    // for path
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID};
//        Toast.makeText(context,MediaStore.Audio.Media.TITLE,Toast.LENGTH_SHORT).show();
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);
                String id = cursor.getString(5);

                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration, id);
                //take log.e for check
                Log.e("path" + path, "album" + album);
                tempAudioList.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudioList;
    }
}
