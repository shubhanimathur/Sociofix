package com.example.project31.searchOrganization;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.graphics.drawable.Drawable;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project31.Dto.AreaDto;
import com.example.project31.Dto.FollowingUserDto;
import com.example.project31.Dto.Overpass.Element;
import com.example.project31.Dto.Overpass.OverpassResponse;
import com.example.project31.Dto.SectorDto;
import com.example.project31.MainActivity;
import com.example.project31.R;
import com.example.project31.Utils.MultiSelectSearchAdapter;
import com.example.project31.Utils.MultiSelectSearchAreaAdapter;
import com.example.project31.Utils.FollowingUserAdapter;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.authentication.OrganizationSignUpDetails;
import com.example.project31.drivePage.DrivePage;
import com.example.project31.homePage.HomePage;
import com.example.project31.notification.Notification;
import com.example.project31.post.CreateDrive;
import com.example.project31.post.CreatePost;
import com.example.project31.profile.Profile;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.retrofit.OverpassApi;
import com.example.project31.retrofit.OrganizationApi;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.SectorApi;
import com.example.project31.retrofit.TalukaApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class SearchOrganization extends AppCompatActivity {

    OrganizationApi organizationApi;
    List<FollowingUserDto> followingUserDtos= new ArrayList<FollowingUserDto>();
    Integer page=0,size=10,complete=0,searched=0;
String searchQuery;
    TextView sectorSpinner,areaSpinner,talukaSpinner,clearSector,clearLocation;

    String currentTalukaSelected;
    SearchView searchView;
    private FollowingUserAdapter followingUserAdapter;
    private RecyclerView organizationRV;

    private ProgressBar loadingPB,areaProgressBar;
    private NestedScrollView nestedSV;
    private FloatingActionButton btnSort;
    String user_id;
    // private View bottom_navigation;
    //private TextView home,drives,add_post,notification,profile;
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
    String  userOrOrganization;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.explode_in_bottom_right, R.anim.explode_out_top_left);
        setContentView(R.layout.activity_search_organization);

        // creating a new array list.
        //userModalArrayList = new ArrayList<>();

        // initializing our views.
        organizationRV = findViewById(R.id.idRVOrganization);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);
        btnSort= findViewById(R.id.btn_sort);
        searchView = findViewById(R.id.search_bar);
        // bottom_navigation = findViewById(R.id.bottom_navigation);

        TextView home=findViewById(R.id.home);
        TextView drives= findViewById(R.id.drives);
        TextView add_post=findViewById(R.id.add_post);
        TextView notification= findViewById(R.id.notification);
        TextView profile_tab= findViewById(R.id.profile_tab);

      //  Toast.makeText(getApplicationContext(),  "in home page",Toast.LENGTH_SHORT).show();
        user_id = SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();
        userOrOrganization = SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchOrganization.this, HomePage.class);
                startActivity(intent);
            }
        });
        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchOrganization.this, DrivePage.class);
                startActivity(intent);;
            }
        });
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization().equals("0")){
                    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, " home I am an individual ");
                    Intent intent = new Intent(SearchOrganization.this, CreatePost.class);
                    startActivity(intent);
                }else{
                    Logger.getLogger(SearchOrganization.class.getName()).log(Level.SEVERE, " home I am an organization ");
                    Intent intent = new Intent(SearchOrganization.this, CreateDrive.class);
                    startActivity(intent);
                }


            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchOrganization.this, Notification.class);
                startActivity(intent);
            }
        });
        profile_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("to_user_id", user_id);
                if(userOrOrganization.equals("0")){
                    Intent intent = new Intent(SearchOrganization.this, Profile.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(SearchOrganization.this, ProfileOrganization.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }


            }
        });

        RetrofitService rs = new RetrofitService();
        organizationApi = rs.getRetrofit().create(OrganizationApi.class);


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
        //  talukas.add("Pune City");
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



        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                sectorApi = rs.getRetrofit().create(SectorApi.class);
                talukaApi = rs.getRetrofit().create(TalukaApi.class);




                sectors = new ArrayList<String>();
                talukas= new ArrayList<String>();
                areas = new ArrayList<String>();


                Dialog dialog=new Dialog(SearchOrganization.this);
                dialog.setContentView(R.layout.filter);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.show();

                //sector
                FlowLayout flSectors = dialog.findViewById(R.id.flowLayoutSectors);
                FlowLayout flLocations = dialog.findViewById(R.id.flowLayoutLocations);

                sectorSpinner =dialog.findViewById(R.id.sector_tv);
                talukaSpinner = dialog.findViewById(R.id.taluka_spinner);
                areaSpinner = dialog.findViewById(R.id.tvArea);
                areaProgressBar = dialog.findViewById(R.id.areaProgressBar);

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
                        Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Error occurred in spinner",t);

                    }
                });


                sectorSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toast.makeText(getApplicationContext(),"in sector view"+talukaNames.get(0),Toast.LENGTH_SHORT).show();
                        Dialog dialogSector=new Dialog(SearchOrganization.this);

                        dialogSector.setContentView(R.layout.dialog_seachable_single_spinner);

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
                        Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Error occurred in taluka spinner"+t);

                    }
                });


//
                talukaSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toast.makeText(getApplicationContext(),"in sector view"+talukaNames.get(0),Toast.LENGTH_SHORT).show();
                        Dialog dialogTaluka=new Dialog(SearchOrganization.this);

                        dialogTaluka.setContentView(R.layout.dialog_seachable_single_spinner);


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
                        Dialog dialogArea=new Dialog(SearchOrganization.this);

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
                        Logger.getLogger(SearchOrganization.class.getName()).log(Level.SEVERE, "selected clear sector");
                        sectors= new ArrayList<>();
                        flSectors.removeAllViews();
                        tvSectors= new ArrayList<TextView>();
                        sectorCnt=-1;
                    }
                });

                clearLocation = dialog.findViewById(R.id.clear_location);
                clearLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Logger.getLogger(SearchOrganization.class.getName()).log(Level.SEVERE, "selected clear location");
                        areas= new ArrayList<>();
                        talukas= new ArrayList<>();
                        selectedAreaDtos= new HashMap<String,ArrayList<AreaDto>>();
                        flLocations.removeAllViews();
                        tvLocations= new ArrayList<TextView>();
                        areaCnt=-1;
                    }
                });



                Button btnSearch = dialog.findViewById(R.id.btnSearch);
                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestBody.put("Sector", sectors);
                        requestBody.put("Taluka", talukas);
                        requestBody.put("Area", areas);



                        dialog.dismiss();
                        // calling a method to load our api.
                        complete=0;
                        page=0;
                        followingUserDtos= new ArrayList<FollowingUserDto>();
                        followingUserAdapter = new FollowingUserAdapter(followingUserDtos, SearchOrganization.this,"not required","1");
                        organizationRV.setLayoutManager(new LinearLayoutManager(SearchOrganization.this));

                        // setting adapter to our recycler view.
                        organizationRV.setAdapter(followingUserAdapter);
                        getDataFromAPI(page, size,requestBody,user_id);






                    }
                });




            }
        });




        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Override onQueryTextSubmit method which is call when submit query is searched
            @Override
            public boolean onQueryTextSubmit(String query) {

                complete=0;
                page=0;
                followingUserDtos= new ArrayList<FollowingUserDto>();
                followingUserAdapter = new FollowingUserAdapter(followingUserDtos, SearchOrganization.this,"not required","1");
                organizationRV.setLayoutManager(new LinearLayoutManager(SearchOrganization.this));

                // setting adapter to our recycler view.
                organizationRV.setAdapter(followingUserAdapter);
                searched=1;
                searchQuery=query.trim();;
                getDataFromAPI(page, size,requestBody,user_id);


                return false;
            }

            // This method is overridden to filter the adapter according
            // to a search query when the user is typing search
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
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
            Logger.getLogger(SearchOrganization.class.getName()).log(Level.SEVERE, "Sector "+s);
        }
        Call<List<FollowingUserDto>> call;
        if(sectors.size()!=0  && ((talukas.size() != 0) || areas.size()!=0) ){
            Logger.getLogger(SearchOrganization.class.getName()).log(Level.SEVERE, "sectors.size()!=0 && talukas.size()!=0 ");

                call = organizationApi.getOrganizationsByLocationAndSector(requestBody,page,size);


        }else if(sectors.size()!=0 && ((talukas.size() == 0) && areas.size()==0)) {
            Logger.getLogger(SearchOrganization.class.getName()).log(Level.SEVERE, "sectors.size()!=0 && talukas.size()==0 ");

                call = organizationApi.getOrganizationsBySector(requestBody,page,size);


        }else if(sectors.size()==0 && (talukas.size() != 0|| areas.size()!=0)) {
            Logger.getLogger(SearchOrganization.class.getName()).log(Level.SEVERE, "sectors.size()==0 && talukas.size()!=0 ");

                call = organizationApi.getOrganizationsByLocation(requestBody,page,size);


        }else{
            if(searched==0){

                call= organizationApi.getAllOrganizations(page,size);

            }else {
                Logger.getLogger(SearchOrganization.class.getName()).log(Level.SEVERE, "last else " );
                call = organizationApi.getAllOrganizationsBySearch(searchQuery, page, size);
                searched=0;
            }
        }


        call.enqueue(new Callback<List<FollowingUserDto>>() {
            @Override
            public void onResponse(Call<List<FollowingUserDto>> call, Response<List<FollowingUserDto>> response) {
                List<FollowingUserDto> tempFollowingUserDtos = response.body();
                Logger.getLogger(SearchOrganization.class.getName()).log(Level.SEVERE, "sort By ");
                Logger.getLogger(SearchOrganization.class.getName()).log(Level.SEVERE, "response "+tempFollowingUserDtos.size());
                if(tempFollowingUserDtos.size()==0 ){
                    if(page==0){
                        Toast.makeText(getApplicationContext(), "No data available for the selected filter", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
                    }

                    loadingPB.setVisibility(View.GONE);
                    complete=1;
                }else {
                    Logger.getLogger(SearchOrganization.class.getName()).log(Level.SEVERE, "I am in ellse ");
                    followingUserDtos.addAll(tempFollowingUserDtos);
                    followingUserAdapter = new FollowingUserAdapter(followingUserDtos, SearchOrganization.this,"not required","1");
                    organizationRV.setLayoutManager(new LinearLayoutManager(SearchOrganization.this));

                    // setting adapter to our recycler view.
                    organizationRV.setAdapter(followingUserAdapter);

                    // Toast.makeText(getApplicationContext(),"post 1"+followingUserDtos.get(0).getDescription(),Toast.LENGTH_SHORT).show();

                    if(tempFollowingUserDtos.size()<size){
                        Toast.makeText(getApplicationContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
                        loadingPB.setVisibility(View.GONE);
                        complete=1;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FollowingUserDto>> call, Throwable t) {
                Logger.getLogger(SearchOrganization.class.getName()).log(Level.SEVERE, "Error occurred in spinner",t);
          //      Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

            }
        });

        // creating a variable for our json object request and passing our url to it.



    }


    private void fetchSuburbs(String taluka) {

        areaProgressBar.setVisibility(View.VISIBLE);
        String talukaNametoQuery=  "\"" + taluka + "\"";
      //  Toast.makeText(getApplicationContext(),"in fetch suburbs "+taluka,Toast.LENGTH_SHORT).show();
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
                //                Toast.makeText(getApplicationContext(),""+latitude,Toast.LENGTH_SHORT).show();

                            }
                        }
                    }


                } else {
                //    Toast.makeText(getApplicationContext(),"null ",Toast.LENGTH_SHORT).show();
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



}

