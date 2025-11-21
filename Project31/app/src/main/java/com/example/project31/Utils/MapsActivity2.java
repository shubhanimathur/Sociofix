package com.example.project31.Utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.project31.Dto.LocationDto;
import com.example.project31.Dto.NotificationDto;
import com.example.project31.Dto.TalukaDto;
import com.example.project31.R;
import com.example.project31.post.CreatePost;
import com.example.project31.profile.Profile;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.example.project31.R;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

//import com.example.project31.Utils.databinding.ActivityMaps2Binding;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //private ActivityMaps2Binding binding;
    private Set<NotificationDto> latestPostNoti;
    private TalukaDto latestPostTaluka;
    private String address;
    private LocationDto currPostLoc;
    double radius = 5000;
    private Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        setContentView(R.layout.activity_maps2);
//        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
//        View view = inflater.inflate(R.layout.activity_maps, null, false);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        latestPostNoti = (Set<NotificationDto>) intent.getSerializableExtra("latestPostNoti");
        latestPostTaluka = (TalukaDto) intent.getSerializableExtra("latestPostTaluka");
        address = intent.getStringExtra("address");
        currPostLoc = (LocationDto) intent.getSerializableExtra("currPostLoc");
        btnOK= findViewById(R.id.btnOK);
    //    Toast.makeText(this, "Oncreate", Toast.LENGTH_SHORT).show();
        Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "In maps2 "+address +"  "+currPostLoc.getLatitude()+"   "+latestPostNoti+"   "+ latestPostTaluka.getTalukaPolygon());

    btnOK.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(MapsActivity2.this, Profile.class);
            startActivity(intent);
        }
    });


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
        LatLng sydney = new LatLng(-34, 151);
    //    Toast.makeText(this, "on ready", Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        makeCircle(currPostLoc,address);
        plotNGOs(currPostLoc,latestPostNoti);
        LatLng[] talukaPoly = talukaStringToArray(latestPostTaluka);
        plotTalukaPolygon(talukaPoly);
    }

    public void makeCircle(LocationDto currPostLoc, String address) {

    //    Toast.makeText(this, "radius", Toast.LENGTH_SHORT).show();
        if (currPostLoc != null) {
  //          Toast.makeText(this, "make circle", Toast.LENGTH_SHORT).show();
            if (currPostLoc.getLatitude() != null && currPostLoc.getLongitude() != null) {
                LatLng postLatlng = new LatLng(currPostLoc.getLatitude(), currPostLoc.getLongitude());
                if(address!=null) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(postLatlng).title(address));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 12)); // 12 is the zoom level
                }
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(postLatlng));
                Circle circle1 = mMap.addCircle(new CircleOptions()
                        .center(postLatlng)
                        .radius(radius)
                        .fillColor(Color.argb(64, 0, 0, 255))
                        .strokeColor(Color.argb(192, 0, 0, 255))
                        .strokeWidth(10));


            }


        }
    }

    public void plotNGOs(LocationDto currPostLoc, Set<NotificationDto> notificationSet){

        if (currPostLoc != null) {
   //         Toast.makeText(this, "make circle", Toast.LENGTH_SHORT).show();
            if (currPostLoc.getLatitude() != null && currPostLoc.getLongitude() != null) {
                LatLng postLatlng = new LatLng(currPostLoc.getLatitude(), currPostLoc.getLongitude());
                if(notificationSet.size()!=0) {
                    List<LatLng> randomPoints = generateRandomPoints(postLatlng, radius, notificationSet.size());

                    // Add the random points to the GoogleMap object
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if (marker.isInfoWindowShown()) {
                                marker.hideInfoWindow();
                            } else {
                                marker.showInfoWindow();
                            }
                            return true;
                        }
                    });

                    int i=0;
                    for (NotificationDto notification : notificationSet) {

                        Marker m = mMap.addMarker(new MarkerOptions()
                                .position(randomPoints.get(i))
                                .title("Organization: "+notification.getToUser().getName())
                                .snippet(notification.getToUser().getUserId())
                                .icon(vectorToBitmap(R.drawable.ic_baseline_account_balance_24))
                        );
                        // m.showInfoWindow();
                        i++;
                    }
                }
            }
        }
    }

    private BitmapDescriptor vectorToBitmap(@DrawableRes int resId) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), resId, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    // Helper method to generate n random points inside the circle
    private List<LatLng> generateRandomPoints(LatLng center, double radius, int n) {
        List<LatLng> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double angle = Math.random() * 2 * Math.PI;
            double r = Math.sqrt(Math.random()) * radius;
            double x = r * Math.cos(angle);
            double y = r * Math.sin(angle);
            double lat = center.latitude + x / 111111.0;
            double lng = center.longitude + y / (111111.0 * Math.cos(center.latitude * Math.PI / 180.0));
            points.add(new LatLng(lat, lng));
        }
        return points;
    }

    public LatLng[] talukaStringToArray(TalukaDto latestPostTaluka) {
        if (latestPostTaluka != null) {
            String coordinatesString = latestPostTaluka.getTalukaPolygon();
            Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "In maps2 "+coordinatesString.length());
            String[] coordinateStrings = coordinatesString.replaceAll("[()]", "").split(",");
            Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "In maps2 "+coordinateStrings[0]);

            double[][] coordinates = new double[coordinateStrings.length / 2][2];
            for (int i = 0; i < coordinateStrings.length; i++) {
                if (i % 2 == 0) {
                    coordinates[i / 2][0] = Double.parseDouble(coordinateStrings[i]);
                } else {
                    coordinates[i / 2][1] = Double.parseDouble(coordinateStrings[i]);
                }

            }

            LatLng[] coordinatesArray = new LatLng[coordinates.length];
            for (int i = 0; i < coordinates.length; i++) {
                coordinatesArray[i] = new LatLng(coordinates[i][0], coordinates[i][1]);
            }

            return coordinatesArray;
        }
        return null;
    }

    public void plotTalukaPolygon(LatLng[] currentTalukaPoly){
        if(currentTalukaPoly!=null) {
   //         Toast.makeText(this, "plot taluka", Toast.LENGTH_SHORT).show();
            PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.add(currentTalukaPoly);
            polygonOptions.strokeColor(Color.RED);
            polygonOptions.fillColor(Color.argb(64, 255, 0, 0));

            //GoogleMap map = ...; // get reference to your GoogleMap object
            Polygon polygon = mMap.addPolygon(polygonOptions);
        }
    }

}