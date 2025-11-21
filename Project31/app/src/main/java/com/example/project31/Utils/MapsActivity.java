package com.example.project31.Utils;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.project31.Dto.AreaDto;
import com.example.project31.Dto.LocationDto;
import com.example.project31.Dto.Overpass.Element;
import com.example.project31.Dto.Overpass.OverpassResponse;
import com.example.project31.R;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project31.authentication.OrganizationSignUpDetails;
import com.example.project31.post.CreatePost;
import com.example.project31.retrofit.OverpassApi;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.TalukaApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import com.example.geofencingapplication.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity  implements OnMapReadyCallback{

    private GoogleMap mMap;

    // String userInputAddress="";
    TalukaApi talukaApi;

    List<String> talukaNames;
    TextView talukaSpinner,areaSpinner;
    Dialog dialog;
    ArrayList<AreaDto> responseAreaDtos;
    Button btnDone,btnSet;
    EditText etLocality;
    Location currentLocation;
    String userAddress;
    LocationDto locationDto;
    AreaDto selectedAreaDto;

    ProgressBar areaProgressBar;



    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, this);
//        binding = ActivityMapsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        RetrofitService rs = new RetrofitService();

        talukaSpinner = findViewById(R.id.taluka_spinner);
        areaSpinner= findViewById(R.id.area_spinner);
        talukaApi = rs.getRetrofit().create(TalukaApi.class);
        btnDone= findViewById(R.id.btnDone);
        btnSet=findViewById(R.id.btnSet);
        etLocality= findViewById(R.id.etLocality);
        areaProgressBar=findViewById(R.id.areaProgressBar);
        Call<List<String>> call1 = talukaApi.getTalukaNames();
        call1.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call1, Response<List<String>> response) {
                talukaNames = response.body();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,talukaNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // talukaSpinner.setAdapter(adapter);
                // fetchSuburbs(talukaNametoQuery);

            }

            @Override
            public void onFailure(Call<List<String>> call1, Throwable t) {
                Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Error occurred in taluka spinner"+t);

            }
        });


        talukaSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(),"in sector view"+talukaNames.get(0),Toast.LENGTH_SHORT).show();
                //  fetchSuburbs("Pune City");
                dialog=new Dialog(MapsActivity.this);

                dialog.setContentView(R.layout.dialog_seachable_single_spinner);

                dialog.getWindow().setLayout(1000,2000);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

              //  Button done = dialog.findViewById(R.id.done);
                // MultiSelectSearchAdapter adapter = new MultiSelectSearchAdapter(getApplicationContext(),R.layout.list_item,sectorNames);
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,talukaNames);
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        talukaSpinner.setText(adapter.getItem(position));
                        areaProgressBar.setVisibility(View.VISIBLE);
                        fetchSuburbs(adapter.getItem(position));
                        // Dismiss dialog

                        dialog.dismiss();
                    }
                });


            }
        });

        areaSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(),"in sector view"+talukaNames.get(0),Toast.LENGTH_SHORT).show();

                if(talukaSpinner.getText()==null || talukaSpinner.getText().equals("")){
                    Toast.makeText(getApplicationContext(),"Please select taluka first",Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog=new Dialog(MapsActivity.this);

                dialog.setContentView(R.layout.dialog_seachable_single_spinner);

                dialog.getWindow().setLayout(1000,2000);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                Button done = dialog.findViewById(R.id.done);
                // MultiSelectSearchAdapter adapter = new MultiSelectSearchAdapter(getApplicationContext(),R.layout.list_item,sectorNames);
                ArrayAdapter<AreaDto> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,responseAreaDtos);
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        areaSpinner.setText(adapter.getItem(position).getAreaName());
                        selectedAreaDto=adapter.getItem(position);
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });


            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedAreaDto !=null && areaSpinner!=null && areaSpinner.getText().toString().length()>=0 && talukaSpinner!=null && talukaSpinner.getText().toString().length()>=0  ){

                }else{
                    Toast.makeText(getApplicationContext(),"Please check for empty fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                userAddress=etLocality.getText()+", "+areaSpinner.getText()+", "+talukaSpinner.getText()+", "+"Maharashtra";

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocationName(userAddress, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                double latitude;
                double longitude;
                if (addresses.size() > 0) {
                    Address location = addresses.get(0);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                else{

                    latitude = -34;
                    longitude =151;
                }

                LatLng locationPoint = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(locationPoint).title(userAddress));
                // mMap.moveCamera(CameraUpdateFactory.newLatLng(locationPoint));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationPoint, 16)); // 16 is the zoom level

                locationDto= new LocationDto( latitude,longitude,"Maharashtra",(String)talukaSpinner.getText(),selectedAreaDto.getAreaId(),selectedAreaDto.getAreaName());

            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // locationDto = new LocationDto();

//                if(talukaSpinner.getText()==null || talukaSpinner.getText().equals("")){
//                    Toast.makeText(getApplicationContext(),"Please set the location first",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(locationDto==null) {
//                    locationDto = new LocationDto(0.0, 0.0, "Maharashtra", (String) talukaSpinner.getText(), selectedAreaDto.getAreaId(), selectedAreaDto.getAreaName());
//                    if(areaSpinner.getText()==null || areaSpinner.getText().equals("")){
//                        userAddress=(String)talukaSpinner.getText();
//
//                    }else{
//                        userAddress=(String)talukaSpinner.getText()+", "+selectedAreaDto.getAreaName();
//                    }
//                }

                if(locationDto!=null && locationDto.getLongitude()!=null && locationDto.getLongitude()!=null){

                }else{
                    Toast.makeText(getApplicationContext(),"Please check for empty fields",Toast.LENGTH_SHORT).show();
                    return;

                }


              //  Toast.makeText(getApplicationContext(),"=maps dto"+ locationDto.getTaluka(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this, CreatePost.class);
                intent.putExtra("address", userAddress);
                intent.putExtra("LocationDto",locationDto);
                //  Toast.makeText(getApplicationContext(),"locaton dto"+ locationDto.getLatitude(),Toast.LENGTH_SHORT).show();

                setResult(Activity.RESULT_OK,intent);
                finish();


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
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    //    Toast.makeText(this, "Calling enableUserLocation();", Toast.LENGTH_SHORT).show();
        enableUserLocation();
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            currentLocation = new Location(location.getLatitude(),location.getLongitude());
//            currentLocation.setLatitude(location.getLatitude());
//            currentLocation.setLatitude(location.getLongitude());
//            Toast.makeText(this, " lat "+location.getLatitude()+" long "+location.getLongitude() , Toast.LENGTH_SHORT).show();

            if(location==null){
                location=new Location("");
                location.setLatitude(18.4863634);
                location.setLongitude(73.8160079);
 //               Toast.makeText(this, "access null"+userAddress, Toast.LENGTH_SHORT).show();

            }
            locationDto= new LocationDto( location.getLatitude(),location.getLongitude());

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                // Get the address from the current location's latitude and longitude
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                // If the geocoder was successful in finding an address
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    // Use the address object to extract the street, city, state, and zip code
                    String street = address.getAddressLine(0);
                    String city = address.getLocality();
                    String state = address.getAdminArea();
                    String zipCode = address.getPostalCode();
                    String country = address.getCountryName();
                    userAddress = street+", "+city+", "+state+", "+country+", "+zipCode;

                    // Do something with the address information, like display it to the user
                }
 //               Toast.makeText(this, "access"+userAddress, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
  //          Toast.makeText(this, userAddress, Toast.LENGTH_SHORT).show();

    //        Toast.makeText(this, "if set true", Toast.LENGTH_SHORT).show();
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
          //      Toast.makeText(this, "if if", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
                mMap.setMyLocationEnabled(true);
            } else {
            //    Toast.makeText(this, "if else", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
                mMap.setMyLocationEnabled(false);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
        //        Toast.makeText(this, "******", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }

                mMap.setMyLocationEnabled(true);



                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                currentLocation.setLatitude(location.getLatitude());
//                currentLocation.setLatitude(location.getLongitude());
                //         Toast.makeText(this, " lat "+location.getLatitude()+" long "+location.getLongitude() , Toast.LENGTH_SHORT).show();

                if(location==null){
                    location=new Location("");
                    location.setLatitude(18.4863634);
                    location.setLongitude(73.8160079);
                }

                locationDto= new LocationDto( location.getLatitude(),location.getLongitude());

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    // Get the address from the current location's latitude and longitude
                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                    // If the geocoder was successful in finding an address
                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        // Use the address object to extract the street, city, state, and zip code
                        String street = address.getAddressLine(0);
                        String city = address.getLocality();
                        String state = address.getAdminArea();
                        String zipCode = address.getPostalCode();
                        String country = address.getCountryName();
                        userAddress = street+", "+city+", "+state+", "+country+", "+zipCode;

                        // Do something with the address information, like display it to the user
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
     //           Toast.makeText(this, userAddress, Toast.LENGTH_SHORT).show();









            } else {
                //We do not have the permission..
  //              Toast.makeText(this, "in else mai", Toast.LENGTH_SHORT).show();
            }
        }


    }


    private void fetchSuburbs(String taluka) {

        String talukaNametoQuery=  "\"" + taluka + "\"";
 //       Toast.makeText(getApplicationContext(),"in fetch suburbs "+taluka,Toast.LENGTH_SHORT).show();
        RetrofitService rsOverpass = new RetrofitService("taluka");
        OverpassApi service = rsOverpass.getRetrofitOverpass().create(OverpassApi.class);
        // areaNames = new ArrayList<>();
        String data = String.format("[out:json];area[\"name\"=%s]->.searchArea;(node[\"place\"=\"suburb\"](area.searchArea);node[\"place\"=\"village\"](area.searchArea);node[\"place\"=\"city\"](area.searchArea);node[\"place\"=\"town\"](area.searchArea););out;", talukaNametoQuery);

        responseAreaDtos=new ArrayList<AreaDto>();

        Call<OverpassResponse> call = service.getSuburbs(data);
        call.enqueue(new Callback<OverpassResponse>() {
            @Override
            public void onResponse(Call<OverpassResponse> call, Response<OverpassResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OverpassResponse overpassResponse = response.body();
                    List<Element> elements = overpassResponse.getElements();
                    for (Element element : elements) {
                        String suburb = element.getTags().getName();
//                        Toast.makeText(getApplicationContext(),""+element.getId(),Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(),""+element.getLat(),Toast.LENGTH_SHORT).show();
                        if(element.getId()!=null && suburb != null ) {
                            if (element.getId().length() != 0 && suburb.length() != 0) {

                                Double latitude = Double.parseDouble(element.getLat());
                                Double longtitude = Double.parseDouble(element.getLon());

                                AreaDto areaDto = new AreaDto(element.getId(), suburb, taluka);
                                responseAreaDtos.add(areaDto);
                            }
                        }


                    }


                } else {
  //                  Toast.makeText(getApplicationContext(),"null ",Toast.LENGTH_SHORT).show();
                    // Handle the error
                }

                areaProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<OverpassResponse> call, Throwable t) {
                // Handle the error

                Logger.getLogger(OrganizationSignUpDetails.class.getName()).log(Level.SEVERE, "Error occurred" +t);
            }
        });
    }


}