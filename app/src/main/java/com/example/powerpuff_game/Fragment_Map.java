package com.example.powerpuff_game;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;



public class Fragment_Map extends Fragment {

    public static GoogleMap mMap;
    private List<Marker> marker_list;
    private TopTen scoreBoard;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Intent i = getActivity().getIntent();
        scoreBoard = (TopTen) i.getSerializableExtra("SCORE_BOARD");
        //create array of markers
        marker_list = new ArrayList<Marker>();


        SupportMapFragment SupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        //add marker to map
        SupportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap= googleMap;
                for (Leader leader : scoreBoard.getLeaders()) {
                    if (leader.getLat() != 0 || leader.getLng() != 0) {
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(leader.getLat(), leader.getLng()))
                                .title(leader.getName()));
                        //add marker to array
                        marker_list.add(marker);
                    }
                }
            }
        });
        return view;
    }

    public void showMarker(int id){
        Marker marker = marker_list.get(id);
        marker.showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15f));
    }


}
