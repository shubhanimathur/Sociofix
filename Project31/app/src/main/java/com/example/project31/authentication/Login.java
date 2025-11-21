package com.example.project31.authentication;
//
//import android.content.Intent;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.project31.MainActivity;
//import com.example.project31.R;
//
//public class Login extends AppCompatActivity{
//    // ############################################################# View Components
//    TextView txtNotAccount;     // For creating account
//    //TextView txtForgetPass;     // For retrieving password
//    Button btnLogin;            // Button for Login
//    EditText etUsername;
//    EditText etPassword;
//    // ############################################################# End View Components
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        setTitle("Login");
//        initViewComponents();
//    }
//
//    private void initViewComponents(){
//        txtNotAccount = findViewById(R.id.txtNotAccount);
//        //txtForgetPass= findViewById(R.id.txtForgetPass);
//        btnLogin = findViewById(R.id.btnLogin);
//        etUsername = findViewById(R.id.etUsername);
//        etPassword = findViewById(R.id.etPassword);
//
//        txtNotAccount.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Login.this, MainActivity.class));
//            }
//        });
//
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Cognito authentication = new Cognito(getApplicationContext());
//                authentication.userLogin(etUsername.getText().toString().replace(" ", ""), etPassword.getText().toString());
//            }
//        });
//    }
//}
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.project31.Dto.UserDto;
import com.example.project31.MainActivity;
import com.example.project31.R;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.homePage.HomePage;
import com.example.project31.profile.Profile;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.UserApi;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity{
    // ############################################################# View Components
    //TextView txtNotAccount;     // For creating account
    //TextView txtForgetPass;     // For retrieving password
    Button btnLogin;            // Button for Login
    EditText etEmailAddress;
    EditText etPassword;
    TextView signup_individual_link,signup_organization_link;
    UserApi userApi;
    Call<UserDto> callGetUser=null;
    UserDto userDto;
    // ############################################################# End View Components
    RetrofitService rs = new RetrofitService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        userApi = rs.getRetrofit().create(UserApi .class);
        initViewComponents();
    }

    private void initViewComponents(){
        btnLogin= findViewById(R.id.btnLogin);
//        txtNotAccount = findViewById(R.id.txtNotAccount);
//        //txtForgetPass= findViewById(R.id.txtForgetPass);
//        btnLogin = findViewById(R.id.btnLogin);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        signup_individual_link= findViewById(R.id.signup_individual_link);
        signup_organization_link=findViewById(R.id.signup_organization_link);

//
//        txtNotAccount.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Login.this, Signup.class));
//            }
//        });

        signup_individual_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, IndividualSignUp.class);
                intent.putExtra("origin", "Login");
                startActivity(intent);
            }
        });
        signup_organization_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, OrganizationSignUpDetails.class);
                intent.putExtra("origin", "Login");
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesHelper.getInstance(getApplicationContext()).removeUser();
                Cognito authentication = new Cognito(getApplicationContext());
                Logger.getLogger(Login.class.getName()).log(Level.INFO, "login",etEmailAddress.getText().toString()+ " "+ etPassword.getText().toString());
              //  Toast.makeText(getApplicationContext(),  "loginnn"+" "+ etEmailAddress.getText().toString()+ " "+ etPassword.getText().toString(),Toast.LENGTH_SHORT).show();
                authentication.userLogin(etEmailAddress.getText().toString().replace(" ", ""), etPassword.getText().toString());
//                SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("yutipat");
                String userEmail= SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();
                if(userEmail==null){

                    Toast.makeText(getApplicationContext(),"Authentication Failure",Toast.LENGTH_SHORT).show();

                }else{
                    callGetUser = userApi.getUser(userEmail);



                    callGetUser.enqueue(new Callback<UserDto>() {
                        @Override
                        public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                            userDto = response.body();
                            if(userDto.getUserOrOrganization().equals("User")) {
                                SharedPreferencesHelper.getInstance(getApplicationContext()).setOrganization("0");
                                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, " I am a user ");
                            }
                            else {
                                SharedPreferencesHelper.getInstance(getApplicationContext()).setOrganization("1");
                                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, " I am an organization ");
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDto> call, Throwable t) {
                            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "Error occurred in user data fetch",t);
 //                           Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

                        }
                    });

                    Intent intent = new Intent(Login.this, HomePage.class);
                    startActivity(intent);

                }
            //    Toast.makeText(getApplicationContext(),  "SP"+" "+ userEmail,Toast.LENGTH_LONG).show();



            }
        });
    }
}
