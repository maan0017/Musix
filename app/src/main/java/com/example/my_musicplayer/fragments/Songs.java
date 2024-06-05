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
 * Use the {@link Songs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Songs extends Fragment {

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

    public Songs() {
        arrList = getAllAudio.ScanAllAudioFiles();
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Songs.
     */
    // TODO: Rename and change types and number of parameters
    public static Songs newInstance(String param1, String param2) {
        Songs fragment = new Songs();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);

        // Find the element by its ID
        recyclerView = view.findViewById(R.id.songs_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MusicAdapter adapter = new MusicAdapter(getActivity(),arrList);
        recyclerView.setAdapter(adapter);
        // Now you can use 'button' to interact with the Button view

        // Example: Set an OnClickListener for the button
        recyclerView.setOnClickListener(v -> {
            // Handle button click event
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}