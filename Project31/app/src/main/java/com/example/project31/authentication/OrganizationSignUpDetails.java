package com.example.project31.authentication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project31.Dto.LocationDto;
import com.example.project31.Dto.OrganizationDto;
import com.example.project31.Dto.Overpass.Element;
import com.example.project31.Dto.Overpass.OverpassResponse;
import com.example.project31.R;
import com.example.project31.post.CreatePost;
import com.example.project31.retrofit.OverpassApi;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.SectorApi;
import com.example.project31.retrofit.TalukaApi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationSignUpDetails extends AppCompatActivity {

    Button btnNext;
    EditText etEmail;           // Enter Email
    EditText etMobile;          // Enter Mobile
    EditText etName;           // Enter Email
    Dialog dialog;
    String address;
    TextView etHQLocation;
    OrganizationDto organizationDto;
//    Spinner sectorSpinner;
//    Spinner talukaSpinner;
//    Spinner areaSpinner;
//    SectorApi sectorApi;
//    TalukaApi talukaApi;
//
//    List<String> sectorList ;
//    List<String> talukaList;
//    List<String> areaList;
//    String talukaNametoQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_organization_sign_up_details);

        btnNext = findViewById(R.id.btnNext);
        etName = findViewById(R.id.etName);
        etEmail= findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etHQLocation = ( TextView ) findViewById(R.id.etHQLocation);

//        sectorSpinner =(Spinner) findViewById(R.id.sector_spinner);
//        talukaSpinner =(Spinner) findViewById(R.id.taluka_spinner);
//        areaSpinner =(Spinner) findViewById(R.id.area_spinner);



//        RetrofitService rs1 = new RetrofitService();
//        RetrofitService rs2 = new RetrofitService();

//
//        Call<List<String>> call2 = areaApi.getAreaNames();
//        call2.enqueue(new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call2, Response<List<String>> response) {
//                List<String> areaNames = response.body();
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,areaNames);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                areaSpinner.setAdapter(adapter);
//            }
//
//            @Override
//            public void onFailure(Call<List<String>> call2, Throwable t) {
//                Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Error occurred in area spinner"+t);
//
//            }
//        });
//
//
        etHQLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=new Dialog(OrganizationSignUpDetails.this);

                //dialog.setContentView(R.layout.dialog_hq_location);
                dialog.setContentView(R.layout.dialog_hq_location);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.show();
                EditText officeEditText = dialog.findViewById(R.id.officeEditText);
                EditText localityEditText = dialog.findViewById(R.id.localityEditText);
                EditText talukaEditText = dialog.findViewById(R.id.talukaEditText);
                EditText districtEditText = dialog.findViewById(R.id.districtEditText);
                EditText stateEditText = dialog.findViewById(R.id.stateEditText);
                EditText pincodeEditText = dialog.findViewById(R.id.pincodeEditText);

                Button btnDone = dialog.findViewById(R.id.btnDone);

                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (officeEditText != null && officeEditText.getText().toString().trim().length() > 0 &&
                                localityEditText != null && localityEditText.getText().toString().trim().length() > 0 &&
                                talukaEditText != null && talukaEditText.getText().toString().trim().length() > 0 &&
                                districtEditText != null && districtEditText.getText().toString().trim().length() > 0 &&
                                stateEditText != null && stateEditText.getText().toString().trim().length() > 0 &&
                                pincodeEditText != null && pincodeEditText.getText().toString().trim().length() > 0) {


                        }else{

                            Toast.makeText(getApplicationContext(),"Please check for empty fields",Toast.LENGTH_SHORT).show();
                            return;
                        }


                        address = officeEditText.getText()+ ", "+ localityEditText.getText()+", "+ talukaEditText.getText()+", "+districtEditText.getText()+", "+ stateEditText.getText()+", "+pincodeEditText.getText();
                        etHQLocation.setText(address);
                        dialog.dismiss();
                    }
                });

            }
        });

//        ActivityResultLauncher<Intent> onAcitvityResult= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//
//                Toast.makeText(getApplicationContext(),"previous screen",Toast.LENGTH_SHORT).show();
//            }
//        });




        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (etEmail != null && etEmail.getText().toString().trim().length() > 0 &&
                        etName != null && etName.getText().toString().trim().length() > 0 &&
                        etMobile != null && etMobile.getText().toString().trim().length() > 0 &&
                        etHQLocation != null && etHQLocation.getText().toString().trim().length() > 0) {


                }else{

                    Toast.makeText(getApplicationContext(),"Please check for empty fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(OrganizationSignUpDetails.this, OrganizationSignUpServingDetails.class);
                intent.putExtra("email", etEmail.getText());
                intent.putExtra("name",etName.getText());
                intent.putExtra("mobile",etMobile.getText());
                intent.putExtra("HQLocation",etHQLocation.getText());
                organizationDto= new OrganizationDto();
                organizationDto.setOrganizationId(String.valueOf(etEmail.getText().toString().trim()));
                organizationDto.setName(String.valueOf(etName.getText().toString().trim()));
                organizationDto.setContactNo(String.valueOf(etMobile.getText().toString().trim()));
                LocationDto hqLocation = new LocationDto();
                hqLocation.setLandmark(String.valueOf(etHQLocation.getText().toString().trim()));
                organizationDto.setBase_location(hqLocation);
                intent.putExtra("OrganizationDto",organizationDto);
                startActivity(intent);
            }
        });



    }



}