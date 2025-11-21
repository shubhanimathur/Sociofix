package com.example.project31.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project31.Dto.PostDto;
import com.example.project31.Dto.UserDto;
import com.example.project31.R;
import com.example.project31.Utils.Constant;
import com.example.project31.Utils.PostAdapter;
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

public class Profile extends AppCompatActivity {


    UserApi userApi;
    List<PostDto> postDtos= new ArrayList<PostDto>();
    Integer page=0,size=6,complete=0,tabPosts=1,tabUpvoted=0,tabSaved=0;
    private PostAdapter postAdapter;
    private RecyclerView postRV;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;

    private ImageView profile;
    TabLayout tabLayout;
    UserDto userDto;
    String imagePath="https://demo4-s3.s3.us-east-2.amazonaws.com/";
    private TextView settings,postsNo,orgNo,volunteerNo,name,userId,bio,volunteerTitle,organizationTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_profile);

        settings= findViewById(R.id.settings);
        postRV = findViewById(R.id.idRVPosts);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);

        postsNo=findViewById(R.id.postsNo);
        orgNo= findViewById(R.id.orgNo);
        organizationTitle= findViewById(R.id.organizationTitle);
        volunteerNo= findViewById(R.id.volunteerNo);
        volunteerTitle= findViewById(R.id.volunteerTitle);
        name= findViewById(R.id.name);
        userId= findViewById(R.id.userId);
        bio= findViewById(R.id.bio);
        profile=findViewById(R.id.profile);
        tabLayout = findViewById(R.id.tab_layout);



        RetrofitService rs = new RetrofitService();
        userApi = rs.getRetrofit().create(UserApi.class);

        RetrofitService rs1 = new RetrofitService();


        TextView home=findViewById(R.id.home);
        TextView drives= findViewById(R.id.drives);
        TextView add_post=findViewById(R.id.add_post);
        TextView notification= findViewById(R.id.notification);
        TextView profile_tab = findViewById(R.id.profile_tab);
        Call<UserDto> callGetUser=null;

        String user_id = SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();






        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, HomePage.class);
                startActivity(intent);
            }
        });
        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, DrivePage.class);
                startActivity(intent);;
            }
        });
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization().equals("0")){
                    Intent intent = new Intent(Profile.this, CreatePost.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Profile.this, CreateDrive.class);
                    startActivity(intent);
                }
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Notification.class);
                startActivity(intent);
            }
        });
        profile_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
//                bundle.putString("to_user_id", userEmail);
                if(SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization().equals("0")){
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

        Intent intent = getIntent();
        String to="";
        Bundle bundle ;
        TabLayout.Tab tabToSelect;
        if (intent.hasExtra("from_settings")) {

            bundle = getIntent().getExtras();
            to = bundle.getString("from_settings", "Default");
            if(to.equals("posts"))
                tabToSelect = tabLayout.getTabAt(0);
            else if(to.equals("upvoted_posts"))
                tabToSelect = tabLayout.getTabAt(1);
            else
                tabToSelect = tabLayout.getTabAt(2);
            tabToSelect.select();
            nestedSV.smoothScrollTo(0, 0);
        }






        callGetUser = userApi.getUser(user_id);



        callGetUser.enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                userDto = response.body();
                postsNo.setText(userDto.getPostsNo().toString());
                orgNo.setText(userDto.getFollowingOrgNo().toString());
                volunteerNo.setText(userDto.getVolunteerNo().toString());
                name.setText(userDto.getName());
                userId.setText(userDto.getUserId());
                bio.setText(userDto.getBio());

                if(Constant.serverImages==1 && userDto.getProfileImg()!=null) {
                    Glide.with(getApplicationContext()).load(imagePath+userDto.getProfileImg()).into(profile);

                }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "Error occurred in user data fetch",t);
             //   Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

            }
        });






        getDataFromAPI(page, size,user_id);
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    loadingPB.setVisibility(View.VISIBLE);
                    getDataFromAPI(page, size,user_id);
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Profile.this, Settings.class);
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
                        tabPosts=1;
                        tabUpvoted=0;
                        tabSaved=0;

                        break;
                    case 1:
                        // Handle tab 2 selection
                        tabPosts=0;
                        tabUpvoted=1;
                        tabSaved=0;

                        break;
                    case 2:
                        // Handle tab 3 selection
                        tabPosts=0;
                        tabUpvoted=0;
                        tabSaved=1;

                        break;
                }
                postDtos= new ArrayList<PostDto>();
                postAdapter = new PostAdapter(postDtos, Profile.this);
                postRV.setLayoutManager(new LinearLayoutManager(Profile.this));
                postRV.setAdapter(postAdapter);
                page=0;
                complete=0;
                nestedSV.smoothScrollTo(0, 0);
                getDataFromAPI(page,size,user_id);
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

        volunteerTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, ProfileDrive.class);
                startActivity(intent);


            }
        });
        volunteerNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Profile.this, ProfileDrive.class);
                startActivity(intent);

            }
        });

        postsNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                nestedSV.smoothScrollTo(0, 50);
            }
        });
        orgNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Profile.this, FollowingUser.class);
                Bundle bundle = new Bundle();
                bundle.putString("to_user_id", user_id);
                bundle.putString("to_isOrganization", "0");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        organizationTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Profile.this, FollowingUser.class);
                Bundle bundle = new Bundle();
                bundle.putString("to_user_id", user_id);
                bundle.putString("to_isOrganization", "0");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



    }


    private void getDataFromAPI(int page, int size, String user_id) {


        if(complete==1){

            Toast.makeText(this, "All the data loaded already", Toast.LENGTH_SHORT).show();
            loadingPB.setVisibility(View.GONE);
            return;
        }

        Call<List<PostDto>> call=null;
        if(tabPosts==1){
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "tab 1 ");

            call = userApi.getUserPosts(user_id,user_id,page,size);


        }else if((tabUpvoted==1)){
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "tab 2 ");
            call = userApi.getUserUpvotedPosts(user_id,page,size);

        }else if(tabSaved==1){
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "tab 3 ");
            call = userApi.getUserSavedPosts(user_id,page,size);

        }else{
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "last else no tab seleted");

        }


        call.enqueue(new Callback<List<PostDto>>() {
            @Override
            public void onResponse(Call<List<PostDto>> call, Response<List<PostDto>> response) {
                List<PostDto> tempPostDtos = response.body();

                Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "response "+tempPostDtos.size());
                if(tempPostDtos.size()==0 ){
                    Toast.makeText(getApplicationContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
                    loadingPB.setVisibility(View.GONE);
                    complete=1;
                }else {
                    Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "I am in ellse ");
                    postDtos.addAll(tempPostDtos);
                    postAdapter = new PostAdapter(postDtos, Profile.this);
                    postRV.setLayoutManager(new LinearLayoutManager(Profile.this));

                    // setting adapter to our recycler view.
                    postRV.setAdapter(postAdapter);

                    // Toast.makeText(getApplicationContext(),"post 1"+postDtos.get(0).getDescription(),Toast.LENGTH_SHORT).show();

                    if(tempPostDtos.size()<size){
                        Toast.makeText(getApplicationContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
                        loadingPB.setVisibility(View.GONE);
                        complete=1;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PostDto>> call, Throwable t) {
                Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "Error occurred in spinner",t);
           //     Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

            }
        });



    }

    public void onRestart() {
        super.onRestart();
        Intent intent = new Intent(Profile.this, Profile.class);
        startActivity(intent);


    }


}