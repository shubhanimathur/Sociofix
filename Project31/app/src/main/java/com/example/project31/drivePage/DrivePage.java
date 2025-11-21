package com.example.project31.drivePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project31.Dto.AreaDto;
import com.example.project31.Dto.LocationDto;
import com.example.project31.Dto.Overpass.Element;
import com.example.project31.Dto.Overpass.OverpassResponse;
import com.example.project31.Dto.DriveDto;
import com.example.project31.Dto.SectorDto;
import com.example.project31.R;
import com.example.project31.Utils.MultiSelectSearchAdapter;
import com.example.project31.Utils.MultiSelectSearchAreaAdapter;
import com.example.project31.Utils.DriveAdapter;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.authentication.OrganizationSignUpDetails;
import com.example.project31.celebrate.Celebrate;
import com.example.project31.homePage.HomePage;
import com.example.project31.notification.Notification;
import com.example.project31.post.CreateDrive;
import com.example.project31.post.CreatePost;
import com.example.project31.profile.Profile;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.retrofit.OverpassApi;
import com.example.project31.retrofit.DriveApi;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.SectorApi;
import com.example.project31.retrofit.TalukaApi;
import com.example.project31.retrofit.UserApi;
import com.example.project31.searchOrganization.SearchOrganization;
import com.example.project31.settings.Settings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrivePage extends AppCompatActivity {

    DriveApi driveApi;
    UserApi userApi;
    List<DriveDto> driveDtos= new ArrayList<DriveDto>();
    Integer page=0,size=6,complete=0,tabPopular=1,tabFollowing,tabLocality;

    TextView sectorSpinner,areaSpinner,talukaSpinner,clearSector,clearLocation,mymenu;

    String currentTalukaSelected;
    private DriveAdapter driveAdapter;
    private RecyclerView driveRV;

    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    private FloatingActionButton btnSort,btnSearchOrganization;
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
    LocationDto locationDto;

    ProgressBar areaProgressBar;
    // creating a variable for our page and limit as 2
    // as our api is having highest limit as 2 so
    // we are setting a limit = 2
    // int page = 0, limit = 2;
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_drive_page);

        // creating a new array list.
        //userModalArrayList = new ArrayList<>();

        // initializing our views.
        driveRV = findViewById(R.id.idRVDrives);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);
        btnSort= findViewById(R.id.btn_sort);
        btnSearchOrganization= findViewById(R.id.btn_search_organization);
        mymenu= findViewById(R.id.mymenu);
        // bottom_navigation = findViewById(R.id.bottom_navigation);

        TextView home=findViewById(R.id.home);
        TextView drives= findViewById(R.id.drives);
        TextView add_post=findViewById(R.id.add_post);
        TextView notification= findViewById(R.id.notification);
        TextView profile_tab= findViewById(R.id.profile_tab);

      //  Toast.makeText(getApplicationContext(),  "in home page",Toast.LENGTH_SHORT).show();
        String user_id = SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();
        String userOrOrganization = SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization();

         locationDto= new LocationDto();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrivePage.this, HomePage.class);
                startActivity(intent);
            }
        });
        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrivePage.this, DrivePage.class);
                startActivity(intent);;
            }
        });
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization().equals("0")){
                    Logger.getLogger(DrivePage.class.getName()).log(Level.SEVERE, " home I am an individual ");
                    Intent intent = new Intent(DrivePage.this, CreatePost.class);
                    startActivity(intent);
                }else{
                    Logger.getLogger(DrivePage.class.getName()).log(Level.SEVERE, " home I am an organization ");
                    Intent intent = new Intent(DrivePage.this, CreateDrive.class);
                    startActivity(intent);
                }


            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrivePage.this, Notification.class);
                startActivity(intent);
            }
        });
        profile_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("to_user_id", user_id);
                if(userOrOrganization.equals("0")){
                    Intent intent = new Intent(DrivePage.this, Profile.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(DrivePage.this, ProfileOrganization.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }


            }
        });

        RetrofitService rs = new RetrofitService();
        driveApi = rs.getRetrofit().create(DriveApi.class);


        RetrofitService rs1 = new RetrofitService();
        userApi = rs1.getRetrofit().create(UserApi.class);


        responseAreaDtos=new ArrayList<AreaDto>();

        responseSectorDtos= new ArrayList<SectorDto>();
        selectedAreaDtos=new HashMap<String,ArrayList<AreaDto>>();


        tvSectors = new ArrayList<TextView>();
        tvLocations = new ArrayList<TextView>();




        Map<String, Object> requestBody = new HashMap<>();
        sectors = new ArrayList<String>();
//        sectors.add("Animal Welfare");
//        sectors.add("Agriculture");
//        sectors.add("Education");
//        sectors.add("Women Empowerment");
//        sectors.add("Economics");

        talukas= new ArrayList<String>();
    //    talukas.add("Pune City");
        areas = new ArrayList<String>();


        requestBody.put("Sector", sectors);
        requestBody.put("Taluka", talukas);
        requestBody.put("Area", areas);




        // calling a method to load our api.
        getDataFromAPI(page, size,requestBody,user_id);

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
                    getDataFromAPI(page, size,requestBody,user_id);
                }
            }
        });


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Perform action when a tab is selected
                switch (tab.getPosition()) {
                    case 0:
                        tabPopular = 1;
                        tabFollowing = 0;
                        tabLocality=0;
                        btnSort.setVisibility(View.VISIBLE);
                        btnSearchOrganization.setVisibility(View.GONE);

                        break;
                    case 1:
                        tabPopular = 0;
                        tabFollowing = 1;
                        tabLocality=0;

                        btnSort.setVisibility(View.GONE);
                        btnSearchOrganization.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        tabPopular = 0;
                        tabFollowing = 0;
                        tabLocality=1;
                        btnSort.setVisibility(View.GONE);
                        btnSearchOrganization.setVisibility(View.GONE);
                        break;


                }
                driveDtos = new ArrayList<DriveDto>();
                driveAdapter = new DriveAdapter(driveDtos, DrivePage.this);
                driveRV.setLayoutManager(new LinearLayoutManager(DrivePage.this));
                driveRV.setAdapter(driveAdapter);
                page = 0;
                complete = 0;
                nestedSV.smoothScrollTo(0, 0);
                getDataFromAPI(page, size, requestBody,user_id);
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

        mymenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialogMyMenu=new Dialog(DrivePage.this);

                dialogMyMenu.setContentView(R.layout.dialog_mymenu);

                dialogMyMenu.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                dialogMyMenu.getWindow().setGravity(Gravity.CENTER);


                dialogMyMenu.show();

                TextView email=dialogMyMenu.findViewById(R.id.email);
                TextView toCelebrate=dialogMyMenu.findViewById(R.id.toCelebrate);
                TextView toSearchOrganizations=dialogMyMenu.findViewById(R.id.toSearchOrganizations);
                TextView toSettings=dialogMyMenu.findViewById(R.id.toSettings);
                TextView close=dialogMyMenu.findViewById(R.id.close);

                email.setText(SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail());

                toCelebrate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DrivePage.this, Celebrate.class);
                        startActivity(intent);
                    }
                });

                toSearchOrganizations.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DrivePage.this, SearchOrganization.class);
                        startActivity(intent);
                    }
                });
                toSettings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DrivePage.this, Settings.class);
                        startActivity(intent);
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogMyMenu.dismiss();
                    }
                });


            }
        });

        btnSearchOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SearchOrganization.class);

                startActivity(intent);



            }
        });
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                sectorApi = rs.getRetrofit().create(SectorApi.class);
                talukaApi = rs.getRetrofit().create(TalukaApi.class);




                sectors = new ArrayList<String>();
                talukas= new ArrayList<String>();
                areas = new ArrayList<String>();


                Dialog dialog=new Dialog(DrivePage.this);
                dialog.setContentView(R.layout.sort_filter);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
               dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.show();

                //sector
                FlowLayout flSectors = dialog.findViewById(R.id.flowLayoutSectors);
                FlowLayout flLocations = dialog.findViewById(R.id.flowLayoutLocations);
                Button btnMostPopular= dialog.findViewById(R.id.btnMostPopular);
                Button btnMostRecent= dialog.findViewById(R.id.btnMostRecent);
                sectorSpinner =dialog.findViewById(R.id.sector_tv);
                talukaSpinner = dialog.findViewById(R.id.taluka_spinner);
                areaSpinner = dialog.findViewById(R.id.tvArea);
                areaProgressBar= dialog.findViewById(R.id.areaProgressBar);

                Call<List<String>> call = sectorApi.getSectorNames();
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        sectorNames = response.body();
                        for(String name:sectorNames){
                            responseSectorDtos.add(new SectorDto(name));
                        }

                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Logger.getLogger(CreateDrive.class.getName()).log(Level.SEVERE, "Error occurred in spinner",t);

                    }
                });


                sectorSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toast.makeText(getApplicationContext(),"in sector view"+talukaNames.get(0),Toast.LENGTH_SHORT).show();
                        Dialog dialogSector=new Dialog(DrivePage.this);

                        dialogSector.setContentView(R.layout.dialog_seachable_single_spinner);

//                        dialogSector.getWindow().setLayout(1000,2000);
                        dialogSector.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialogSector.getWindow().setGravity(Gravity.CENTER);

                        dialogSector.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialogSector.show();

                        EditText editText=dialogSector.findViewById(R.id.edit_text);
                        ListView listView=dialogSector.findViewById(R.id.list_view);

                        Button done = dialogSector.findViewById(R.id.done);
                        // MultiSelectSearchAdapter adapter = new MultiSelectSearchAdapter(getApplicationContext(),R.layout.list_item,sectorNames);
                        ArrayAdapter<SectorDto> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,responseSectorDtos);
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

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // when item selected from list
                                // set selected item on textView
                                sectorCnt++;
                                int i= sectorCnt;
                                sectorSpinner.setText(adapter.getItem(position).getSectorId());
                                sectors.add(adapter.getItem(position).getSectorId());

                                Resources res = getResources();
                                Drawable myDrawable = res.getDrawable(R.drawable.text_view_border);


                                tvSectors.add(new TextView(getApplicationContext()));

                                tvSectors.get(i).setId(i);
                                String text = ""+adapter.getItem(position).getSectorId()+"";
                                tvSectors.get(i).setText(text);
                                tvSectors.get(i).setGravity(Gravity.CENTER);
                                tvSectors.get(i).setBackgroundResource(R.color.darkRed);
                                tvSectors.get(i).getHorizontalFadingEdgeLength();
                                tvSectors.get(i).setPadding(20,10,20,10);

                                tvSectors.get(i).setBackground(myDrawable);
                                flSectors.addView(tvSectors.get(i));

                                sectorSpinner.setText("");
                                // Dismiss dialog
                                dialogSector.dismiss();
                            }
                        });


                    }
                });

                // sector end

                //taluka

                Call<List<String>> call1 = talukaApi.getTalukaNames();
                call1.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call1, Response<List<String>> response) {
                        talukaNames = response.body();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,talukaNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // talukaSpinner.setAdapter(adapter);
                        // fetchSuburbs(talukaNametoQuery);

                    }

                    @Override
                    public void onFailure(Call<List<String>> call1, Throwable t) {
                        Logger.getLogger(CreateDrive.class.getName()).log(Level.SEVERE, "Error occurred in taluka spinner"+t);

                    }
                });


//
                talukaSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toast.makeText(getApplicationContext(),"in sector view"+talukaNames.get(0),Toast.LENGTH_SHORT).show();
                        Dialog dialogTaluka=new Dialog(DrivePage.this);

                        dialogTaluka.setContentView(R.layout.dialog_seachable_single_spinner);

//
                        dialogTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialogTaluka.getWindow().setGravity(Gravity.CENTER);
                        dialogTaluka.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialogTaluka.show();

                        EditText editText=dialogTaluka.findViewById(R.id.edit_text);
                        ListView listView=dialogTaluka.findViewById(R.id.list_view);

                        Button done = dialogTaluka.findViewById(R.id.done);
                        // MultiSelectSearchAdapter adapter = new MultiSelectSearchAdapter(getApplicationContext(),R.layout.list_item,sectorNames);
                        ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,talukaNames);
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

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // when item selected from list
                                // set selected item on textView
                                talukaSpinner.setText(adapter.getItem(position));
                                currentTalukaSelected=adapter.getItem(position);
                                fetchSuburbs(adapter.getItem(position));
                                // Dismiss dialog
                                dialogTaluka.dismiss();
                            }
                        });


                    }
                });

//taluka end
                //area start
                areaSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toast.makeText(getApplicationContext(),"in sector view"+talukaNames.get(0),Toast.LENGTH_SHORT).show();
                        Dialog dialogArea=new Dialog(DrivePage.this);

                        dialogArea.setContentView(R.layout.dialog_seachable_single_spinner);

                        dialogArea.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialogArea.getWindow().setGravity(Gravity.CENTER);
                        dialogArea.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialogArea.show();

                        EditText editText=dialogArea.findViewById(R.id.edit_text);
                        ListView listView=dialogArea.findViewById(R.id.list_view);

                        Button done = dialogArea.findViewById(R.id.done);
                        // MultiSelectSearchAdapter adapter = new MultiSelectSearchAdapter(getApplicationContext(),R.layout.list_item,sectorNames);
                        ArrayAdapter<AreaDto> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,responseAreaDtos);
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

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // when item selected from list
                                // set selected item on textView
                                areaSpinner.setText(adapter.getItem(position).getAreaName());
                                currentAreaDto= adapter.getItem(position);
                                // Dismiss dialog
                                dialogArea.dismiss();
                            }
                        });


                    }
                });
//area end
                Button btnAdd = dialog.findViewById(R.id.btnAdd);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(currentTalukaSelected==null){
                            Toast.makeText(getApplicationContext(),"Please complete the selection of taluka",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String talukaSelected = currentTalukaSelected;
                        areaCnt++;
                        int i =areaCnt;

                        if(selectedAreaDtos.containsKey(talukaSelected)){

                            selectedAreaDtos.get(talukaSelected).add(  currentAreaDto);

                        }else{

                            selectedAreaDtos.put(talukaSelected, new ArrayList<AreaDto>());
                            selectedAreaDtos.get(talukaSelected).add(  currentAreaDto);


                        }
                        Resources res = getResources();
                        Drawable myDrawable = res.getDrawable(R.drawable.text_view_border);
                        tvLocations.add(new TextView(getApplicationContext()));
                        tvLocations.get(i).setId(i);
                        if(currentAreaDto==null){
                            currentAreaDto= new AreaDto("All","All",talukaSelected);
                        }
                        String text = ""+talukaSelected+": "+currentAreaDto.getAreaName()+"";
                        tvLocations.get(i).setText(text);
                        tvLocations.get(i).setGravity(Gravity.CENTER);
                        tvLocations.get(i).setBackgroundColor(Color.LTGRAY);
                        tvLocations.get(i).setPadding(20,10,20,10);

                        tvLocations.get(i).setBackground(myDrawable);
                        flLocations.addView(tvLocations.get(i));

                        if(currentAreaDto==null || currentAreaDto.getAreaId().equals("All")){
                            talukas.add(talukaSelected);
                        }else{
                            areas.add(currentAreaDto.getAreaId());
                        }


                        areaSpinner.setText("");
                        talukaSpinner.setText("");
                        currentAreaDto=null;
                    }
                });

                clearSector = dialog.findViewById(R.id.clear_sector);
                clearSector.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "selected clear sector");
                        sectors= new ArrayList<>();
                        flSectors.removeAllViews();
                        tvSectors= new ArrayList<TextView>();
                    }
                });

                clearLocation = dialog.findViewById(R.id.clear_location);
                clearLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "selected clear location");
                        areas= new ArrayList<>();
                        talukas= new ArrayList<>();
                        selectedAreaDtos= new HashMap<String,ArrayList<AreaDto>>();
                        flLocations.removeAllViews();
                        tvLocations= new ArrayList<TextView>();
                    }
                });

                btnMostPopular.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnMostPopular.setBackgroundColor(0xBFBFBBBB);
                        btnMostRecent.setBackgroundColor(0xE9E9E7E7);
                        sortBy="MostPopular";
                    }
                });

                btnMostRecent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnMostPopular.setBackgroundColor(0xE9E9E7E7);
                        btnMostRecent.setBackgroundColor(0xBFBFBBBB);
                        sortBy="MostRecent";
                    }
                });
                Button btnSearch = dialog.findViewById(R.id.btnSearch);
                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBody.put("Sector", sectors);
                        requestBody.put("Taluka", talukas);
                        requestBody.put("Area", areas);

                        String user_id = SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();

                        dialog.dismiss();
                        // calling a method to load our api.
                        complete=0;
                        page=0;
                        driveDtos= new ArrayList<DriveDto>();
                        driveAdapter = new DriveAdapter(driveDtos, DrivePage.this);
                        driveRV.setLayoutManager(new LinearLayoutManager(DrivePage.this));

                        // setting adapter to our recycler view.
                        driveRV.setAdapter(driveAdapter);
                        getDataFromAPI(page, size,requestBody,user_id);






                    }
                });




            }
        });




    }

    private void getDataFromAPI(int page, int size, Map<String, Object> requestBody, String user_id) {
//        if (page > size) {
//            // checking if the page number is greater than limit.
//            // displaying toast message in this case when page>limit.
//            Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show();
//
//            // hiding our progress bar.
//            loadingPB.setVisibility(View.GONE);
//            return;
//        }

        if(complete==1){

            Toast.makeText(this, "All the data loaded already", Toast.LENGTH_SHORT).show();
            loadingPB.setVisibility(View.GONE);
            return;
        }
        for(String s : (ArrayList<String>)requestBody.get("Sector")){
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "Sector "+s);
        }
        Call<List<DriveDto>> call;
        if(tabPopular==1) {


            if (sectors.size() != 0  && ((talukas.size() != 0) || areas.size()!=0) ){
                Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "sectors.size()!=0 && talukas.size()!=0 " + sortBy);
                if (sortBy.equals("MostPopular"))
                    call = driveApi.getDrivebyLocationAndSectorMostPopular(requestBody, user_id, page, size);
                else
                    call = driveApi.getDrivebyLocationAndSectorMostRecent(requestBody, user_id, page, size);

            } else if (sectors.size() != 0 && ((talukas.size() == 0) && areas.size()==0)) {
                Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "sectors.size()!=0 && talukas.size()==0 " + sortBy);
                if (sortBy.equals("MostPopular"))
                    call = driveApi.getDrivebySectorMostPopular(requestBody, user_id, page, size);
                else
                    call = driveApi.getDrivebySectorMostRecent(requestBody, user_id, page, size);

            } else if (sectors.size() == 0 && (talukas.size() != 0|| areas.size()!=0)) {
                Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "sectors.size()==0 && talukas.size()!=0 " + sortBy);
                if (sortBy.equals("MostPopular"))
                    call = driveApi.getDrivebyLocationMostPopular(requestBody, user_id, page, size);
                else
                    call = driveApi.getDrivebyLocationMostRecent(requestBody, user_id, page, size);

            } else {
                Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "going for default : most popular tab selected " + sortBy);

                call = driveApi.getDrivebyDefaultMostPopular(user_id, page, size);
            }
        }else if(tabFollowing==1){

            call = userApi.getDrivesForFollowedOrganizations( user_id, user_id, page, size);
        }else if(tabLocality==1){
            enableUserLocation();
            call = driveApi.getDrivebyMyLocalityMostRecent( locationDto, user_id, page, size);
        }

        else{
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "going for default : drives tab selected " + sortBy);
            call= userApi.getDrivesForFollowedOrganizations( user_id, user_id, page, size);
        }

        call.enqueue(new Callback<List<DriveDto>>() {
            @Override
            public void onResponse(Call<List<DriveDto>> call, Response<List<DriveDto>> response) {
                List<DriveDto> tempDriveDtos = response.body();
                Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "sort By "+sortBy);
                Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "response "+tempDriveDtos.size());
                if(tempDriveDtos.size()==0 ){
                    if(page==0){
                        Toast.makeText(getApplicationContext(), "No data available for the selected filter", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
                    }

                    loadingPB.setVisibility(View.GONE);
                    complete=1;
                }else {
                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "I am in ellse ");
                    driveDtos.addAll(tempDriveDtos);
                    driveAdapter = new DriveAdapter(driveDtos, DrivePage.this);
                    driveRV.setLayoutManager(new LinearLayoutManager(DrivePage.this));

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
                Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "Error occurred in spinner",t);
               //
                // Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

            }
        });

        // creating a variable for our json object request and passing our url to it.



    }


    private void fetchSuburbs(String taluka) {

        areaProgressBar.setVisibility(View.VISIBLE);
        String talukaNametoQuery=  "\"" + taluka + "\"";
     //   Toast.makeText(getApplicationContext(),"in fetch suburbs "+taluka,Toast.LENGTH_SHORT).show();
        RetrofitService rsOverpass = new RetrofitService("taluka");
        OverpassApi service = rsOverpass.getRetrofitOverpass().create(OverpassApi.class);
        areaNames = new ArrayList<>();
        String data = String.format("[out:json];area[\"name\"=%s]->.searchArea;(node[\"place\"=\"suburb\"](area.searchArea);node[\"place\"=\"village\"](area.searchArea);node[\"place\"=\"city\"](area.searchArea);node[\"place\"=\"town\"](area.searchArea););out;", talukaNametoQuery);

        responseAreaDtos=new ArrayList<AreaDto>();

        Call<OverpassResponse> call = service.getSuburbs(data);
        call.enqueue(new Callback<OverpassResponse>() {
            @Override
            public void onResponse(Call<OverpassResponse> call, Response<OverpassResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseAreaDtos.add(new AreaDto("All","All",taluka));
                    OverpassResponse overpassResponse = response.body();
                    List<Element> elements = overpassResponse.getElements();
                    for (Element element : elements) {
                        String suburb = element.getTags().getName();
//                         Toast.makeText(getApplicationContext(),""+element.getLat(),Toast.LENGTH_SHORT).show();
                        Double latitude = Double.parseDouble(element.getLat());
                        Double longtitude = Double.parseDouble(element.getLon());

                        //areaNames.add(suburb);

                        if(element.getId()!=null && suburb != null ){
                            if(element.getId().length()!=0 && suburb.length()!=0){

                                AreaDto areaDto =new AreaDto(element.getId(),suburb,taluka);
                                areaDto.setLatitude(latitude);
                                areaDto.setLongitude(longtitude);
                                responseAreaDtos.add(areaDto);
                           //     Toast.makeText(getApplicationContext(),""+latitude,Toast.LENGTH_SHORT).show();

                            }
                        }


                    }


                } else {
           //         Toast.makeText(getApplicationContext(),"null ",Toast.LENGTH_SHORT).show();
                    // Handle the error
                }

                areaProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<OverpassResponse> call, Throwable t) {
                // Handle the error

                Logger.getLogger(OrganizationSignUpDetails.class.getName()).log(Level.SEVERE, "Error occurred" +t);
            }
        });
    }
    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            currentLocation = new Location(location.getLatitude(),location.getLongitude());
//            currentLocation.setLatitude(location.getLatitude());
//            currentLocation.setLatitude(location.getLongitude());
//            Toast.makeText(this, " lat "+location.getLatitude()+" long "+location.getLongitude() , Toast.LENGTH_SHORT).show();

            if(location!=null) {
                locationDto = new LocationDto(location.getLatitude(), location.getLongitude());
            }


        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
               // Toast.makeText(this, "if if", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
                //mMap.setMyLocationEnabled(true);
            } else {
             //   Toast.makeText(this, "if else", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);

            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            currentLocation = new Location(location.getLatitude(),location.getLongitude());
//            currentLocation.setLatitude(location.getLatitude());
//            currentLocation.setLatitude(location.getLongitude());
//            Toast.makeText(this, " lat "+location.getLatitude()+" long "+location.getLongitude() , Toast.LENGTH_SHORT).show();
                if(location!=null) {
                    locationDto = new LocationDto(location.getLatitude(), location.getLongitude());
                }

            }
        }
        return;
    }

}

