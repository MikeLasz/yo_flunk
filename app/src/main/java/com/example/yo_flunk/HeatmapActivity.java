package com.example.yo_flunk;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HeatmapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    String[] geoGames, datesGames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent main_intent = getIntent();

        geoGames = main_intent.getStringArrayListExtra("geotags").toArray(new String[0]);
        datesGames = main_intent.getStringArrayListExtra("dates_games").toArray(new String[0]);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng defaultCenter = new LatLng(51.487472, 7.041235);
        LatLng center = defaultCenter;

        int counterGames = 0;
        String[] invalidGeotags = {"geotag", "null,null"}; // the values that occure when users dont choose to share the location
        List<String> list_invalid_geotags = Arrays.asList(invalidGeotags);
        for(String geotag: geoGames){
            if(!list_invalid_geotags.contains(geotag)){
                // preprocess string -> actual location in LatLng
                String[] lat_lon = geotag.split(",");
                double lat = Double.parseDouble(lat_lon[0]);
                double lon = Double.parseDouble(lat_lon[1]);
                LatLng location = new LatLng(lat, lon);
                Date date_game = new Date(Long.parseLong(datesGames[counterGames]));
                int day = date_game.getDate();
                int month = date_game.getMonth() + 1;
                int year = date_game.getYear() + 1900;
                map.addMarker(new MarkerOptions().position(location).title(day + "." + month + "." + year));
                // Set the center to the first location, otherwise it remains at its defaultCenter:
                if(center==defaultCenter){
                    center = location;
                }
            }
            counterGames = counterGames + 1;
        }
        float zoomLevel = 13.0f;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
    }
}