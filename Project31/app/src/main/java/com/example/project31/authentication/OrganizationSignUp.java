package com.example.project31.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project31.Dto.OrganizationDto;
import com.example.project31.R;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.homePage.HomePage;
import com.example.project31.post.CreatePost;
import com.example.project31.profile.Profile;
import com.example.project31.retrofit.OrganizationApi;
import com.example.project31.retrofit.RetrofitService;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationSignUp extends AppCompatActivity {

    EditText etUsername;        // Enter Username
   // EditText etEmail;           // Enter Email
 //   EditText etMobile;          // Enter Mobile
    EditText etPass;            // Enter Password
    EditText etRepeatPass;      // Repeat Password
    EditText etConfCode;        // Enter Confirmation Code

    Button btnSignUp;           // Sending data to Cognito for registering new user
    Button btnVerify;
    // ############################################################# End View Components

    // ############################################################# Cognito connection
    Cognito authentication;

    private String email;
    private String name;
    private String mobile;
    OrganizationDto organizationDto;
    OrganizationApi organizationApi;

    // ############################################################# End Cognito connection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_organization_sign_up);

        authentication = new Cognito(getApplicationContext());
        initViewComponents();
    }

    private void initViewComponents(){
//        etUsername = findViewById(R.id.etUsername);
//        etEmail= findViewById(R.id.etEmail);
//        etMobile = findViewById(R.id.etMobile);
        etPass = findViewById(R.id.etPass);
        etRepeatPass = findViewById(R.id.etRepeatPass);
        etConfCode = findViewById(R.id.etConfCode);


        btnSignUp = findViewById(R.id.btnSignUp);
        btnVerify = findViewById(R.id.btnVerify);
        RetrofitService rs = new RetrofitService();
        //sector_spinner =(Spinner) findViewById(R.id.sector_spinner);

        organizationApi = rs.getRetrofit().create(OrganizationApi.class);
        Intent intent = this.getIntent();

        if (getIntent().getExtras() != null) {

            if (intent.hasExtra("OrganizationDto")) {
             //   Toast.makeText(getApplicationContext(),"iin intent sign up",Toast.LENGTH_SHORT).show();
                organizationDto= (OrganizationDto) getIntent().getSerializableExtra("OrganizationDto");

            }
        }

        email = organizationDto.getOrganizationId();
        //Bundle bundle = intent.getExtras();
//        email = bundle.getString("email");
//        mobile = bundle.getString("mobile");
//        name = bundle.getString("name");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (
                        etPass != null && etPass.getText().toString().trim().length() > 7 &&
                                etRepeatPass != null && etRepeatPass.getText().toString().trim().length() > 7
                && etPass.getText().toString().equals(etRepeatPass.getText().toString())
                ) {

                }else{

                    Toast.makeText(getApplicationContext(),"Please check if the password follows guidelines",Toast.LENGTH_SHORT).show();
                    return;
                }

                    if(etPass.getText().toString().endsWith(etRepeatPass.getText().toString())){
                    //userId = etUsername.getText().toString().replace(" ", "");
//                    authentication.addAttribute("name", userId);
//                    authentication.addAttribute("phone_number", etMobile.getText().toString().replace(" ", ""));
                     authentication.addAttribute("email", email.toString().replace(" ", ""));
                    email=email.toString().replace(" ", "");
                   authentication.signUpInBackground(email, etPass.getText().toString());
                }
                else{

                }
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (
                        etPass != null && etPass.getText().toString().trim().length() > 7 &&
                                etRepeatPass != null && etRepeatPass.getText().toString().trim().length() > 7
                                && etPass.getText().toString().equals(etRepeatPass.getText().toString())
                ) {

                }else{

                    Toast.makeText(getApplicationContext(),"Please check if the password follows guidelines",Toast.LENGTH_SHORT).show();
                    return;
                }
              //  Toast.makeText(getApplicationContext(),"in verify ",Toast.LENGTH_SHORT).show();
                //comment below line to only add in database
                authentication.confirmUser(email, etConfCode.getText().toString().replace(" ", ""));
                String userEmail= SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();
                if(userEmail==null){

                    Toast.makeText(getApplicationContext(),"Authentication Failure",Toast.LENGTH_SHORT).show();

                }else{
                    SharedPreferencesHelper.getInstance(getApplicationContext()).setOrganization("1");
           //         Toast.makeText(getApplicationContext(),"sectorssss "+ organizationDto.getSectors(),Toast.LENGTH_SHORT).show();
                    organizationApi.writeOrganization(organizationDto)
                            .enqueue(new Callback<OrganizationDto>() {
                                @Override
                                public void onResponse(Call<OrganizationDto> call, Response<OrganizationDto> response) {
                                    //Toast.makeText(MainActivity.this,  "Save Success", Toast.LENGTH_SHORT).show();
                              //      Toast.makeText(getApplicationContext(),"on response",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(OrganizationSignUp.this, HomePage.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<OrganizationDto> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(),  "Save Failed", Toast.LENGTH_SHORT).show();
                                    Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Error occurred",t);
                                    // editTextDescription.setText(t.toString());
                                }
                            });
                    Intent intent = new Intent(OrganizationSignUp.this, HomePage.class);
                    startActivity(intent);

                }


                //finish();
            }
        });

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_organization_sign_up);
//    }
}