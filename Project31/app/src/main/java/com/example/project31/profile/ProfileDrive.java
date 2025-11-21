package com.example.project31.profile;

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

import com.example.project31.Dto.DriveDto;
import com.example.project31.Dto.UserDto;
import com.example.project31.R;
import com.example.project31.Utils.DriveAdapter;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.drivePage.DrivePage;
import com.example.project31.homePage.HomePage;
import com.example.project31.notification.Notification;
import com.example.project31.post.CreateDrive;
import com.example.project31.post.CreatePost;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.UserApi;
import com.example.project31.settings.Settings;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDrive extends AppCompatActivity {


    UserApi userApi;
    List<DriveDto> driveDtos = new ArrayList<DriveDto>();
    Integer page = 0, size = 6, complete = 0, tabDrives = 1, tabUpvoted = 0, tabSaved = 0;
    private DriveAdapter driveAdapter;
    private RecyclerView driveRV;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    TabLayout tabLayout;
    UserDto userDto;
    private TextView settings, postsNo, orgNo, volunteerNo, name, userId, bio, volunteerTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_drive);

        settings = findViewById(R.id.settings);
        driveRV = findViewById(R.id.idRVDrives);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);


        RetrofitService rs = new RetrofitService();
        userApi = rs.getRetrofit().create(UserApi.class);

        RetrofitService rs1 = new RetrofitService();


        TextView home = findViewById(R.id.home);
        TextView drives = findViewById(R.id.drives);
        TextView add_post = findViewById(R.id.add_post);
        TextView notification = findViewById(R.id.notification);
        TextView profile_tab = findViewById(R.id.profile_tab);
        Call<UserDto> callGetUser = null;

        String user_id = SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileDrive.this, HomePage.class);
                startActivity(intent);
            }
        });
        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileDrive.this, DrivePage.class);
                startActivity(intent);
                ;
            }
        });
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization().equals("0")) {
                    Intent intent = new Intent(ProfileDrive.this, CreatePost.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ProfileDrive.this, CreateDrive.class);
                    startActivity(intent);
                }
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileDrive.this, Notification.class);
                startActivity(intent);
            }
        });
        profile_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
//                bundle.putString("to_user_id", userEmail);
                if(SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail().equals("0")){
                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), ProfileOrganization.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });


        getDataFromAPI(page, size, user_id);
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    loadingPB.setVisibility(View.VISIBLE);
                    getDataFromAPI(page, size, user_id);
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileDrive.this, Settings.class);
                startActivity(intent);

            }
        });
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Perform action when a tab is selected
                switch (tab.getPosition()) {
                    case 0:
                        tabDrives = 1;
                        tabUpvoted = 0;
                        tabSaved = 0;

                        break;
                    case 1:
                        // Handle tab 2 selection
                        tabDrives = 0;
                        tabUpvoted = 1;
                        tabSaved = 0;

                        break;
                    case 2:
                        // Handle tab 3 selection
                        tabDrives = 0;
                        tabUpvoted = 0;
                        tabSaved = 1;

                        break;
                }
                driveDtos = new ArrayList<DriveDto>();
                driveAdapter = new DriveAdapter(driveDtos, ProfileDrive.this);
                driveRV.setLayoutManager(new LinearLayoutManager(ProfileDrive.this));
                driveRV.setAdapter(driveAdapter);
                page = 0;
                complete = 0;
                nestedSV.smoothScrollTo(0, 0);
                getDataFromAPI(page, size, user_id);
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

    }
        private void getDataFromAPI( int page, int size, String user_id){


            if (complete == 1) {

                Toast.makeText(this, "All the data loaded already", Toast.LENGTH_SHORT).show();
                loadingPB.setVisibility(View.GONE);
                return;
            }

            Call<List<DriveDto>> call = null;
            if (tabDrives == 1) {
                Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "tab 1 ");

                call = userApi.getUserVolunteeredDrives(user_id, SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail(),page,size);


            } else if ((tabUpvoted == 1)) {
                Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "tab 2 ");
                call = userApi.getUserUpvotedDrives(user_id, page, size);

            } else if (tabSaved == 1) {
                Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "tab 3 ");
                call = userApi.getUserSavedDrives(user_id, page, size);

            } else {
                Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "last else no tab seleted");

            }


            call.enqueue(new Callback<List<DriveDto>>() {
                @Override
                public void onResponse(Call<List<DriveDto>> call, Response<List<DriveDto>> response) {
                    List<DriveDto> tempDriveDto = response.body();

                    Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "response " + tempDriveDto.size());
                    if (tempDriveDto.size() == 0) {
                        Toast.makeText(getApplicationContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
                        loadingPB.setVisibility(View.GONE);
                        complete = 1;
                    } else {
                        Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "I am in ellse ");
                        driveDtos.addAll(tempDriveDto);
                        driveAdapter = new DriveAdapter(driveDtos, ProfileDrive.this);
                        driveRV.setLayoutManager(new LinearLayoutManager(ProfileDrive.this));

                        // setting adapter to our recycler view.
                        driveRV.setAdapter(driveAdapter);

                        // Toast.makeText(getApplicationContext(),"post 1"+postDtos.get(0).getDescription(),Toast.LENGTH_SHORT).show();

                        if (tempDriveDto.size() < size) {
                            Toast.makeText(getApplicationContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
                            loadingPB.setVisibility(View.GONE);
                            complete = 1;
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<DriveDto>> call, Throwable t) {
                    Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "Error occurred in spinner", t);
                 //   Toast.makeText(getApplicationContext(), "on error", Toast.LENGTH_SHORT).show();

                }
            });


        }


    }
