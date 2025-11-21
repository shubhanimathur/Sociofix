package com.example.project31.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project31.Dto.NotificationDisplayDto;
import com.example.project31.Dto.PostDto;
import com.example.project31.R;
import com.example.project31.Utils.NotificationAdapter;
import com.example.project31.Utils.PostAdapter;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.homePage.HomePage;
import com.example.project31.post.CreateDrive;
import com.example.project31.post.CreatePost;
import com.example.project31.profile.Profile;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.UserApi;
import com.google.android.material.tabs.TabLayout;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class Notification extends AppCompatActivity {

    UserApi userApi;
    String user_id;

    // List<DriveDto> driveDtos= new ArrayList<DriveDto>();
    Integer page=0,size=11,complete=0,tabHelp=1,tabActivity=0;
    Call<List<NotificationDisplayDto>> call ;
    List<NotificationDisplayDto> notificationDisplayDtos= new ArrayList<NotificationDisplayDto>();

    TextView sectorSpinner,areaSpinner,talukaSpinner,clearSector,clearLocation;

    String currentTalukaSelected , userOrOrganization ;
    private NotificationAdapter notificationAdapter;
    private RecyclerView notificationRV;

    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        setContentView(R.layout.activity_notification);

        // creating a new array list.
        //userModalArrayList = new ArrayList<>();

        // initializing our views.
        notificationRV = findViewById(R.id.idRVNotification);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);


        TextView home=findViewById(R.id.home);
        TextView drives= findViewById(R.id.drives);
        TextView add_post=findViewById(R.id.add_post);
        TextView notification= findViewById(R.id.notification);
        TextView profile_tab= findViewById(R.id.profile_tab);

       // Toast.makeText(getApplicationContext(),  "in home page",Toast.LENGTH_SHORT).show();

         userOrOrganization = SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization();

        Bundle bundle = getIntent().getExtras();



        user_id = SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();




        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, HomePage.class);
                startActivity(intent);
            }
        });
        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, com.example.project31.drivePage.DrivePage.class);
                startActivity(intent);;
            }
        });
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization().equals("0")){
                    Logger.getLogger(com.example.project31.drivePage.DrivePage.class.getName()).log(Level.SEVERE, " home I am an individual ");
                    Intent intent = new Intent(Notification.this, CreatePost.class);
                    startActivity(intent);
                }else{
                    Logger.getLogger(com.example.project31.drivePage.DrivePage.class.getName()).log(Level.SEVERE, " home I am an organization ");
                    Intent intent = new Intent(Notification.this, CreateDrive.class);
                    startActivity(intent);
                }


            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification.this, Notification.class);
                startActivity(intent);
            }
        });
        profile_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("to_user_id", user_id);
                if(userOrOrganization.equals("0")){
                    Intent intent = new Intent(Notification.this, Profile.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Notification.this, ProfileOrganization.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }


            }
        });

        RetrofitService rs = new RetrofitService();
        userApi = rs.getRetrofit().create(UserApi.class);



        getDataFromAPI(page, size);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Perform action when a tab is selected
                switch (tab.getPosition()) {
                    case 0:
                        tabHelp=1;
                        tabActivity=0;


                        break;
                    case 1:
                        // Handle tab 2 selection
                        tabHelp=0;
                        tabActivity=1;


                        break;

                }
                notificationDisplayDtos= new ArrayList<NotificationDisplayDto>();
                notificationAdapter = new NotificationAdapter(notificationDisplayDtos, Notification.this,tabHelp);
                notificationRV.setLayoutManager(new LinearLayoutManager(Notification.this));
                notificationRV.setAdapter(notificationAdapter);
                page=0;
                complete=0;
                nestedSV.smoothScrollTo(0, 0);
                getDataFromAPI(page,size);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Perform action when a tab is unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Perform action when a tab is reselected
            }
        });

        // adding on scroll change listener method for our nested scroll view.
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    loadingPB.setVisibility(View.VISIBLE);
                    getDataFromAPI(page, size);
                }
            }
        });






    }

    private void getDataFromAPI(int page, int size) {



        if( userOrOrganization.equals("0") ){

            if(tabHelp==1){

                call = userApi.getNotificationsUserHelp(user_id, page, size);
            }else{
                call = userApi.getNotificationsUserActivity(user_id, page, size);
            }

        }else{

            if(tabHelp==1){
                call = userApi.getNotificationsOrganizationHelp(user_id, page, size);
            }else{
                call = userApi.getNotificationsOrganizationActivity(user_id, page, size);
            }


        }

        call.enqueue(new Callback<List<NotificationDisplayDto>>() {
            @Override
            public void onResponse(Call<List<NotificationDisplayDto>> call, Response<List<NotificationDisplayDto>> response) {
                List<NotificationDisplayDto> tempNotificationDisplayDtos = response.body();
                Logger.getLogger(Notification.class.getName()).log(Level.SEVERE, "helloo ");
                Logger.getLogger(Notification.class.getName()).log(Level.SEVERE, "response "+tempNotificationDisplayDtos.size());
                if(tempNotificationDisplayDtos.size()==0 ){
                    Toast.makeText(getApplicationContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
                    loadingPB.setVisibility(View.GONE);
                    complete=1;
                }else {
                    Logger.getLogger(Notification.class.getName()).log(Level.SEVERE, "I am in ellse ");
                    notificationDisplayDtos.addAll(tempNotificationDisplayDtos);
                    notificationAdapter = new NotificationAdapter(notificationDisplayDtos, Notification.this,tabHelp);

                    notificationRV.setLayoutManager(new LinearLayoutManager(Notification.this));

                    // setting adapter to our recycler view.
                    notificationRV.setAdapter(notificationAdapter);

                    // Toast.makeText(getApplicationContext(),"drive 1"+notificationDisplayDtos.get(0).getDescription(),Toast.LENGTH_SHORT).show();

                    if(tempNotificationDisplayDtos.size()<size){
                        Toast.makeText(getApplicationContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
                        loadingPB.setVisibility(View.GONE);
                        complete=1;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NotificationDisplayDto>> call, Throwable t) {
                Logger.getLogger(Notification.class.getName()).log(Level.SEVERE, "Error occurred in spinner",t);
              //  Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

            }
        });


    }





}

