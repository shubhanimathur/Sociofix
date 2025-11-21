package com.example.project31.Utils;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.project31.Dto.LocationDto;
import com.example.project31.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.example.project31.Utils.databinding.ActivityMaps3Binding;

public class MapsActivity3 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //private ActivityMaps3Binding binding;
    LocationDto locationDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.explode_in_top_right, R.anim.explode_out_bottom_left);
        setContentView(R.layout.activity_maps3);
//        binding = ActivityMaps3Binding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map3);
        mapFragment.getMapAsync(this);

        locationDto = (LocationDto) getIntent().getSerializableExtra("locationDto");

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng markerPos = new LatLng(locationDto.getLatitude(), locationDto.getLongitude());
        if(locationDto.getLandmark()!=null) {
            mMap.addMarker(new MarkerOptions().position(markerPos).title(locationDto.getLandmark()));
        }
        else{
            mMap.addMarker(new MarkerOptions().position(markerPos).title("Issue's Location"));
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPos, 16));

    }
}

