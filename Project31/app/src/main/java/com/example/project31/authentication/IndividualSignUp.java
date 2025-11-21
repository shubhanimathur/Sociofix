package com.example.project31.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project31.Dto.OrganizationDto;
import com.example.project31.Dto.UserDto;
import com.example.project31.R;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.homePage.HomePage;
import com.example.project31.post.CreatePost;
import com.example.project31.retrofit.OrganizationApi;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.UserApi;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndividualSignUp extends AppCompatActivity {
    EditText etUsername;        // Enter Username
    EditText etEmail,etName;           // Enter Email
    EditText etMobile;          // Enter Mobile
    EditText etPass;            // Enter Password
    EditText etRepeatPass;      // Repeat Password
    EditText etConfCode;        // Enter Confirmation Code

    Button btnSignUp;           // Sending data to Cognito for registering new user
    Button btnVerify;
    // ############################################################# End View Components

    // ############################################################# Cognito connection
    Cognito authentication;
    private String userId;
    private String email;
    private String name;
    // ############################################################# End Cognito connection
    UserDto userDto;
    UserApi userApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_individual_sign_up);

        authentication = new Cognito(getApplicationContext());
        AlertDialog alert11 = new AlertDialog.Builder(this).setMessage(Html.fromHtml("<i><font color='#808080' size='1'><br>Please create a strong password containing <br><br>1. Atleast 7 characters <br><br> 2. Atleast 1 uppercase<br><br> 3. Atleast 1 special symbol [@,* ,.,etc]<br><br>4. Atleast one number</font></i>")).show();
        TextView textView = (TextView) alert11.findViewById(android.R.id.message);
        textView.setTextSize(11);
        textView.setGravity(Gravity.CENTER);
        alert11.show();

        initViewComponents();
    }

    private void initViewComponents(){
//        etUsername = findViewById(R.id.etUsername);
        etEmail= findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etPass = findViewById(R.id.etPass);
        etRepeatPass = findViewById(R.id.etRepeatPass);
        etConfCode = findViewById(R.id.etConfCode);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnVerify = findViewById(R.id.btnVerify);






        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (etEmail != null && etEmail.getText().toString().trim().length() > 0 &&
                        etName != null && etName.getText().toString().trim().length() > 0 &&
                        etMobile != null && etMobile.getText().toString().trim().length() > 0 &&
                        etPass != null && etPass.getText().toString().trim().length() > 7 &&
                        etRepeatPass != null && etRepeatPass.getText().toString().trim().length() > 7 &&
                        etPass.getText().toString().equals(etRepeatPass.getText().toString())) {

                }else{
                    Toast.makeText(getApplicationContext(),"Please check for empty fields and if the password follows app guidelines",Toast.LENGTH_SHORT).show();
                    return;

                }


                if(etPass.getText().toString().endsWith(etRepeatPass.getText().toString())){
                   // userId = etUsername.getText().toString().replace(" ", "");
//                    authentication.addAttribute("name", userId);
//                    authentication.addAttribute("phone_number", etMobile.getText().toString().replace(" ", ""));
                    authentication.addAttribute("email", etEmail.getText().toString().replace(" ", ""));
                    email=etEmail.getText().toString().replace(" ", "");
                    authentication.signUpInBackground(email, etPass.getText().toString());
                }
                else{

                }
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (etEmail != null && etEmail.getText().toString().trim().length() > 0 &&
                        etName != null && etName.getText().toString().trim().length() > 0 &&
                        etMobile != null && etMobile.getText().toString().trim().length() > 0 &&
                        etPass != null && etPass.getText().toString().trim().length() > 7 &&
                        etRepeatPass != null && etRepeatPass.getText().toString().trim().length() > 7 &&
                        etPass.getText().toString().equals(etRepeatPass.getText().toString())) {

                }else{
                    Toast.makeText(getApplicationContext(),"Please check for empty fields and if the password follows app guidelines",Toast.LENGTH_SHORT).show();
                    return;

                }


                authentication.confirmUser(email, etConfCode.getText().toString().replace(" ", ""));
                RetrofitService rs = new RetrofitService();
                userApi = rs.getRetrofit().create(UserApi.class);


                String userEmail= SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();
                if(userEmail==null){

                    Toast.makeText(getApplicationContext(),"Authentication failure ",Toast.LENGTH_SHORT).show();

                }else{
                  //  Toast.makeText(getApplicationContext(),"userr ",Toast.LENGTH_SHORT).show();
                    SharedPreferencesHelper.getInstance(getApplicationContext()).setOrganization("0");
                    userApi.writeUser(new UserDto(email,etName.getText().toString(),etMobile.getText().toString()))
                            .enqueue(new Callback<UserDto>() {
                                @Override
                                public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                                    //Toast.makeText(MainActivity.this,  "Save Success", Toast.LENGTH_SHORT).show();
                                   // Toast.makeText(getApplicationContext(),"on response",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<UserDto> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(),  "Save Failed", Toast.LENGTH_SHORT).show();
                                    Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Error occurred",t);
                                    // editTextDescription.setText(t.toString());
                                }
                            });

                    Intent intent = new Intent(IndividualSignUp.this, HomePage.class);
                    startActivity(intent);


                }
                //finish();
            }
        });

    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_individual_sign_up);
//    }
}