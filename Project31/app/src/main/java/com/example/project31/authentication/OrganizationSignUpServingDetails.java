package com.example.project31.authentication;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.project31.Dto.AreaDto;
import com.example.project31.Dto.OrganizationDto;
import com.example.project31.Dto.Overpass.Element;
import com.example.project31.Dto.Overpass.OverpassResponse;
import com.example.project31.Dto.SectorDto;
import com.example.project31.Dto.TalukaDto;
import com.example.project31.R;
import com.example.project31.Utils.MultiSelectSearchAdapter;
import com.example.project31.Utils.MultiSelectSearchAreaAdapter;
import com.example.project31.homePage.HomePage;
import com.example.project31.post.CreatePost;
import com.example.project31.retrofit.OverpassApi;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.SectorApi;
import com.example.project31.retrofit.TalukaApi;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationSignUpServingDetails extends AppCompatActivity {

    TextView sectorSpinner;
    TextView areaSpinner, tvSelected;
    TextView talukaSpinner;
    TextView clearSector,clearLocation;
    SectorApi sectorApi;
    TalukaApi talukaApi;
    Button btnNext,btnAdd;
    List<String> sectorList ;
    List<String> talukaList;
    List<String> areaList;
    String talukaNametoQuery;
    Dialog dialog;
    List<String> sectorNames;
    List<String> areaNames;
    List<String> talukaNames;
    ArrayList<AreaDto> responseAreaDtos;
    ArrayList<SectorDto> responseSectorDtos;
    ArrayList<SectorDto> selectedSectorDtos;
    HashMap<String,ArrayList<AreaDto>> selectedAreaDtos;
    String talukaSelected;
    MultiSelectSearchAdapter  sectorAdapter;
    MultiSelectSearchAreaAdapter areaAdapter;
    AreaDto currentAreaDto;

    OrganizationDto organizationDto;
    //ArrayList<Chip> cp ;
    ArrayList<TextView> cp ;
    ArrayList<TextView> tvSectors;
    ArrayList<TextView> tvLocations;
    String currentTalukaSelected;

    Integer sectorCnt=-1,areaCnt=-1;

    ProgressBar areaProgressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_organization_sign_up_serving_details);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        RetrofitService rs = new RetrofitService();
        responseAreaDtos=new ArrayList<AreaDto>();
        selectedSectorDtos= new ArrayList<SectorDto>();
        responseSectorDtos= new ArrayList<SectorDto>();
        selectedAreaDtos=new HashMap<String,ArrayList<AreaDto>>();

       // cp = new ArrayList<Chip>();
        cp = new ArrayList<TextView>();
        btnNext = findViewById(R.id.btnNext);
        btnAdd=findViewById(R.id.btnAdd);

        sectorSpinner =findViewById(R.id.sector_tv);
     //   tvSelected= findViewById(R.id.tvSelected);
        //chipContainer = findViewById(R.id.chip_container);
        talukaSpinner = findViewById(R.id.taluka_spinner);
        areaSpinner = findViewById(R.id.tvArea);
        areaProgressBar = findViewById(R.id.areaProgressBar);

        sectorApi = rs.getRetrofit().create(SectorApi.class);
        talukaApi = rs.getRetrofit().create(TalukaApi.class);

        sectorList = new ArrayList<>();
        talukaList = new ArrayList<>();
        areaList = new ArrayList<>();
        tvSectors = new ArrayList<TextView>();
        tvLocations = new ArrayList<TextView>();

        if (getIntent().getExtras() != null) {

            if (intent.hasExtra("OrganizationDto")) {
               // Toast.makeText(getApplicationContext(),"iin intent serving details",Toast.LENGTH_SHORT).show();
                organizationDto= (OrganizationDto) getIntent().getSerializableExtra("OrganizationDto");

            }
           }
        FlowLayout flSectors = findViewById(R.id.flowLayoutSectors);


        FlowLayout flLocations = findViewById(R.id.flowLayoutLocations);





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
                dialog=new Dialog(OrganizationSignUpServingDetails.this);

                dialog.setContentView(R.layout.dialog_seachable_single_spinner);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                Button done = dialog.findViewById(R.id.done);
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
                        selectedSectorDtos.add(new SectorDto(adapter.getItem(position).getSectorId()));

                        Resources res = getResources();
                        Drawable myDrawable = res.getDrawable(R.drawable.text_view_border);


                            tvSectors.add(new TextView(getApplicationContext()));

                            tvSectors.get(i).setId(i);
                            String text = ""+adapter.getItem(position).getSectorId()+"";
                            tvSectors.get(i).setText(text);
                            tvSectors.get(i).setGravity(Gravity.CENTER);
                            tvSectors.get(i).setBackgroundColor(Color.LTGRAY);
                            tvSectors.get(i).getHorizontalFadingEdgeLength();
                            tvSectors.get(i).setPadding(20,10,20,10);

                        tvSectors.get(i).setBackground(myDrawable);
                            flSectors.addView(tvSectors.get(i));



                        sectorSpinner.setText("");

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });


            }
        });




//        tvSector.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Initialize dialog
////                Toast.makeText(getApplicationContext(),"in sector view"+sectorNames.get(0),Toast.LENGTH_SHORT).show();
//                dialog=new Dialog(OrganizationSignUpServingDetails.this);
//
//                dialog.setContentView(R.layout.dialog_searchable_spinner);
//
//                dialog.getWindow().setLayout(1000,2000);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                dialog.show();
//
//                EditText editText=dialog.findViewById(R.id.edit_text);
//                //ListView listView=dialog.findViewById(R.id.list_view);
//                RecyclerView mRvData = dialog.findViewById(R.id.rv_data);
//                mRvData.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                Button done = dialog.findViewById(R.id.done);
//                sectorAdapter = new MultiSelectSearchAdapter(getApplicationContext(),responseSectorDtos);
//
////                listView.setAdapter(sectorAdapter);
////                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//                 mRvData.setAdapter(sectorAdapter);
//
//                editText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        sectorAdapter.getFilter().filter(s);
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
//
//                done.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
////                        selectedSectorDtos= new ArrayList<SectorDto>();
////                        for(Integer i : sectorAdapter.getSelectedPositions()) {
////                            selectedSectorDtos.add(new SectorDto(sectorNames.get(i)));
////
////                        }
//                        tvSector.setText(sectorAdapter.showInView());
//                        dialog.dismiss();
//                    }
//                });
//
//            }
//        });





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
                dialog=new Dialog(OrganizationSignUpServingDetails.this);

                dialog.setContentView(R.layout.dialog_seachable_single_spinner);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                Button done = dialog.findViewById(R.id.done);
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
                        dialog.dismiss();
                    }
                });


            }
        });
//        talukaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                // Code to be executed when an item is selected from the spinner
//                talukaSelected= parent.getItemAtPosition(position).toString();
//                // Do something with the selected item
//                talukaList.add( talukaSelected);
//                //talukaNametoQuery=  "\"" + selectedItem + "\"";
//                fetchSuburbs( talukaSelected);
//
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Code to be executed when no item is selected
//            }
//        });
//
//
//       tvArea.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Initialize dialog
////                Toast.makeText(getApplicationContext(),"in area view"+areaNames.get(0),Toast.LENGTH_SHORT).show();
//                dialog=new Dialog(OrganizationSignUpServingDetails.this);
//
//                dialog.setContentView(R.layout.dialog_searchable_spinner);
//
//                dialog.getWindow().setLayout(1000,2000);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                dialog.show();
//
//                EditText editText=dialog.findViewById(R.id.edit_text);
//                RecyclerView mRvData = dialog.findViewById(R.id.rv_data);
//                mRvData.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//                Button done = dialog.findViewById(R.id.done);
//                areaAdapter = new MultiSelectSearchAreaAdapter(getApplicationContext(),responseAreaDtos);
//
//                mRvData.setAdapter(areaAdapter);
//
//                editText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        areaAdapter.getFilter().filter(s);
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
//
//                done.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//
//                        tvArea.setText(areaAdapter.showInView());
//                        dialog.dismiss();
//                    }
//                });
//
//            }
//        });


        areaSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(),"in sector view"+talukaNames.get(0),Toast.LENGTH_SHORT).show();
                dialog=new Dialog(OrganizationSignUpServingDetails.this);

                dialog.setContentView(R.layout.dialog_seachable_single_spinner);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.CENTER);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                Button done = dialog.findViewById(R.id.done);
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
                        dialog.dismiss();
                    }
                });


            }
        });


       btnAdd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(currentTalukaSelected==null){
                   Toast.makeText(getApplicationContext(),"Please complete the selection of taluka to add in your servings",Toast.LENGTH_SHORT).show();
                   return;
               }
               String talukaSelected = currentTalukaSelected;
               areaCnt++;
               int i =areaCnt;
               if(currentAreaDto==null){
                   currentAreaDto= new AreaDto("All","All",talukaSelected);
               }
               if(selectedAreaDtos.containsKey(talukaSelected)){
                   Logger.getLogger(OrganizationSignUpServingDetails.class.getName()).log(Level.SEVERE, "Contains taluka already "+talukaSelected);
                   selectedAreaDtos.get(talukaSelected).add(  currentAreaDto);

               }else{
                   Logger.getLogger(OrganizationSignUpServingDetails.class.getName()).log(Level.SEVERE, "does noy Contains taluka making new "+talukaSelected);
                   selectedAreaDtos.put(talukaSelected, new ArrayList<AreaDto>());
                   selectedAreaDtos.get(talukaSelected).add(  currentAreaDto);


               }
               for (String key : selectedAreaDtos.keySet()) {
                   Logger.getLogger(OrganizationSignUpServingDetails.class.getName()).log(Level.SEVERE, "Key "+key);

                   ArrayList<AreaDto> areaDtos = selectedAreaDtos.get(key);
                   for (AreaDto areaDto : areaDtos) {
                       Logger.getLogger(OrganizationSignUpServingDetails.class.getName()).log(Level.SEVERE, "Area name "+areaDto.getAreaName());
                   }
               }
               Resources res = getResources();
               Drawable myDrawable = res.getDrawable(R.drawable.text_view_border);
               tvLocations.add(new TextView(getApplicationContext()));
               tvLocations.get(i).setId(i);

               String text = ""+talukaSelected+": "+currentAreaDto.getAreaName()+"";
               tvLocations.get(i).setText(text);
               tvLocations.get(i).setGravity(Gravity.CENTER);
               tvLocations.get(i).setBackgroundColor(Color.LTGRAY);
               tvLocations.get(i).setPadding(20,10,20,10);

               tvLocations.get(i).setBackground(myDrawable);
               flLocations.addView(tvLocations.get(i));


//               String selectedText= (String) tvSelected.getText();
//               selectedText+= "\n"+ talukaSelected + ": "+ areaUnderTaluka;
//               tvSelected.setText(selectedText);
              areaSpinner.setText("");
               talukaSpinner.setText("");

               currentAreaDto=null;

           }
       });


        clearSector = findViewById(R.id.clear_sector);
        clearSector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "selected clear sector");
                selectedSectorDtos= new ArrayList<SectorDto>();
                flSectors.removeAllViews();
                tvSectors= new ArrayList<TextView>();
                sectorCnt=-1;
            }
        });

        clearLocation = findViewById(R.id.clear_location);
        clearLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, "selected clear location");
                selectedAreaDtos= new HashMap<String,ArrayList<AreaDto>>();
                flLocations.removeAllViews();
                tvLocations= new ArrayList<TextView>();
                areaCnt=-1;
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (organizationDto != null && selectedSectorDtos != null && selectedSectorDtos.size() > 0
                        && selectedAreaDtos != null && selectedAreaDtos.size() > 0) {
                }else{
                    Toast.makeText(getApplicationContext(),"Please ensure that you serve in atleast 1 sector and atleast one location",Toast.LENGTH_SHORT).show();
               return;
                }

                    Intent intent = new Intent();
               intent.setClass(OrganizationSignUpServingDetails.this, OrganizationSignUp.class);
//                intent.putExtra("email", etEmail.getText());
//                intent.putExtra("name",etName.getText());
//                intent.putExtra("mobile",etMobile.getText());

              organizationDto.setSectors(new HashSet<SectorDto>());

              for(SectorDto s : selectedSectorDtos){

                  organizationDto.getSectors().add(s);

              }

                organizationDto.setServingTalukas(new HashSet<TalukaDto>() );
                organizationDto.setServingAreas(new HashSet<AreaDto>());

                for (String key : selectedAreaDtos.keySet()) {
                    // retrieve the ArrayList for the current key
                    ArrayList<AreaDto> areaDtos = selectedAreaDtos.get(key);
                    System.out.println("Key: " + key);
                    // iterate through the ArrayList and print each element
                    for (AreaDto areaDto : areaDtos) {

                        if(areaDto.getAreaName().equals("All")){

                            organizationDto.getServingTalukas().add(new TalukaDto(key));

                        }else{
                            organizationDto.getServingAreas().add(areaDto);
                        }
                        System.out.println("  " + areaDto.toString());
                    }
                }

                intent.putExtra("OrganizationDto",organizationDto);
                startActivity(intent);
            }
        });



    }



    private void fetchSuburbs(String taluka) {

        areaProgressBar.setVisibility(View.VISIBLE);
        String talukaNametoQuery=  "\"" + taluka + "\"";
       // Toast.makeText(getApplicationContext(),"in fetch suburbs "+taluka,Toast.LENGTH_SHORT).show();
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

                         if(element.getId()!=null && suburb != null ){
                             if(element.getId().length()!=0 && suburb.length()!=0){

                                 AreaDto areaDto =new AreaDto(element.getId(),suburb,taluka);
                                 areaDto.setLatitude(latitude);
                                 areaDto.setLongitude(longtitude);
                                 responseAreaDtos.add(areaDto);
                              //   Toast.makeText(getApplicationContext(),""+latitude,Toast.LENGTH_SHORT).show();

                             }
                         }


                        //areaNames.add(suburb);

                    }

                    areaProgressBar.setVisibility(View.GONE);
                } else {
                  //  Toast.makeText(getApplicationContext(),"null ",Toast.LENGTH_SHORT).show();
                    // Handle the error
                }


            }

            @Override
            public void onFailure(Call<OverpassResponse> call, Throwable t) {
                // Handle the error

                Logger.getLogger(OrganizationSignUpDetails.class.getName()).log(Level.SEVERE, "Error occurred" +t);
            }
        });
    }


}