package com.example.project31.Utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

import com.bumptech.glide.Glide;
import com.example.project31.Dto.PostDto;
import com.example.project31.Dto.UserDto;
import com.example.project31.R;
import com.example.project31.post.PostViewFull;
import com.example.project31.profile.Profile;
import com.example.project31.profile.ProfileAll;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.retrofit.PostApi;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.UserApi;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptPostAdapter extends RecyclerView.Adapter<AcceptPostAdapter.ViewHolder> {

    List<PostDto> postDtos;
    Call<UserDto> callGetUser=null;
    UserDto userDto;
    // variable for our array list and context.
    //private ArrayList<UserModal> userModalArrayList;
    private Context context;

    private Activity activity;
    String user_id = SharedPreferencesHelper.getInstance(context).getUserEmail();
    String userOrOrganization = SharedPreferencesHelper.getInstance(context).getOrganization();
    RetrofitService rs = new RetrofitService();
    UserApi userApi;

    String imagePath="https://demo4-s3.s3.us-east-2.amazonaws.com/";

    // creating a constructor.
    public AcceptPostAdapter(List<PostDto> postDtos, Context context,Activity activity) {
        this.postDtos = postDtos;
        this.context = context;
        this.activity=activity;
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
        holder.name.setText(postDto.getName());
        holder.name.setHint(postDto.getUserEmail());
        String location = postDto.getLocation().getTaluka()+", "+postDto.getLocation().getAreaName();
        holder.location.setText(location);
        holder.location.setTextColor(Color.BLUE);

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

        if(!postDto.getOrganizationEmail().equals(user_id) ){
            holder.helpOrAccept.setVisibility(View.GONE);
            Logger.getLogger(AcceptPostAdapter.class.getName()).log(Level.SEVERE, "Gone "+postDto.getDescription());
        }

        if(Constant.serverImages==1 && postDto.getImagePath()!=null) {

            if( postDto.getImagePath().charAt(0)=='U'){

                holder.blurImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(imagePath+postDto.getImagePath())
                        .transform(new BlurTransformation(20, 1))
                        .into(holder.postImage);
                holder.loadingImg.setVisibility(View.GONE);

            }else{

                Glide.with(context).load(imagePath+postDto.getImagePath()).into(holder.postImage);
                holder.loadingImg.setVisibility(View.GONE);



            }

        }else{
            holder.postImage.setImageResource(R.drawable.default_img3);
            holder.loadingImg.setVisibility(View.GONE);
            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "post Image path null or serverImages=0");
        }

        if(Constant.serverImages==1 && postDto.getProfileImg()!=null) {
            Glide.with(context).load(imagePath+postDto.getProfileImg()).into(holder.profile);

        }

        holder.accepted.setChecked(true);
            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
            holder.helpOrAccept.setText("Solve");
            holder.helpOrAccept.setTextSize(pixels);
            holder.helpOrAccept.setPadding(20,10,20,10);
            holder.helpOrAccept.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            holder.helpOrAccept.setBackground(ContextCompat.getDrawable(context, R.drawable.text_view_border));
            holder.helpOrAccept.setGravity(Gravity.CENTER);


        RetrofitService rs = new RetrofitService();
        PostApi postApi = rs.getRetrofit().create(PostApi.class);

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




                    Logger.getLogger(AcceptPostAdapter.class.getName()).log(Level.SEVERE, "solve clicked");


                    // rest api call to update post status to solved

                    Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Post solved calling ");
                    Call<List<String>> call=postApi.solvePost(user_id,postDto.getPostId());
                    call.enqueue(new Callback<List<String>>() {
                        @Override
                        public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                            List<String> acceptedResult = response.body();
                            if(acceptedResult.get(0).equals("Solved")){
                                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Solved Successfully");
                                holder.solved.setChecked(true);
                                holder.accepted.setChecked(false);


                                float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                                holder.helpOrAccept.setText("Solved");
                                holder.helpOrAccept.setTextSize(pixels);
                                holder.helpOrAccept.setEnabled(false);
                                holder.helpOrAccept.setPadding(20,10,20,10);
                                holder.helpOrAccept.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                holder.helpOrAccept.setBackground(ContextCompat.getDrawable(context, R.drawable.text_view_border));
                                holder.helpOrAccept.setGravity(Gravity.CENTER);

                                // holder.helpOrAccept.setVisibility(View.GONE);
                                Logger.getLogger(AcceptPostAdapter.class.getName()).log(Level.SEVERE, "Post solved by you ");

                                AlertDialog alert11 = new AlertDialog.Builder(activity).setMessage(Html.fromHtml("<i><font color='#808080' size='1'>You're doing an amazing job!<br><br>Keep up the fantastic work of making the world a better place!.</font></i>")).show();
                                TextView textView = (TextView) alert11.findViewById(android.R.id.message);
                                textView.setTextSize(11);
                                textView.setGravity(Gravity.CENTER);
                                alert11.show();

                            }else{
                                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Post solving mai error ");

                            }

                        }

                        @Override
                        public void onFailure(Call<List<String>> call, Throwable t) {
                            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Error occurred in accepted",t);


                        }
                    });








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