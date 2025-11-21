package com.example.project31.post;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.project31.Dto.DriveDto;
import com.example.project31.Dto.OrganizationDto;
import com.example.project31.R;
import com.example.project31.Utils.Constant;
import com.example.project31.Utils.MapsActivity3;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.drivePage.DrivePage;
import com.example.project31.homePage.HomePage;
import com.example.project31.notification.Notification;
import com.example.project31.profile.Profile;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.profile.ProfileOrganizationAll;
import com.example.project31.retrofit.DriveApi;
import com.example.project31.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriveViewFull extends AppCompatActivity {

    DriveDto driveDto;
    DriveApi driveApi;
    Integer drive_id;
    Call<DriveDto> getDrive;

    String imagePath="https://demo4-s3.s3.us-east-2.amazonaws.com/";

    private TextView name, location, sector,time,description,upvotes,save,blank,volunteerNo,time_created_at;
    private ImageView driveImage,profile,blurImage;


    private Button volunteer;

    Context context;

    String user_id;
    private ProgressBar loadingImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        overridePendingTransition(R.anim.fadein, R.anim.explode_outwards);

        setContentView(R.layout.activity_drive_view_full);


        name =  findViewById(R.id.name);
        location =  findViewById(R.id.location);
        sector =  findViewById(R.id.sector);
        time =  findViewById(R.id.time);
        description =  findViewById(R.id.description);
        upvotes= findViewById(R.id.upvotes);
        save= findViewById(R.id.save);
        volunteer=  findViewById(R.id.volunteer);
        driveImage =  findViewById(R.id.driveImage);

        volunteerNo=  findViewById(R.id.volunteerNo);
        blank=  findViewById(R.id.blank);
        profile=  findViewById(R.id.profile);
        time_created_at= findViewById(R.id.time_created_at);
        loadingImg=findViewById(R.id.loadingImg);

        blurImage= findViewById(R.id.blurImage);

        context= getApplicationContext();

        user_id = SharedPreferencesHelper.getInstance(this).getUserEmail();



        user_id = SharedPreferencesHelper.getInstance(this).getUserEmail();

        RetrofitService rs = new RetrofitService();
        driveApi = rs.getRetrofit().create(DriveApi.class);

        Bundle bundle = getIntent().getExtras();


        String temp_drive_id = bundle.getString("drive_id", "Default");
        drive_id = Integer.parseInt(temp_drive_id);


        TextView home=findViewById(R.id.home);
        TextView drives= findViewById(R.id.drives);
        TextView add_drive=findViewById(R.id.add_post);
        TextView notification= findViewById(R.id.notification);
        TextView profile_tab = findViewById(R.id.profile_tab);
        Call<OrganizationDto> callGetUser=null;



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });
        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DrivePage.class);
                startActivity(intent);;
            }
        });
        add_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization().equals("0")){
                    Intent intent = new Intent(getApplicationContext(), CreateDrive.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), CreateDrive.class);
                    startActivity(intent);
                }
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Notification.class);
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


        getDrive=driveApi.getDrive(  SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail(),drive_id);

        getDrive.enqueue(new Callback<DriveDto>() {
            @Override
            public void onResponse(Call<DriveDto> call, Response<DriveDto> response) {

              driveDto= response.body();

                name.setText(driveDto.getName());
                String locationStr = driveDto.getLocation().getTaluka()+", "+driveDto.getLocation().getAreaName();
                location.setText(locationStr);
                location.setTextColor(Color.BLUE);

                sector.setText(driveDto.getSector().getSectorId());
                if(driveDto.getWhenAt()!=null){
                    int timePosititon = driveDto.getWhenAt().toString().indexOf("T");
                    String dateStr = driveDto.getWhenAt().toString().substring(0,timePosititon);
                    String timeStr =driveDto.getWhenAt().getHour()+":"+driveDto.getWhenAt().getMinute();
                    time.setText(timeStr+" "+dateStr);
                }

                if(driveDto.getCreatedAt()!=null){
                    int timePosititon = driveDto.getCreatedAt().toString().indexOf("T");
                    String timeStr = driveDto.getCreatedAt().toString().substring(0,timePosititon);
                    time_created_at.setText(timeStr);
                }else{
                    Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "elseee nulllllllllll");
                }


                description.setText(driveDto.getDescription());
                upvotes.setText(driveDto.getUpvotes().toString());
                volunteerNo.setText(""+driveDto.getVolunteers().toString()+" volunteers");
                if(driveDto.getMyUpvote()==1)
                    upvotes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upvote_true, 0, 0, 0);
                if( driveDto.getMyVolunteerStatus()!= null && driveDto.getMyVolunteerStatus()==1){
                    float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                    volunteer.setText("Volunteered");
                    volunteer.setTextSize(pixels);
                }
                if(user_id.equals(driveDto.getUserEmail())){
                    volunteer.setVisibility(View.GONE);
                }
                if(Constant.serverImages==1 && driveDto.getProfileImg()!=null) {
                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "has profile image");
                    Glide.with(context).load(imagePath+driveDto.getProfileImg()).into(profile);

                }

                if(Constant.serverImages==1 && driveDto.getImagePath()!=null) {

                    if( driveDto.getImagePath().charAt(0)=='U'){

                        blurImage.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(imagePath+driveDto.getImagePath())
                                .transform(new BlurTransformation(20, 1))
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        loadingImg.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        loadingImg.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(driveImage);
                        loadingImg.setVisibility(View.GONE);

                    }else{

                        Glide.with(context).load(imagePath+driveDto.getImagePath())
                                .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                loadingImg.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                loadingImg.setVisibility(View.GONE);
                                return false;
                            }
                        })
                                .into(driveImage);
                        loadingImg.setVisibility(View.GONE);



                    }


                }else{
                    driveImage.setImageResource(R.drawable.default_img3);
                    loadingImg.setVisibility(View.GONE);
                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "post Image path null");
                }


                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentToMap3 = new Intent(context, MapsActivity3.class);
                        intentToMap3.putExtra("locationDto", driveDto.getLocation());
                        context.startActivity(intentToMap3);

                    }
                });

                blurImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Glide.with(context).load(imagePath+driveDto.getImagePath())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        loadingImg.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        loadingImg.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(driveImage);
                        loadingImg.setVisibility(View.GONE);
                        blurImage.setVisibility(View.GONE);

                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "save clicked drive id: "+driveDto.getDriveId());

                        Call<List<String>> call=driveApi.saveDrive(user_id,driveDto.getDriveId());
                        call.enqueue(new Callback<List<String>>() {
                            @Override
                            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                                List<String> saveResult = response.body();
                                if(saveResult.get(0).equals("Saved")){
                                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Saved Successfully");
                                    Toast.makeText(context, "Drive Saved", Toast.LENGTH_SHORT).show();


                                }else{
                                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Removed saved");
                                    Toast.makeText(context, "Drive Unsaved", Toast.LENGTH_SHORT).show();



                                }

                            }

                            @Override
                            public void onFailure(Call<List<String>> call, Throwable t) {
                                Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Error occurred in upvote",t);


                            }
                        });

                    }
                });

                volunteer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "volunteer clicked drive id: "+driveDto.getDriveId());

                        Call<List<String>> call=driveApi.volunteerDrive(user_id,driveDto.getDriveId());
                        call.enqueue(new Callback<List<String>>() {
                            @Override
                            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                                List<String> volunteerResult = response.body();
                                if(volunteerResult.get(0).equals("Volunteered Successfully")){
                                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Volunteered Successfully");

                                    AlertDialog alert11 = new AlertDialog.Builder(DriveViewFull.this).setMessage(Html.fromHtml("<i><font color='#808080' size='1'>With each step you take towards making a difference, may your journey be filled with hope and positivity</font>.<br><br> <font color='#808080' size='3'>Have a happy drive!</font></i>")).show();
                                    TextView textView = (TextView) alert11.findViewById(android.R.id.message);
                                    textView.setTextSize(11);
                                    textView.setGravity(Gravity.CENTER);
                                    alert11.show();


                                    float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                                    volunteer.setText("Volunteered");
                                    volunteer.setTextSize(pixels);
                                    volunteer.setPadding(20,10,20,10);
                                    volunteer.setBackground(ContextCompat.getDrawable(context, R.drawable.text_view_border));
                                    volunteer.setGravity(Gravity.CENTER);

                                    float pixels1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                                    volunteerNo.setTextSize(pixels1);

                                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Drive vol before u no "+driveDto.getVolunteers().toString());
                                    driveDto.setVolunteers(driveDto.getVolunteers()+1);
                                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "drive vol no after u no "+driveDto.getVolunteers().toString());
                                    volunteerNo.setText(""+driveDto.getVolunteers().toString()+" volunteers");

                                }else{
                                    float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                                    volunteer.setText("Volunteer");
                                    volunteer.setTextSize(pixels);
                                    volunteer.setPadding(20,10,20,10);
                                    volunteer.setBackground(ContextCompat.getDrawable(context, R.drawable.text_view_border2));
                                    volunteer.setGravity(Gravity.CENTER);

                                    float pixels1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                                    volunteerNo.setTextSize(pixels1);

                                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Drive vol before u no "+driveDto.getVolunteers().toString());
                                    driveDto.setVolunteers(driveDto.getVolunteers()-1);
                                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "drive vol no after u no "+driveDto.getVolunteers().toString());
                                    volunteerNo.setText(""+driveDto.getVolunteers().toString()+" volunteers");

                                }

                            }

                            @Override
                            public void onFailure(Call<List<String>> call, Throwable t) {
                                Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Error occurred in upvote",t);


                            }
                        });
                    }
                });

                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(user_id.equals(driveDto.getUserEmail())){

                            Intent intent = new Intent(context, ProfileOrganization.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("to_user_id", user_id);
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);



                        }else{


                            Intent intent = new Intent(context, ProfileOrganizationAll.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("to_user_id", driveDto.getUserEmail());
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);



                        }
                    }
                });

                profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(user_id.equals(driveDto.getUserEmail())){

                            Intent intent = new Intent(context, ProfileOrganization.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("to_user_id", user_id);
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);


                        }else{

                            Intent intent = new Intent(context, ProfileOrganizationAll.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("to_user_id", driveDto.getUserEmail());
                            intent.putExtras(bundle);
                            Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, " goin to profile org all");
                   //         Toast.makeText(context, "going to profile org all", Toast.LENGTH_SHORT).show();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);


                        }
                    }
                });






                upvotes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "upvote clicked drive id: "+driveDto.getDriveId());

                        Call<List<String>> call=driveApi.upvoteDrive(user_id,driveDto.getDriveId());
                        call.enqueue(new Callback<List<String>>() {
                            @Override
                            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                                List<String> upvoteResult = response.body();
                                if(upvoteResult.get(0).equals("Upvoted Successfully")){
                                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Upvoted Successfully");
                                    upvotes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upvote_true, 0, 0, 0);
                                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Ubefore u no "+driveDto.getUpvotes().toString());
                                    driveDto.setUpvotes(driveDto.getUpvotes()+1);
                                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Uafter u no "+driveDto.getUpvotes().toString());
                                    upvotes.setText(driveDto.getUpvotes().toString());

                                }else{
                                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Removed Upvote");
                                    upvotes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upvote_false, 0, 0, 0);
                                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Ubefore down u no "+driveDto.getUpvotes().toString());
                                    driveDto.setUpvotes(driveDto.getUpvotes()-1);
                                    Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Uafter down u no "+driveDto.getUpvotes().toString());
                                    upvotes.setText(driveDto.getUpvotes().toString());

                                }

                            }

                            @Override
                            public void onFailure(Call<List<String>> call, Throwable t) {
                                Logger.getLogger(DriveViewFull.class.getName()).log(Level.SEVERE, "Error occurred in upvote",t);


                            }
                        });
                    }
                });







            }

            @Override
            public void onFailure(Call<DriveDto> call, Throwable t) {

            }
        });

    }
}