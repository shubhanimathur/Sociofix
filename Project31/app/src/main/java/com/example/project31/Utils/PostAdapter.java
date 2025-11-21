package com.example.project31.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.project31.Dto.NotificationDto;
import com.example.project31.Dto.PostDto;
import com.example.project31.Dto.UserDto;
import com.example.project31.R;
import com.example.project31.post.CreatePost;
import com.example.project31.post.PostViewFull;
import com.example.project31.profile.Profile;
import com.example.project31.profile.ProfileAll;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.profile.ProfileOrganizationAll;
import com.example.project31.retrofit.NotificationApi;
import com.example.project31.retrofit.PostApi;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.UserApi;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    List<PostDto> postDtos;
    Call<UserDto> callGetUser=null;
    UserDto userDto;

    String imagePath="https://demo4-s3.s3.us-east-2.amazonaws.com/";
    // variable for our array list and context.
    //private ArrayList<UserModal> userModalArrayList;
    private Context context;
    String user_id = SharedPreferencesHelper.getInstance(context).getUserEmail();
    String userOrOrganization = SharedPreferencesHelper.getInstance(context).getOrganization();
    RetrofitService rs = new RetrofitService();
    UserApi userApi;
    UserDto meUserDto;
//    String accessKeyId = "your-access-key-id";
//    String secretAccessKey = "your-secret-access-key";
//    AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
//

    // creating a constructor.
    public PostAdapter(List<PostDto> postDtos, Context context) {
        this.postDtos = postDtos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.post_item1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // getting data from our array list in our modal class.
        //UserModal userModal = userModalArrayList.get(position);
        PostDto postDto= postDtos.get(position);

        // on below line we are setting data to our text view.
        holder.blurImage.setVisibility(View.GONE);
        holder.name.setText(postDto.getName());
        holder.name.setHint(postDto.getUserEmail());
        String location = postDto.getLocation().getTaluka()+", "+postDto.getLocation().getAreaName();
        holder.location.setText(location);
        //AIzaSyCRqlPuvkZKNjNwSJWWKMtywpCt0RDTWHk
        holder.location.setTextColor(Color.BLUE);

        if(Constant.serverImages==1 && postDto.getProfileImg()!=null) {
            Glide.with(context).load(imagePath+postDto.getProfileImg()).into(holder.profile);

        }

        if(Constant.serverImages==1 &&  postDto.getImagePath()!=null) {

            if( postDto.getImagePath().charAt(0)=='U'){

                holder.blurImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(imagePath+postDto.getImagePath())
                        .transform(new BlurTransformation(20, 1))
                        .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.loadingImg.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.loadingImg.setVisibility(View.GONE);
                        return false;
                    }
                })
                        .into(holder.postImage);
                holder.loadingImg.setVisibility(View.GONE);

            }else{

                Glide.with(context)
                        .load(imagePath+postDto.getImagePath())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.loadingImg.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.loadingImg.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.postImage);
                holder.loadingImg.setVisibility(View.GONE);



            }



        }else{
            holder.postImage.setImageResource(R.drawable.default_img3);
            holder.loadingImg.setVisibility(View.GONE);
            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "post Image path null");
        }


holder.blurImage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Glide.with(context).load(imagePath+postDto.getImagePath())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.loadingImg.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.loadingImg.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.postImage);
        holder.loadingImg.setVisibility(View.GONE);
        holder.blurImage.setVisibility(View.GONE);

    }
});

        holder.sector.setText(postDto.getSector().getSectorId());
        if(postDto.getWhenAt()!=null){
            int timePosititon = postDto.getWhenAt().toString().indexOf("T");
            String dateStr = postDto.getWhenAt().toString().substring(0,timePosititon);
            String timeStr =postDto.getWhenAt().getHour()+":"+postDto.getWhenAt().getMinute();
            holder.time.setText(timeStr+" "+dateStr);
        }

        holder.description.setText(postDto.getDescription());
        holder.upvotes.setText(postDto.getUpvotes().toString());
        if(postDto.getMyUpvote()==1)
            holder.upvotes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upvote_true, 0, 0, 0);

        String status = postDto.getStatus();

         if(postDto.getUserEmail().equals(user_id) && postDto.getStatus().equals("Pending")){
            holder.helpOrAccept.setVisibility(View.GONE);
            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Gone "+postDto.getDescription());
        }

        if(status.equals("Pending")) {
            holder.pending.setChecked(true);
            holder.helpOrAccept.setEnabled(true);

        }
        else if(status.equals("Accepted")) {
            holder.accepted.setChecked(true);

                holder.blank.setText(postDto.getOrganizationName() + "- ");
                holder.blank.setHint(postDto.getOrganizationEmail());


            holder.helpOrAccept.setEnabled(false);
            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
            holder.helpOrAccept.setText("Accepted");
            holder.helpOrAccept.setTextSize(pixels);
            holder.helpOrAccept.setPadding(20,10,20,10);
            holder.helpOrAccept.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            holder.helpOrAccept.setBackground(ContextCompat.getDrawable(context, R.drawable.text_view_border));
            holder.helpOrAccept.setGravity(Gravity.CENTER);



        }
        else {
            holder.solved.setChecked(true);
            if(postDto.getOrganizationName()!=null) {
                holder.blank.setText(postDto.getOrganizationName() + "- ");
                holder.blank.setHint(postDto.getOrganizationEmail());
            }else{
                holder.blank.setText("Individual User- ");
                holder.blank.setClickable(false);
            }

//            holder.blank.setText(postDto.getOrganizationName()+"- ");
//            holder.blank.setHint(postDto.getOrganizationEmail());
            holder.helpOrAccept.setClickable(false);
            holder.helpOrAccept.setEnabled(false);
            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
            holder.helpOrAccept.setText("Solved");
            holder.helpOrAccept.setTextSize(pixels);
            holder.helpOrAccept.setPadding(20,10,20,10);
            holder.helpOrAccept.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            holder.helpOrAccept.setBackground(ContextCompat.getDrawable(context, R.drawable.text_view_border));
            holder.helpOrAccept.setGravity(Gravity.CENTER);


        }

        RetrofitService rs = new RetrofitService();
        PostApi postApi = rs.getRetrofit().create(PostApi.class);

        RetrofitService rs1 = new RetrofitService();
        userApi = rs1.getRetrofit().create(UserApi.class);

        String organization= SharedPreferencesHelper.getInstance(context).getOrganization();

        if(organization.equals("0") && postDto.getStatus().equals("Pending")){

            holder.helpOrAccept.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_help, 0, 0, 0);
            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
            holder.helpOrAccept.setTextSize(pixels);

            holder.helpOrAccept.setText("Help");

        }else if(organization.equals("1")  && postDto.getStatus().equals("Pending")){
            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
            holder.helpOrAccept.setText("Accept");
            holder.helpOrAccept.setTextSize(pixels);
            holder.helpOrAccept.setPadding(20,10,20,10);
            holder.helpOrAccept.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            holder.helpOrAccept.setBackground(ContextCompat.getDrawable(context, R.drawable.text_view_border));
            holder.helpOrAccept.setGravity(Gravity.CENTER);

        }

        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToMap3 = new Intent(context, MapsActivity3.class);
                intentToMap3.putExtra("locationDto", postDto.getLocation());
                context.startActivity(intentToMap3);

            }
        });

        holder.helpOrAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(organization.equals("0")){


                       Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "help clicked help box to appear ");
                       Dialog  dialog=new Dialog(context);

                       //dialog.setContentView(R.layout.dialog_hq_location);
                       dialog.setContentView(R.layout.dialog_for_help);



                     dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                     dialog.getWindow().setGravity(Gravity.CENTER);
                       dialog.show();




///
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

                        Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Post accepted else ");
                    Call<List<String>> call=postApi.acceptPost(user_id,postDto.getPostId());
                    call.enqueue(new Callback<List<String>>() {
                        @Override
                        public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                            List<String> acceptedResult = response.body();
                            if(acceptedResult.get(0).equals("Accepted")){
                                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Accepted Successfully");
                                holder.accepted.setChecked(true);
                                holder.pending.setChecked(false);
                                float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());

                                AlertDialog alert11 = new AlertDialog.Builder(context).setMessage(Html.fromHtml("<i><font color='#808080' size='1'>The willingness to help is what makes you special.<br><br>Thank you for your efforts to solve this post!<br>Wishing you a journey filled with growth and impact!</font></i>")).show();
                                TextView textView = (TextView) alert11.findViewById(android.R.id.message);
                                textView.setTextSize(11);
                                textView.setGravity(Gravity.CENTER);
                                alert11.show();


                                holder.helpOrAccept.setText("Accepted");
                                holder.helpOrAccept.setTextSize(pixels);
                                holder.helpOrAccept.setPadding(20,10,20,10);
                                holder.helpOrAccept.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                holder.helpOrAccept.setBackground(ContextCompat.getDrawable(context, R.drawable.text_view_border));
                                holder.helpOrAccept.setGravity(Gravity.CENTER);

                               // holder.helpOrAccept.setVisibility(View.GONE);
                                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Post accepted by you ");

                            }else{
                                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Post wasnt a pending post ");

                            }

                        }

                        @Override
                        public void onFailure(Call<List<String>> call, Throwable t) {
                            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Error occurred in accepted",t);


                        }
                    });





                }


            }
        });

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "save clicked post id: "+postDto.getPostId());

                Call<List<String>> call=postApi.savePost(user_id,postDto.getPostId());
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        List<String> saveResult = response.body();
                        if(saveResult.get(0).equals("Saved")){
                            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Saved Successfully");
                            Toast.makeText(context, "Post Saved", Toast.LENGTH_SHORT).show();


                        }else{
                            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Removed saved");
                            Toast.makeText(context, "Post Unsaved", Toast.LENGTH_SHORT).show();



                        }

                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Error occurred in upvote",t);


                    }
                });

            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_id.equals(postDto.getUserEmail())){

                    Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "eauals"+postDto.getOrganizationEmail());

//                    if(userOrOrganization.equals("0")){
                        Intent intent = new Intent(context, Profile.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("to_user_id", postDto.getUserEmail());
                        intent.putExtras(bundle);
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

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_id.equals(postDto.getUserEmail())){

                    Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "eauals"+postDto.getOrganizationEmail());
//
//                    if(userOrOrganization.equals("0")){
                        Intent intent = new Intent(context, Profile.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("to_user_id", postDto.getUserEmail());
                        intent.putExtras(bundle);
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



        holder.blank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.blank.getText()==null || holder.blank.getText().equals("") ){
                    return;

                }else{
                    String to_user_id =postDto.getOrganizationEmail();
                        String temp_user_id= user_id;

                    if(to_user_id.equals(temp_user_id)){
                        Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "blank equals"+postDto.getOrganizationEmail());
                        Intent intent = new Intent(context, ProfileOrganization.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("to_user_id", postDto.getOrganizationEmail());
                        intent.putExtras(bundle);
                        context.startActivity(intent);



                    }else{
                        Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "blank not euals "+"("+postDto.getOrganizationEmail()+")"+" "+"("+user_id+")");
                        Intent intent = new Intent(context, ProfileOrganizationAll.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("to_user_id", postDto.getOrganizationEmail());
                        intent.putExtras(bundle);
                        context.startActivity(intent);


                    }


                }
            }
        });


holder.postImage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, PostViewFull.class);
        Bundle bundle = new Bundle();
        bundle.putString("post_id", postDto.getPostId().toString());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
});
        holder.upvotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "upvote clicked post id: "+postDto.getPostId());

                Call<List<String>> call=postApi.upvotePost(user_id,postDto.getPostId());
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        List<String> upvoteResult = response.body();
                        if(upvoteResult.get(0).equals("Upvoted Successfully")){
                            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Upvoted Successfully");
                            holder.upvotes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upvote_true, 0, 0, 0);
                            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Ubefore u no "+postDto.getUpvotes().toString());
                            postDto.setUpvotes(postDto.getUpvotes()+1);
                            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Uafter u no "+postDto.getUpvotes().toString());
                            holder.upvotes.setText(postDto.getUpvotes().toString());

                        }else{
                            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Removed Upvote");
                            holder.upvotes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upvote_false, 0, 0, 0);
                            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Ubefore down u no "+postDto.getUpvotes().toString());
                            postDto.setUpvotes(postDto.getUpvotes()-1);
                            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Uafter down u no "+postDto.getUpvotes().toString());
                            holder.upvotes.setText(postDto.getUpvotes().toString());

                        }

                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Error occurred in upvote",t);


                    }
                });
            }
        });


        // on below line we are loading our image
        // from url in our image view using picasso.
       // Picasso.get().load(userModal.getAvatar()).into(holder.userIV);
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return postDtos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable for our text view and image view.
        private TextView name, location, sector,time,description,upvotes,save,blank;
        private ImageView postImage,profile,blurImage;
        private View accept_reject;
        private RadioButton pending, accepted, solved;
        private Button helpOrAccept;

        private ProgressBar loadingImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our variables.
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.location);
            sector = itemView.findViewById(R.id.sector);
            time = itemView.findViewById(R.id.time);
            description = itemView.findViewById(R.id.description);
            upvotes=itemView.findViewById(R.id.upvotes);
            save=itemView.findViewById(R.id.save);
            helpOrAccept= itemView.findViewById(R.id.helpOrAccept);
            postImage = itemView.findViewById(R.id.postImage);
            accept_reject = itemView.findViewById(R.id.status);
            pending= itemView.findViewById(R.id.pending);
            solved= itemView.findViewById(R.id.solved);
            accepted = itemView.findViewById(R.id.accepted);
            blank= itemView.findViewById(R.id.blank);
            profile= itemView.findViewById(R.id.profile);
            loadingImg= itemView.findViewById(R.id.loadingImg);
            blurImage = itemView.findViewById(R.id.blurImage);



        }
    }
}