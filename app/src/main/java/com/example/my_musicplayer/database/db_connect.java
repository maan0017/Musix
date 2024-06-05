package com.example.my_musicplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.my_musicplayer.fragments.Playlist;

public class db_connect extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_Musix";
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    public db_connect(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        Toast.makeText(context,"reached db_connect constructor",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the database tables
        Toast.makeText(context,"query executed successfully",Toast.LENGTH_SHORT).show();
        db.execSQL("CREATE TABLE IF NOT EXISTS Playlists ( INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT)");

        //db reference
        //read db
        SQLiteDatabase db_read = this.getReadableDatabase();

        //write inside db
        SQLiteDatabase db_write = this.getWritableDatabase();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades
        // This method is called when the database version is upgraded
    }

    public void addPlaylist(String name){
        SQLiteDatabase db = this.getWritableDatabase();
//        db.insert(Playlists,null,null);
    }
}
