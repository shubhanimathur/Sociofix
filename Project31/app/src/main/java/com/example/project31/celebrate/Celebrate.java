package com.example.project31.celebrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project31.Dto.PostDto;
import com.example.project31.Dto.WinnerDto;
import com.example.project31.R;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.drivePage.DrivePage;
import com.example.project31.homePage.HomePage;
import com.example.project31.notification.Notification;
import com.example.project31.post.CreateDrive;
import com.example.project31.post.CreatePost;
import com.example.project31.profile.FollowingUser;
import com.example.project31.profile.Profile;
import com.example.project31.profile.ProfileAll;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.profile.ProfileOrganizationAll;
import com.example.project31.retrofit.PostApi;
import com.example.project31.retrofit.RetrofitService;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Celebrate extends AppCompatActivity {

    Integer tabUser=1,tabOrganization=0;

    TextView rank, number, name, user_id;
    View includedLayout;
    PostApi postApi;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrate);


        RetrofitService rs = new RetrofitService();
        postApi = rs.getRetrofit().create(PostApi.class);

        TabLayout tabLayout = findViewById(R.id.tab_layout);

        TextView home=findViewById(R.id.home);
        TextView drives= findViewById(R.id.drives);
        TextView add_post=findViewById(R.id.add_post);
        TextView notification= findViewById(R.id.notification);
        TextView profile_tab= findViewById(R.id.profile_tab);


        userEmail= SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();
        String userOrOrganization = SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Celebrate.this, HomePage.class);
                startActivity(intent);
            }
        });
        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Celebrate.this, DrivePage.class);
                startActivity(intent);;
            }
        });
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization().equals("0")){
                    Intent intent = new Intent(Celebrate.this, CreatePost.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Celebrate.this, CreateDrive.class);
                    startActivity(intent);
                }
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Celebrate.this, Notification.class);
                startActivity(intent);
            }
        });
        profile_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("to_user_id", userEmail);
                if(userOrOrganization.equals("0")){
                    Intent intent = new Intent(Celebrate.this, Profile.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Celebrate.this, ProfileOrganization.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });


        getDataFromAPI();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Perform action when a tab is selected
                switch (tab.getPosition()) {
                    case 0:
                        tabUser = 1;
                        tabOrganization = 0;
                        for (int i = 1; i <= 10; i++) {
                            int resourceId = getResources().getIdentifier("top_performer" + i, "id", getPackageName());
                            View includedLayout = findViewById(resourceId);
                            includedLayout.setVisibility(View.GONE);
                        }

                        break;
                    case 1:
                        tabUser = 0;
                        tabOrganization = 1;
                        for (int i = 1; i <= 10; i++) {
                            int resourceId = getResources().getIdentifier("top_performer" + i, "id", getPackageName());
                            View includedLayout = findViewById(resourceId);
                            includedLayout.setVisibility(View.GONE);
                        }

                        break;

                }

                getDataFromAPI();
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


        for (int i = 1; i <= 10; i++) {
            int resourceId = getResources().getIdentifier("top_performer" + i, "id", getPackageName());
            View includedLayout = findViewById(resourceId);
            includedLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView userIdTextView = view.findViewById(R.id.user_id);
                    String userId = userIdTextView.getText().toString();
                    onTopPerformerClick(userId);
                    Toast.makeText(Celebrate.this, "Top performer clicked!", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    public void onTopPerformerClick( String user_id) {




        Bundle bundle = new Bundle();
        bundle.putString("to_user_id", user_id);

        Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "u selecetdddddddddddddddddddd" + name.toString());


        if(tabUser==1){





            if(SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail().equals(user_id)){
                Intent intent = new Intent(Celebrate.this, Profile.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                Intent intent = new Intent(Celebrate.this, ProfileAll.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        }else{


            if(SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail().equals(user_id)){
                Intent intent = new Intent(Celebrate.this, ProfileOrganization.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                Intent intent = new Intent(Celebrate.this, ProfileOrganizationAll.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }



        }



    }


    private void getDataFromAPI() {




        Call<List<WinnerDto>> call=null;

        if(tabUser==1){
//            call=  postApi.getPostbyMyLocalityMostRecent(locationDto,user_id,page,size);
            call =  postApi.getTopTenWinnerUsers();
        }else {

//                call = postApi.getPostbyDefaultMostPopular(user_id, page, size);
            call = postApi.getTop10OrganizationsWithSolvedPosts();
            }




        call.enqueue(new Callback<List<WinnerDto>>() {
            @Override
            public void onResponse(Call<List<WinnerDto>> call, Response<List<WinnerDto>> response) {
                List<WinnerDto> tempWinnerDto = response.body();
                int i=1;
                String toAccess="";
                if(tempWinnerDto==null){
                    Toast.makeText(getApplicationContext(), "Seems like there hasn't been a lot of activity in the past month", Toast.LENGTH_SHORT).show();
                    return;
                }
               for(WinnerDto performer: tempWinnerDto){
                   toAccess="top_performer"+i;
                   int resId = getResources().getIdentifier(toAccess, "id", getPackageName());
                   includedLayout = findViewById(resId);
                   includedLayout.setVisibility(View.VISIBLE);
                    name = includedLayout.findViewById(R.id.name);
                    user_id=includedLayout.findViewById(R.id.user_id);
                    rank=includedLayout.findViewById(R.id.rank);
                    number = includedLayout.findViewById(R.id.number);

                    name.setText(performer.getName());
                    user_id.setText(performer.getId());
                    rank.setText(""+i);
                    number.setText(""+performer.getNumberOfPosts());
                    i++;

               }
            }

            @Override
            public void onFailure(Call<List<WinnerDto>> call, Throwable t) {
                Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "Error occurred in spinner",t);
               // Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

            }
        });

        // creating a variable for our json object request and passing our url to it.



    }

}