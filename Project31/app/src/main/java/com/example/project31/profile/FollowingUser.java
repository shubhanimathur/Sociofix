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

import com.example.project31.Dto.AreaDto;

import com.example.project31.Dto.FollowingUserDto;
import com.example.project31.Dto.SectorDto;
import com.example.project31.R;
import com.example.project31.Utils.FollowingUserAdapter;
import com.example.project31.Utils.MultiSelectSearchAdapter;
import com.example.project31.Utils.MultiSelectSearchAreaAdapter;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.homePage.HomePage;
import com.example.project31.post.CreateDrive;
import com.example.project31.post.CreatePost;
import com.example.project31.retrofit.OrganizationApi;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.SectorApi;
import com.example.project31.retrofit.TalukaApi;
import com.example.project31.retrofit.UserApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class FollowingUser extends AppCompatActivity {

    UserApi userApi;
    OrganizationApi organizationApi;
    // List<DriveDto> driveDtos= new ArrayList<DriveDto>();
    Integer page=0,size=6,complete=0;
    Call<List<FollowingUserDto>> call ;
    List<FollowingUserDto> followingUserDtos= new ArrayList<FollowingUserDto>();

    TextView sectorSpinner,areaSpinner,talukaSpinner,clearSector,clearLocation;

    String currentTalukaSelected;
    private FollowingUserAdapter followingUserAdapter;
    private RecyclerView followingRV;

    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    private FloatingActionButton btnSort;
    String myView="0",to_isOrganization;
    // private View bottom_navigation;
    //private TextView home,drives,add_drive,notification,profile;
    List<String> sectors,talukas,areas;

    List<String> sectorNames,areaNames,talukaNames;
    ArrayList<AreaDto> responseAreaDtos;
    ArrayList<SectorDto> responseSectorDtos;

    HashMap<String,ArrayList<AreaDto>> selectedAreaDtos;
    String talukaSelected;
    MultiSelectSearchAdapter sectorAdapter;
    MultiSelectSearchAreaAdapter areaAdapter;

    ArrayList<TextView> tvSectors;
    ArrayList<TextView> tvLocations;

    SectorApi sectorApi;
    TalukaApi talukaApi;
    Integer sectorCnt=-1,areaCnt=-1;
    AreaDto currentAreaDto;
    String sortBy="MostPopular";
    String to_user_id;

    // creating a variable for our page and limit as 2
    // as our api is having highest limit as 2 so
    // we are setting a limit = 2
    // int page = 0, limit = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_following_user);

        // creating a new array list.
        //userModalArrayList = new ArrayList<>();

        // initializing our views.
        followingRV = findViewById(R.id.idRVFollowing);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);
        btnSort= findViewById(R.id.btn_sort);
        // bottom_navigation = findViewById(R.id.bottom_navigation);

        TextView home=findViewById(R.id.home);
        TextView drives= findViewById(R.id.drives);
        TextView add_post=findViewById(R.id.add_post);
        TextView notification= findViewById(R.id.notification);
        TextView profile_tab= findViewById(R.id.profile_tab);

     //   Toast.makeText(getApplicationContext(),  "in home page",Toast.LENGTH_SHORT).show();

        String userOrOrganization = SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization();

        Bundle bundle = getIntent().getExtras();


        //myView = bundle.getString("myView", "Default");
        to_isOrganization = bundle.getString("to_isOrganization","Default");
        to_user_id=bundle.getString("to_user_id","Default");

        Logger.getLogger(FollowingUser.class.getName()).log(Level.SEVERE, "just after bundle"+to_user_id);

        String user_id = SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();




        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FollowingUser.this, HomePage.class);
                startActivity(intent);
            }
        });
        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FollowingUser.this, com.example.project31.drivePage.DrivePage.class);
                startActivity(intent);;
            }
        });
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization().equals("0")){
                    Logger.getLogger(com.example.project31.drivePage.DrivePage.class.getName()).log(Level.SEVERE, " home I am an individual ");
                    Intent intent = new Intent(FollowingUser.this, CreatePost.class);
                    startActivity(intent);
                }else{
                    Logger.getLogger(com.example.project31.drivePage.DrivePage.class.getName()).log(Level.SEVERE, " home I am an organization ");
                    Intent intent = new Intent(FollowingUser.this, CreateDrive.class);
                    startActivity(intent);
                }


            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FollowingUser.this, HomePage.class);
                startActivity(intent);
            }
        });
        profile_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("to_user_id", user_id);
                if(userOrOrganization.equals("0")){
                    Intent intent = new Intent(FollowingUser.this, Profile.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(FollowingUser.this, ProfileOrganization.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }


            }
        });

        RetrofitService rs = new RetrofitService();
        userApi = rs.getRetrofit().create(UserApi.class);

        RetrofitService rs1 = new RetrofitService();
        organizationApi = rs.getRetrofit().create(OrganizationApi.class);


        getDataFromAPI(page, size,to_user_id);
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
                    getDataFromAPI(page, size,to_user_id);
                }
            }
        });






    }

    private void getDataFromAPI(int page, int size,  String to_user_id) {

        if(to_isOrganization.equals("1")) {
            Logger.getLogger(FollowingUser.class.getName()).log(Level.SEVERE, "getFollowingUsers By  "+to_user_id);
            call = organizationApi.getFollowingUsers(to_user_id, page, size);

        }
        else {
            Logger.getLogger(FollowingUser.class.getName()).log(Level.SEVERE, "getFollowingOrganizationa By ");
            call = userApi.getFollowingOrganizations(to_user_id, page, size);
        }





        call.enqueue(new Callback<List<FollowingUserDto>>() {
            @Override
            public void onResponse(Call<List<FollowingUserDto>> call, Response<List<FollowingUserDto>> response) {
                List<FollowingUserDto> tempFollowingUserDtos = response.body();
                Logger.getLogger(FollowingUser.class.getName()).log(Level.SEVERE, "helloo "+sortBy);
                Logger.getLogger(FollowingUser.class.getName()).log(Level.SEVERE, "response "+tempFollowingUserDtos.size());
                if(tempFollowingUserDtos.size()==0 ){
                    Toast.makeText(getApplicationContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
                    loadingPB.setVisibility(View.GONE);
                    complete=1;
                }else {
                    Logger.getLogger(FollowingUser.class.getName()).log(Level.SEVERE, "I am in ellse ");
                    followingUserDtos.addAll(tempFollowingUserDtos);
                    followingUserAdapter = new FollowingUserAdapter(followingUserDtos, FollowingUser.this,to_user_id,to_isOrganization);

                    followingRV.setLayoutManager(new LinearLayoutManager(FollowingUser.this));

                    // setting adapter to our recycler view.
                    followingRV.setAdapter(followingUserAdapter);

                    // Toast.makeText(getApplicationContext(),"drive 1"+followingUserDtos.get(0).getDescription(),Toast.LENGTH_SHORT).show();

                    if(tempFollowingUserDtos.size()<size){
                        Toast.makeText(getApplicationContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
                        loadingPB.setVisibility(View.GONE);
                        complete=1;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FollowingUserDto>> call, Throwable t) {
                Logger.getLogger(FollowingUser.class.getName()).log(Level.SEVERE, "Error occurred in spinner",t);
     //           Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

            }
        });


    }





}

