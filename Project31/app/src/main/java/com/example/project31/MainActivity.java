package com.example.project31;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.Toast;

import com.example.project31.Dto.FilterDto;
import com.example.project31.Dto.UserDto;
import com.example.project31.Utils.Constant;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.authentication.Login;
import com.example.project31.authentication.OrganizationSignUpDetails;
import com.example.project31.homePage.HomePage;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.TextFilterApi;
import com.example.project31.retrofit.UserApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button createPost;
    private Button login;
    private Button individualSignUp;
    private Button orgSignUp;
    private Button logout;
    private Button homePage;
    UserApi userApi;
    Call<UserDto> callGetUser=null;
    Call<List<String>> callCensoringState;
    UserDto userDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RetrofitService rs = new RetrofitService();
        userApi = rs.getRetrofit().create(UserApi.class);



//rutujaudhane478
        // SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("rutujaudhane478@gmail.com");
     //SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("rutuja.udhane@cumminscollege.in");
      //    SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("yutika.patel@cumminscollege.in");
        //SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("rutujakeyacc@gmail.com");
      //  SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("split.it.easy@gmail.com");
//        SharedPreferencesHelper.getInstance(getApplicationContext()).setOrganization("1");
        //  SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("preetipatel2dec@gmail.com");
       // SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("seemapatel2dec@gmail.com");


        // SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("rutujaudhane478@gmail.com");
//        SharedPreferencesHelper.getInstance(getApplicationContext()).setOrganization("1");
     // SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("preetipatel2dec@gmail.com");
 //SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("yutikapatel6june@gmail.com");
     //SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("igniteandroll@gmail.com");

       //     SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("yutikapat6@gmail.com");
//        Intent intent = new Intent(MainActivity.this, OrganizationSignUpDetails.class);
//        startActivity(intent);

        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {


            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {






                String userEmail= SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();

                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, " user email "+userEmail);
              //  Toast.makeText(getApplicationContext(),  "Main activity"+" "+ userEmail,Toast.LENGTH_LONG).show();

                callCensoringState= userApi.censoringState(Constant.censoring);
                callCensoringState.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        Constant.censoring = response.body().get(0);
                        if( Constant.censoring.equals("yes")) {

                            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, " Censoring on");
                           // Toast.makeText(getApplicationContext(),  "Censoring on ",Toast.LENGTH_SHORT).show();
                        }
                        else {

                           // Toast.makeText(getApplicationContext(),  "Censoring off ",Toast.LENGTH_SHORT).show();
                            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, " Censoring off ");
                        }

                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "Error occurred in censoring setting",t);
                        //Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

                    }
                });

                if(userEmail==null){
                  // Toast.makeText(getApplicationContext(),  "in null",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);

                }else {






                    callGetUser = userApi.getUser(userEmail);



                    callGetUser.enqueue(new Callback<UserDto>() {
                        @Override
                        public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                            userDto = response.body();
                           if(userDto.getUserOrOrganization().equals("User")) {
                               SharedPreferencesHelper.getInstance(getApplicationContext()).setOrganization("0");
                               Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, " I am a user ");
                        //       Toast.makeText(getApplicationContext(),  "I am a user ",Toast.LENGTH_SHORT).show();
                           }
                           else {
                               SharedPreferencesHelper.getInstance(getApplicationContext()).setOrganization("1");
                           //    Toast.makeText(getApplicationContext(),  "I am an organization ",Toast.LENGTH_SHORT).show();
                               Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, " I am an organization ");
                           }

                         //   Toast.makeText(getApplicationContext(),  "in not null",Toast.LENGTH_SHORT).show();
                            String organization = SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization();
                            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "Kon aahe "+organization);
                            //Intent intent = new Intent(MainActivity.this, IndividualSignUp.class);
                            Intent intent = new Intent(MainActivity.this, HomePage.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<UserDto> call, Throwable t) {
                            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "Error occurred in user data fetch",t);
                         //   Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

                        }
                    });






                }





            }
        }.start();

//        SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("yutikapatel6june@gmail.com");
//        SharedPreferencesHelper.getInstance(getApplicationContext()).setOrganization("0");

//        createPost = findViewById(R.id.createPost);
//        login= findViewById(R.id.login);
//        individualSignUp= findViewById(R.id.individualSignUp);
//        orgSignUp= findViewById(R.id.orgSignUp);
//        logout= findViewById(R.id.logout);
//        homePage= findViewById(R.id.homePage);





      //  SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("yutikapatel6june@gmail.com");
       // SharedPreferencesHelper.getInstance(getApplicationContext()).setUserEmail("rutuja.udhane@cummincollege.in");
//        createPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, CreatePost.class);
//                intent.putExtra("origin", "MainActitvity");
//                startActivity(intent);
//            }
//        });
//
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, Login.class);
//                intent.putExtra("origin", "MainActitvity");
//                startActivity(intent);
//            }
//        });
//
//        individualSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, IndividualSignUp.class);
//                intent.putExtra("origin", "MainActitvity");
//                startActivity(intent);
//            }
//        });
//
//        orgSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, OrganizationSignUpDetails.class);
//                intent.putExtra("origin", "MainActitvity");
//                startActivity(intent);
//            }
//        });
//        homePage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, HomePage.class);
//                startActivity(intent);
//
//            }
//        });
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Cognito authentication = new Cognito(getApplicationContext());
//                authentication.logout();
//                SharedPreferencesHelper.getInstance(getApplicationContext()).removeUser();
//
//            }
//        });


    }

    @Override
    public void onRestart() {
        super.onRestart();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();

    }

}