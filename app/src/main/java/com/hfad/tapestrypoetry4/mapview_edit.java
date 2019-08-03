package com.hfad.tapestrypoetry4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class mapview_edit extends AppCompatActivity {

    private GoogleMap mMap;
    ArrayList<LatLng>  coordinates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview_edit);

        Intent intent = getIntent();
        final List<String>  Lats = intent.getStringArrayListExtra("LATS");
        final List<String>  Longs = intent.getStringArrayListExtra("LONGS");
        final List<String>  names = intent.getStringArrayListExtra("NAMES");


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Toast.makeText(getApplicationContext(),"Map is Ready",Toast.LENGTH_SHORT).show();
                    mMap=googleMap;
                    coordinates = new ArrayList<>();
                    for(int i=0;i<Lats.size();i++)
                    {
                        coordinates.add(new LatLng(Double.parseDouble(Lats.get(i)), Double.parseDouble(Longs.get(i))));
                        googleMap.addMarker(new MarkerOptions().position(coordinates.get(i))
                                .title(""));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates.get(i)));
                    }
            }
        });
    }
}
