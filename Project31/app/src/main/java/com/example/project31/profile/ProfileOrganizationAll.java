package com.example.project31.profile;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project31.Dto.AreaDto;
import com.example.project31.Dto.DriveDto;
import com.example.project31.Dto.OrganizationDto;
import com.example.project31.Dto.SectorDto;
import com.example.project31.Dto.TalukaDto;
import com.example.project31.R;
import com.example.project31.Utils.Constant;
import com.example.project31.Utils.DriveAdapter;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.drivePage.DrivePage;
import com.example.project31.homePage.HomePage;

import com.example.project31.notification.Notification;
import com.example.project31.post.CreateDrive;
import com.example.project31.retrofit.DriveApi;
import com.example.project31.retrofit.OrganizationApi;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.UserApi;
import com.example.project31.settings.Settings;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileOrganizationAll extends AppCompatActivity {


    OrganizationApi organizationApi;
    UserApi userApi;
    DriveApi driveApi;
    List<DriveDto> driveDtos= new ArrayList<DriveDto>();
    ArrayList<AreaDto> servingAreaDtos;
    ArrayList<SectorDto> servingSectorDtos;
    Integer page=0,size=6,complete=0,tabDrives=1,tabUpvoted=0,tabSaved=0;
    private DriveAdapter driveAdapter;
    private RecyclerView driveRV;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    TabLayout tabLayout;
    Dialog dialog;
    OrganizationDto organizationDto;
    private Button follow, info_sector, info_location;
    private TextView settings,drivesNo,orgNo, solvedNo,name,followersTitle,userId,bio,solvedTitle;
    private ImageView profile;

    String imagePath="https://demo4-s3.s3.us-east-2.amazonaws.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_profile_organization_all);

        settings= findViewById(R.id.settings);
        driveRV = findViewById(R.id.idRVDrives);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);

        drivesNo=findViewById(R.id.drivesNo);
        orgNo= findViewById(R.id.orgNo);
        solvedNo = findViewById(R.id.solvedNo);
        solvedTitle= findViewById(R.id.solvedTitle);
        followersTitle= findViewById(R.id.followersTitle);
        name= findViewById(R.id.name);
        userId= findViewById(R.id.userId);
        bio= findViewById(R.id.bio);
        follow= findViewById(R.id.follow);
        info_sector= findViewById(R.id.info_sector);
        info_location= findViewById(R.id.info_location);
        profile=findViewById(R.id.profile);

        servingAreaDtos= new ArrayList<>();
        servingSectorDtos= new ArrayList<>();

        RetrofitService rs = new RetrofitService();

        organizationApi = rs.getRetrofit().create(OrganizationApi.class);

        RetrofitService rs1 = new RetrofitService();
        userApi=rs1.getRetrofit().create(UserApi.class);

        RetrofitService rs2 = new RetrofitService();
        driveApi= rs2.getRetrofit().create(DriveApi.class);

        TextView home=findViewById(R.id.home);
        TextView drives= findViewById(R.id.drives);
        TextView add_drive=findViewById(R.id.add_post);
        TextView notification= findViewById(R.id.notification);
        TextView profile_tab = findViewById(R.id.profile_tab);
        Call<OrganizationDto> callGetUser=null;

        Bundle bundle = getIntent().getExtras();
        String to_user_id = bundle.getString("to_user_id", "Default");
        String user_id = to_user_id;

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileOrganizationAll.this, HomePage.class);
                startActivity(intent);
            }
        });
        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileOrganizationAll.this, DrivePage.class);
                startActivity(intent);;
            }
        });
        add_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization().equals("0")){
                    Intent intent = new Intent(ProfileOrganizationAll.this, CreateDrive.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ProfileOrganizationAll.this, CreateDrive.class);
                    startActivity(intent);
                }
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileOrganizationAll.this, Notification.class);
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






        callGetUser = organizationApi.getOrganization(user_id);



        callGetUser.enqueue(new Callback<OrganizationDto>() {
            @Override
            public void onResponse(Call<OrganizationDto> call, Response<OrganizationDto> response) {
                organizationDto = response.body();
                drivesNo.setText(organizationDto.getDriveNo().toString());
                orgNo.setText(organizationDto.getFollowingUserNo().toString());
                solvedNo.setText(organizationDto.getSolvedPostsNo().toString());
                name.setText(organizationDto.getName());
                userId.setText(organizationDto.getUserId());
                bio.setText(organizationDto.getBio());
                if(Constant.serverImages==1 &&organizationDto.getProfileImg()!=null) {
                    Glide.with(getApplicationContext()).load(imagePath+organizationDto.getProfileImg()).into(profile);

                }
            }

            @Override
            public void onFailure(Call<OrganizationDto> call, Throwable t) {
                Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "Error occurred in user data fetch",t);
    //            Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

            }
        });

        Call<List<String>> call4 = organizationApi.followOrganizationStatus(to_user_id,SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail());

        call4.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> result = response.body();

                if(result.get(0).equals("Following")){
                 //   Toast.makeText(getApplicationContext(), "Followed ", Toast.LENGTH_SHORT).show();

                    float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getApplicationContext().getResources().getDisplayMetrics());
                    follow.setText("Following");
                    follow.setTextSize(pixels);
                    follow.setPadding(20,10,20,10);
                    follow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.text_view_border));
                    follow.setGravity(Gravity.CENTER);


                }else {

                    float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getApplicationContext().getResources().getDisplayMetrics());
                    follow.setText("Follow");
                    follow.setTextSize(pixels);
                    follow.setPadding(20,10,20,10);
                    follow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.text_view_border));
                    follow.setGravity(Gravity.CENTER);




                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "Error occurred in following status",t);
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

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<List<String>> call3 = organizationApi.followOrganization(to_user_id,SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail());

                call3.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        List<String> result = response.body();

                        if(result.get(0).equals("Followed")){
                          //  Toast.makeText(getApplicationContext(), "Followed ", Toast.LENGTH_SHORT).show();
                            Integer followers = organizationDto.getFollowingUserNo()+1;
                            organizationDto.setFollowingUserNo(followers);
                            orgNo.setText(followers.toString());

                            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getApplicationContext().getResources().getDisplayMetrics());
                            follow.setText("Following");
                            follow.setTextSize(pixels);
                            follow.setPadding(20,10,20,10);
                            follow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.text_view_border));
                            follow.setGravity(Gravity.CENTER);




                        }else {

                            Integer followers = organizationDto.getFollowingUserNo()-1;
                            organizationDto.setFollowingUserNo(followers);
                            orgNo.setText(followers.toString());

                            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getApplicationContext().getResources().getDisplayMetrics());
                            follow.setText("Follow");
                            follow.setTextSize(pixels);
                            follow.setPadding(20,10,20,10);
                            follow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.text_view_border));
                            follow.setGravity(Gravity.CENTER);
                            follow.setBackgroundColor(getResources().getColor(R.color.my_dark_grey));



                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "Error occurred in following",t);
                      //  Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

                    }
                });








            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileOrganizationAll.this, Settings.class);
                startActivity(intent);

            }
        });



        info_sector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(),"in sector view"+talukaNames.get(0),Toast.LENGTH_SHORT).show();
                dialog=new Dialog(ProfileOrganizationAll.this);

                dialog.setContentView(R.layout.dialog_seachable_single_spinner);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.show();

                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                Button done = dialog.findViewById(R.id.done);

                if(servingSectorDtos==null || servingAreaDtos.size()==0) {
                    Iterator value = organizationDto.getSectors().iterator();


                    while (value.hasNext()) {
                        servingSectorDtos.add((SectorDto) value.next());
                    }
                }

                // MultiSelectSearchAdapter adapter = new MultiSelectSearchAdapter(getApplicationContext(),R.layout.list_item,sectorNames);
                ArrayAdapter<SectorDto> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,servingSectorDtos);
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }
        });

        info_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(),"in sector view"+talukaNames.get(0),Toast.LENGTH_SHORT).show();
                dialog=new Dialog(ProfileOrganizationAll.this);

                dialog.setContentView(R.layout.dialog_seachable_single_spinner);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.CENTER);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.CENTER);
                if(servingAreaDtos==null || servingAreaDtos.size()==0) {

                    Iterator valueTaluka = organizationDto.getServingTalukas().iterator();

                    TalukaDto talukaDto;

                    while (valueTaluka.hasNext()) {
                        talukaDto= (TalukaDto) valueTaluka.next();

                        servingAreaDtos.add(new AreaDto("All areas",""+talukaDto.getTalukaId()+": "+"All areas",talukaDto.getTalukaId()));

                    }


                    Iterator value = organizationDto.getServingAreas().iterator();
                    AreaDto temp;

                    while (value.hasNext()) {
                        temp =(AreaDto) value.next();
                        temp.setAreaName(temp.getTalukaName()+": "+temp.getAreaName());
                        servingAreaDtos.add(temp);

                    }
                }


                dialog.show();

                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                Button done = dialog.findViewById(R.id.done);
                // MultiSelectSearchAdapter adapter = new MultiSelectSearchAdapter(getApplicationContext(),R.layout.list_item,sectorNames);
                ArrayAdapter<AreaDto> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,servingAreaDtos);
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }
        });





        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Perform action when a tab is selected
                switch (tab.getPosition()) {
                    case 0:
                        tabDrives=1;
                        tabUpvoted=0;
                        tabSaved=0;

                        break;
                    case 1:
                        // Handle tab 2 selection
                        tabDrives=0;
                        tabUpvoted=1;
                        tabSaved=0;

                        break;
                    case 2:
                        // Handle tab 3 selection
                        tabDrives=0;
                        tabUpvoted=0;
                        tabSaved=1;

                        break;
                }
                driveDtos= new ArrayList<DriveDto>();
                driveAdapter = new DriveAdapter(driveDtos, ProfileOrganizationAll.this);
                driveRV.setLayoutManager(new LinearLayoutManager(ProfileOrganizationAll.this));
                driveRV.setAdapter(driveAdapter);
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

        solvedTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ProfileOrganizationPostAll.class);
                Bundle bundle = new Bundle();
                bundle.putString("to_user_id", to_user_id);
//                bundle.putString("acceptedNo",organizationDto.getAcceptedPostsNo().toString());
//                bundle.putString("solvedNo",organizationDto.getSolvedPostsNo().toString());
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });
        solvedNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ProfileOrganizationPostAll.class);
                Bundle bundle = new Bundle();
                bundle.putString("to_user_id", to_user_id);
//                bundle.putString("acceptedNo",organizationDto.getAcceptedPostsNo().toString());
//                bundle.putString("solvedNo",organizationDto.getSolvedPostsNo().toString());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        drivesNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                nestedSV.smoothScrollTo(0, 400);
            }
        });
        orgNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileOrganizationAll.this, FollowingUser.class);
                Bundle bundle = new Bundle();
                bundle.putString("to_isOrganization", "1");
                bundle.putString("to_user_id", to_user_id);
                Logger.getLogger(ProfileOrganizationAll.class.getName()).log(Level.SEVERE, "userId in profilr org all By  "+to_user_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        followersTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileOrganizationAll.this, FollowingUser.class);
                Bundle bundle = new Bundle();
                bundle.putString("to_user_id", to_user_id);
                bundle.putString("to_isOrganization", "1");
                Logger.getLogger(ProfileOrganizationAll.class.getName()).log(Level.SEVERE, "userId in profilr org all By  "+to_user_id);
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

        Call<List<DriveDto>> call=null;
        if(tabDrives==1){
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "tab 1 ");

            call = userApi.getUserDrives(user_id,SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail(),page,size);


        }else if((tabUpvoted==1)){
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "tab 2 ");
            call = userApi.getUserUpvotedDrives(user_id,page,size);

        }else if(tabSaved==1){
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "tab 3 ");
            call = userApi.getUserSavedDrives(user_id,page,size);

        }else{
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "last else no tab seleted");

        }


        call.enqueue(new Callback<List<DriveDto>>() {
            @Override
            public void onResponse(Call<List<DriveDto>> call, Response<List<DriveDto>> response) {
                List<DriveDto> tempDriveDtos = response.body();

                Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "response "+tempDriveDtos.size());
                if(tempDriveDtos.size()==0 ){
                    Toast.makeText(getApplicationContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
                    loadingPB.setVisibility(View.GONE);
                    complete=1;
                }else {
                    Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "I am in ellse ");
                    driveDtos.addAll(tempDriveDtos);
                    driveAdapter = new DriveAdapter(driveDtos, ProfileOrganizationAll.this);
                    driveRV.setLayoutManager(new LinearLayoutManager(ProfileOrganizationAll.this));

                    // setting adapter to our recycler view.
                    driveRV.setAdapter(driveAdapter);

                    // Toast.makeText(getApplicationContext(),"drive 1"+driveDtos.get(0).getDescription(),Toast.LENGTH_SHORT).show();

                    if(tempDriveDtos.size()<size){
                        Toast.makeText(getApplicationContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
                        loadingPB.setVisibility(View.GONE);
                        complete=1;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DriveDto>> call, Throwable t) {
                Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "Error occurred in spinner",t);
            //    Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

            }
        });



    }


}

