package com.example.my_musicplayer.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_musicplayer.R;
import com.example.my_musicplayer.ReadFiles.GetAllAudio;
import com.example.my_musicplayer.ReadFiles.MusicFiles;
import com.example.my_musicplayer.RecyclerViewAdapters.MusicAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Playlist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Playlist extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    ArrayList<MusicFiles> arrList;
    GetAllAudio getAllAudio = new GetAllAudio();

    public Playlist() {
        // Required empty public constructor
        arrList = getAllAudio.ScanAllAudioFiles();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Playlist.
     */
    // TODO: Rename and change types and number of parameters
    public static Playlist newInstance(String param1, String param2) {
        Playlist fragment = new Playlist();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist,container,false);

        //find the element by its ID
        recyclerView = view.findViewById(R.id.playlist_recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MusicAdapter adapter = new MusicAdapter(getActivity(),arrList);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnClickListener(v -> {
            //handle button click events
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}