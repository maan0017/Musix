package com.example.my_musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.my_musicplayer.fragments.Album;
import com.example.my_musicplayer.fragments.Artists;
import com.example.my_musicplayer.fragments.Folders;
import com.example.my_musicplayer.fragments.Genres;
import com.example.my_musicplayer.fragments.Playlist;
import com.example.my_musicplayer.fragments.Songs;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new Songs();
        } else if (position==1) {
            return new Playlist();
        }else if(position==2){
            return new Folders();
        } else if (position==3) {
            return new Album();
        } else if (position==4) {
            return new Artists();
        } else {
            return new Genres();
        }
    }

    //number of tabs
    @Override
    public int getCount() {
        return 6;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0){
            return "Songs";
        } else if (position==1) {
            return "Playlist";
        }else if(position==2){
            return "Folders";
        } else if (position==3) {
            return "Albums";
        } else if (position==4) {
            return "Artists";
        } else {
            return "Genres";
        }
    }
}
