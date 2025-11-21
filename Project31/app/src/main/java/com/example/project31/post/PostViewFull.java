package com.example.project31.post;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.project31.Dto.NotificationDto;
import com.example.project31.Dto.OrganizationDto;
import com.example.project31.Dto.PostDto;
import com.example.project31.Dto.UserDto;
import com.example.project31.R;
import com.example.project31.Utils.Constant;
import com.example.project31.Utils.MapsActivity3;
import com.example.project31.Utils.PostAdapter;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.drivePage.DrivePage;
import com.example.project31.homePage.HomePage;
import com.example.project31.notification.Notification;
import com.example.project31.profile.Profile;
import com.example.project31.profile.ProfileAll;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.profile.ProfileOrganizationAll;
import com.example.project31.retrofit.NotificationApi;
import com.example.project31.retrofit.PostApi;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.UserApi;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewFull extends AppCompatActivity {

    PostDto postDto;
    PostApi postApi;



    Integer post_id;
    Call<PostDto> getPost;

    private TextView name, location, sector,time,description,upvotes,save,blank,time_created_at;
    private ImageView postImage,profile,blurImage;

    private ProgressBar loadingImg;
    private View accept_reject;
    private RadioButton pending, accepted, solved;
    private Button helpOrAccept;

    private UserDto meUserDto;

    Context context;

    String user_id;

    UserApi userApi;

    String imagePath="https://demo4-s3.s3.us-east-2.amazonaws.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        overridePendingTransition(R.anim.fadein, R.anim.explode_outwards);
        setContentView(R.layout.activity_post_view_full);

        name =   findViewById(R.id.name);
        location =   findViewById(R.id.location);
        sector =   findViewById(R.id.sector);
        time =   findViewById(R.id.time);
        description =   findViewById(R.id.description);
        upvotes=  findViewById(R.id.upvotes);
        save=  findViewById(R.id.save);
        helpOrAccept=   findViewById(R.id.helpOrAccept);
        postImage =   findViewById(R.id.postImage);
        accept_reject =   findViewById(R.id.status);
        pending=   findViewById(R.id.pending);
        solved=   findViewById(R.id.solved);
        accepted =   findViewById(R.id.accepted);
        blank=   findViewById(R.id.blank);
        profile=   findViewById(R.id.profile);
        time_created_at= findViewById(R.id.time_created_at);
        loadingImg=findViewById(R.id.loadingImg);
        blurImage= findViewById(R.id.blurImage);
        context= getApplicationContext();

        user_id = SharedPreferencesHelper.getInstance(this).getUserEmail();

        RetrofitService rs = new RetrofitService();
        postApi = rs.getRetrofit().create(PostApi.class);

        RetrofitService rs1 = new RetrofitService();
        userApi = rs1.getRetrofit().create(UserApi.class);


        Bundle bundle = getIntent().getExtras();


        String temp_post_id = bundle.getString("post_id", "Default");
        post_id = Integer.parseInt(temp_post_id);

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



        getPost=postApi.getPost(  SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail(),post_id);

        getPost.enqueue(new Callback<PostDto>() {
            @Override
            public void onResponse(Call<PostDto> call, Response<PostDto> response) {

                postDto = response.body();



                name.setText(postDto.getName());
                name.setHint(postDto.getUserEmail());
                String locationStr = postDto.getLocation().getTaluka()+", "+postDto.getLocation().getAreaName();
                location.setText(locationStr);
                location.setTextColor(Color.BLUE);
                sector.setText(postDto.getSector().getSectorId());
                if(Constant.serverImages==1 && postDto.getProfileImg()!=null) {
                    Glide.with(context).load(imagePath+postDto.getProfileImg()).into(profile);

                }


                if(Constant.serverImages==1 && postDto.getImagePath()!=null) {

                    if( postDto.getImagePath().charAt(0)=='U'){

                        blurImage.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(imagePath+postDto.getImagePath())
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
                                .into(postImage);
                        loadingImg.setVisibility(View.GONE);

                    }else{

                        Glide.with(context).load(imagePath+postDto.getImagePath())
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
                                .into(postImage);
                        loadingImg.setVisibility(View.GONE);



                    }

                }else{
                    postImage.setImageResource(R.drawable.default_img3);
                    loadingImg.setVisibility(View.GONE);
                    Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "post Image path null");
                }


                if(postDto.getWhenAt()!=null){
                    int timePosititon = postDto.getWhenAt().toString().indexOf("T");
                    String dateStr = postDto.getWhenAt().toString().substring(0,timePosititon);
                    String timeStr =postDto.getWhenAt().getHour()+":"+postDto.getWhenAt().getMinute();
                    time.setText(timeStr+" "+dateStr);
                }
                if(postDto.getCreatedAt()!=null){

                    int timePosititon = postDto.getCreatedAt().toString().indexOf("T");
                    String timeStr = postDto.getCreatedAt().toString().substring(0,timePosititon);
                    time_created_at.setText(timeStr);

                }

                description.setText(postDto.getDescription());
                upvotes.setText(postDto.getUpvotes().toString());
                if(postDto.getMyUpvote()==1)
                    upvotes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upvote_true, 0, 0, 0);

                String status = postDto.getStatus();

                if(postDto.getUserEmail().equals(user_id) && postDto.getStatus().equals("Pending")){
                    helpOrAccept.setVisibility(View.GONE);
                    Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Gone "+postDto.getDescription());
                }

                if(status.equals("Pending")) {
                    pending.setChecked(true);
                    helpOrAccept.setEnabled(true);

                }
                else if(status.equals("Accepted")) {
                    accepted.setChecked(true);
                    blank.setText(postDto.getOrganizationName()+"- ");
                    blank.setHint(postDto.getOrganizationEmail());
                    helpOrAccept.setEnabled(false);
                    float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                    helpOrAccept.setText("Accepted");
                    helpOrAccept.setTextSize(pixels);
                    helpOrAccept.setPadding(20,10,20,10);
                    helpOrAccept.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    helpOrAccept.setBackground(ContextCompat.getDrawable(context, R.drawable.text_view_border));
                    helpOrAccept.setGravity(Gravity.CENTER);



                }
                else {
                    solved.setChecked(true);
                    if(postDto.getOrganizationName()!=null) {
                       blank.setText(postDto.getOrganizationName() + "- ");
                       blank.setHint(postDto.getOrganizationEmail());
                    }else{
                       blank.setText("Individual User- ");
                       blank.setClickable(false);
                    }


                    helpOrAccept.setClickable(false);
                    helpOrAccept.setEnabled(false);
                    float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
                    helpOrAccept.setText("Solved");
                    helpOrAccept.setTextSize(pixels);
                    helpOrAccept.setPadding(20,10,20,10);
                    helpOrAccept.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    helpOrAccept.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.text_view_border));
                    helpOrAccept.setGravity(Gravity.CENTER);


                }

                RetrofitService rs = new RetrofitService();
                PostApi postApi = rs.getRetrofit().create(PostApi.class);

                String organization= SharedPreferencesHelper.getInstance(context).getOrganization();

                if(organization.equals("0") && postDto.getStatus().equals("Pending")){

                    helpOrAccept.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_help, 0, 0, 0);
                    float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                    helpOrAccept.setTextSize(pixels);

                    helpOrAccept.setText("Help");

                }else if(organization.equals("1")  && postDto.getStatus().equals("Pending")){
                    float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                    helpOrAccept.setText("Accept");
                    helpOrAccept.setTextSize(pixels);
                    helpOrAccept.setPadding(20,10,20,10);
                    helpOrAccept.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    helpOrAccept.setBackground(ContextCompat.getDrawable(context, R.drawable.text_view_border));
                    helpOrAccept.setGravity(Gravity.CENTER);

                }

                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentToMap3 = new Intent(context, MapsActivity3.class);
                        intentToMap3.putExtra("locationDto", postDto.getLocation());
                        context.startActivity(intentToMap3);

                    }
                });

                blurImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Glide.with(context).load(imagePath+postDto.getImagePath())
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
                                .into(postImage);
                        loadingImg.setVisibility(View.GONE);
                        blurImage.setVisibility(View.GONE);

                    }
                });

                helpOrAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(organization.equals("0")){


                            Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "help clicked help box to appear ");
                            Dialog dialog=new Dialog(PostViewFull.this);

                            //dialog.setContentView(R.layout.dialog_hq_location);
                            dialog.setContentView(R.layout.dialog_for_help);

                            dialog.getWindow().setLayout(1100,2200);


                            dialog.show();

                            userApi.getUser(user_id).enqueue(new Callback<UserDto>() {
                                @Override
                                public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                                    meUserDto = response.body();
                                    Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, meUserDto.getName()+meUserDto.getUserId()+meUserDto.getContactNo());
                                    TextView tvName = dialog.findViewById(R.id.userName);
                                    tvName.setText(meUserDto.getName());

                                    TextView tvMail = dialog.findViewById(R.id.userEmailAddress);
                                    Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, tvMail.getText().toString());
                                    tvMail.setText(meUserDto.getUserId());

                                    TextView tvPhone = dialog.findViewById(R.id.userMobile);
                                    tvPhone.setText(meUserDto.getContactNo());

                                    UserDto toUser = new UserDto();
                                    toUser.setUserId(postDto.getUserEmail());

                                    Button btnHelp = dialog.findViewById(R.id.btnHelp);

                                    EditText etDescription = dialog.findViewById(R.id.etDescription);

                                    btnHelp.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            NotificationDto notificationDto = new NotificationDto();
                                            notificationDto.setByUser(meUserDto);
                                            notificationDto.setToUser(toUser);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                notificationDto.setCreatedAt(LocalDateTime.now());
                                            }
                                            notificationDto.setNotificationType("contribute");
                                            notificationDto.setPost(postDto);
                                            notificationDto.setDescription(etDescription.getText().toString());
                                            Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Noti details"+postDto.getPostId());

                                            NotificationApi notificationApi = rs.getRetrofit().create(NotificationApi.class);
                                            notificationApi.saveNotification(notificationDto).enqueue(new Callback<String>() {
                                                @Override
                                                public void onResponse(Call<String> call, Response<String> response) {
                                                    if(response.isSuccessful()) {
                                                        String message = response.body();
                                                        //Toast.makeText(PostAdapter.this, "Save success: " + message, Toast.LENGTH_SHORT).show();
                                                        Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "jseahwjlgvhjdfhkjlhqhdfuidghilgfjlighkljhgfhk"+message);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<String> call, Throwable t) {
                                                    Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Failed to save notification "+t.getMessage());
                                                }
                                            });

                                            dialog.dismiss();
                                        }
                                    });


                                }

                                @Override
                                public void onFailure(Call<UserDto> call, Throwable t) {

                                }
                            });





                        }else{



                            // rest api call to update post status to accepted

                            Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Post accepted else ");
                            Call<List<String>> call=postApi.acceptPost(user_id,postDto.getPostId());
                            call.enqueue(new Callback<List<String>>() {
                                @Override
                                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                                    List<String> acceptedResult = response.body();
                                    if(acceptedResult.get(0).equals("Accepted")){
                                        Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Accepted Successfully");
                                        accepted.setChecked(true);
                                        pending.setChecked(false);
                                        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                                        helpOrAccept.setText("Accepted");
                                        helpOrAccept.setTextSize(pixels);
                                        helpOrAccept.setPadding(20,10,20,10);
                                        helpOrAccept.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                        helpOrAccept.setBackground(ContextCompat.getDrawable(context, R.drawable.text_view_border));
                                        helpOrAccept.setGravity(Gravity.CENTER);

                                        //  helpOrAccept.setVisibility(View.GONE);
                                        Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Post accepted by you ");

                                    }else{
                                        Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Post wasnt a pending post ");

                                    }

                                }

                                @Override
                                public void onFailure(Call<List<String>> call, Throwable t) {
                                    Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Error occurred in accepted",t);


                                }
                            });





                        }


                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "save clicked post id: "+postDto.getPostId());

                        Call<List<String>> call=postApi.savePost(user_id,postDto.getPostId());
                        call.enqueue(new Callback<List<String>>() {
                            @Override
                            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                                List<String> saveResult = response.body();
                                if(saveResult.get(0).equals("Saved")){
                                    Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Saved Successfully");
                                    Toast.makeText(context, "Post Saved", Toast.LENGTH_SHORT).show();


                                }else{
                                    Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Removed saved");
                                    Toast.makeText(context, "Post Unsaved", Toast.LENGTH_SHORT).show();



                                }

                            }

                            @Override
                            public void onFailure(Call<List<String>> call, Throwable t) {
                                Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Error occurred in upvote",t);


                            }
                        });

                    }
                });

                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(user_id.equals(postDto.getUserEmail())){

                            Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "eauals"+postDto.getOrganizationEmail());

//                    if(userOrOrganization.equals("0")){
                            Intent intent = new Intent(context, Profile.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("to_user_id", postDto.getUserEmail());
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
//                    }else{
//                        Intent intent = new Intent(context, ProfileOrganization.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("to_user_id", postDto.getUserEmail());
//                        intent.putExtras(bundle);
//                        context.startActivity(intent);
//
//                    }

                        }else{
//                    Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "go not equals "+postDto.getOrganizationEmail());
//                    userApi = rs.getRetrofit().create(UserApi.class);
//                    callGetUser = userApi.getUser(postDto.getUserEmail());
//
//                    callGetUser.enqueue(new Callback<UserDto>() {
//                        @Override
//                        public void onResponse(Call<UserDto> call, Response<UserDto> response) {
//                            userDto = response.body();
//                            if(userDto.getUserOrOrganization().equals("User")) {
                            Intent intent = new Intent(context, ProfileAll.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("to_user_id", postDto.getUserEmail());
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
//                            }
//                            else {
//                                Intent intent = new Intent(context, ProfileOrganizationAll.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("to_user_id", postDto.getUserEmail());
//                                intent.putExtras(bundle);
//                                context.startActivity(intent);
//                            }
//
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserDto> call, Throwable t) {
//
//
//                        }
//                    });




                        }
                    }
                });

                profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(user_id.equals(postDto.getUserEmail())){

                            Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "eauals"+postDto.getOrganizationEmail());
//
//                    if(userOrOrganization.equals("0")){
                            Intent intent = new Intent(context, Profile.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("to_user_id", postDto.getUserEmail());
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
//                    }else{
//                        Intent intent = new Intent(context, ProfileOrganization.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("to_user_id", postDto.getUserEmail());
//                        intent.putExtras(bundle);
//                        context.startActivity(intent);
//
//                    }

                        }else
                        {
//                    Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "gonot equals "+postDto.getOrganizationEmail());
//                    userApi = rs.getRetrofit().create(UserApi.class);
//                    callGetUser = userApi.getUser(postDto.getUserEmail());
//
//                    callGetUser.enqueue(new Callback<UserDto>() {
//                        @Override
//                        public void onResponse(Call<UserDto> call, Response<UserDto> response) {
//                            userDto = response.body();
//                            if(userDto.getUserOrOrganization().equals("User")) {
                            Intent intent = new Intent(context, ProfileAll.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("to_user_id", postDto.getUserEmail());
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
//                            }
//                            else {
//                                Intent intent = new Intent(context, ProfileOrganizationAll.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("to_user_id", postDto.getUserEmail());
//                                intent.putExtras(bundle);
//                                context.startActivity(intent);
//                            }
//
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserDto> call, Throwable t) {
//
//
//                        }
//                    });




                        }
                    }
                });



                blank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if( blank.getText()==null ||  blank.getText().equals("") ){
                            return;

                        }else{
                            String to_user_id =postDto.getOrganizationEmail();
                            String temp_user_id= user_id;

                            if(to_user_id.equals(temp_user_id)){
                                Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "blank equals"+postDto.getOrganizationEmail());
                                Intent intent = new Intent(context, ProfileOrganization.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("to_user_id", postDto.getOrganizationEmail());
                                intent.putExtras(bundle);
                                context.startActivity(intent);



                            }else{
                                Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "blank not euals "+"("+postDto.getOrganizationEmail()+")"+" "+"("+user_id+")");
                                Intent intent = new Intent(context, ProfileOrganizationAll.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("to_user_id", postDto.getOrganizationEmail());
                                intent.putExtras(bundle);
                                context.startActivity(intent);


                            }


                        }
                    }
                });



                upvotes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "upvote clicked post id: "+postDto.getPostId());

                        Call<List<String>> call=postApi.upvotePost(user_id,postDto.getPostId());
                        call.enqueue(new Callback<List<String>>() {
                            @Override
                            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                                List<String> upvoteResult = response.body();
                                if(upvoteResult.get(0).equals("Upvoted Successfully")){
                                    Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Upvoted Successfully");
                                    upvotes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upvote_true, 0, 0, 0);
                                    Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Ubefore u no "+postDto.getUpvotes().toString());
                                    postDto.setUpvotes(postDto.getUpvotes()+1);
                                    Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Uafter u no "+postDto.getUpvotes().toString());
                                    upvotes.setText(postDto.getUpvotes().toString());

                                }else{
                                    Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Removed Upvote");
                                    upvotes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upvote_false, 0, 0, 0);
                                    Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Ubefore down u no "+postDto.getUpvotes().toString());
                                    postDto.setUpvotes(postDto.getUpvotes()-1);
                                    Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Uafter down u no "+postDto.getUpvotes().toString());
                                    upvotes.setText(postDto.getUpvotes().toString());

                                }

                            }

                            @Override
                            public void onFailure(Call<List<String>> call, Throwable t) {
                                Logger.getLogger(PostViewFull.class.getName()).log(Level.SEVERE, "Error occurred in upvote",t);


                            }
                        });
                    }
                });


                // on below line we are loading our image
                // from url in our image view using picasso.
                // Picasso.get().load(userModal.getAvatar()).into( userIV);




















        }

            @Override
            public void onFailure(Call<PostDto> call, Throwable t) {

            }
        });





    }
}