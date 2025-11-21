package com.example.project31.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project31.Dto.FollowingUserDto;
import com.example.project31.Dto.OrganizationDto;
import com.example.project31.R;
import com.example.project31.profile.Profile;
import com.example.project31.profile.ProfileAll;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.profile.ProfileOrganizationAll;
import com.example.project31.retrofit.OrganizationApi;
import com.example.project31.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingUserAdapter extends RecyclerView.Adapter<FollowingUserAdapter.ViewHolder> {

    List<FollowingUserDto> followingUserDtos;
    OrganizationApi organizationApi;
    OrganizationDto organizationDto;
    Call<OrganizationDto> callGetOrganization=null;
    String to_user_id ="0",to_isOrganization;


    // variable for our array list and context.
    //private ArrayList<UserModal> userModalArrayList;
    private Context context;
    String user_id = SharedPreferencesHelper.getInstance(context).getUserEmail();
    String userOrOrganization = SharedPreferencesHelper.getInstance(context).getOrganization();
    RetrofitService rs = new RetrofitService();



    // creating a constructor.
    public FollowingUserAdapter(List<FollowingUserDto> followingUserDtos, Context context,String to_user_id,String to_isOrganization) {
        this.followingUserDtos = followingUserDtos;
        this.context = context;
        this.to_user_id =to_user_id;
        this.to_isOrganization=to_isOrganization;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.following_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // getting data from our array list in our modal class.
        //UserModal userModal = userModalArrayList.get(position);
        FollowingUserDto followingUserDto= followingUserDtos.get(position);

        // on below line we are setting data to our text view.
        holder.name.setText(followingUserDto.getName());
        holder.name.setHint(followingUserDto.getUserId());
        holder.userId.setText(followingUserDto.getUserId());


        RetrofitService rs = new RetrofitService();

        if((!to_user_id.equals(user_id))||to_isOrganization.equals("1")){
            holder.follow.setVisibility(View.GONE);
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "setting vis gone"+user_id+","+to_user_id);
        }

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(followingUserDto.getUserOrOrganization().equals("Organization")) {
                    if (user_id.equals(followingUserDto.getUserId())) {

                        Intent intent = new Intent(context, ProfileOrganization.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("to_user_id", user_id);
                        intent.putExtras(bundle);
                        context.startActivity(intent);


                    } else {

                        Intent intent = new Intent(context, ProfileOrganizationAll.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("to_user_id", followingUserDto.getUserId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);


                    }
                }else{

                    if (user_id.equals(followingUserDto.getUserId())) {

                        Intent intent = new Intent(context, Profile.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("to_user_id", user_id);
                        intent.putExtras(bundle);
                        context.startActivity(intent);


                    } else {

                        Intent intent = new Intent(context, ProfileAll.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("to_user_id", followingUserDto.getUserId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);


                    }




                }
            }
        });

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_id.equals(followingUserDto.getUserId())){

                    Intent intent = new Intent(context, ProfileOrganization.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("to_user_id", user_id);
                    intent.putExtras(bundle);
                    context.startActivity(intent);


                }else{

                    Intent intent = new Intent(context, ProfileOrganizationAll.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("to_user_id", followingUserDto.getUserId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);


                }
            }
        });



        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<List<String>> call3 = organizationApi.followOrganization(holder.name.getHint().toString(),SharedPreferencesHelper.getInstance(context).getUserEmail());

                call3.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        List<String> result = response.body();

                        if(result.get(0).equals("Followed")){
                     //       Toast.makeText(context, "Followed ", Toast.LENGTH_SHORT).show();

                            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());
                            holder.follow.setText("Following");
                            holder.follow.setTextSize(pixels);
                            holder.follow.setHeight(45);
                            holder.follow.setGravity(Gravity.CENTER);
                           // holder.follow.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.text_view_border));



                        }else {





                            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());
                            holder.follow.setText("Follow");
                            holder.follow.setTextSize(pixels);

                            holder.follow.setHeight(45);

                            holder.follow.setGravity(Gravity.CENTER);



                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "Error occurred in following",t);
                    //    Toast.makeText(context.getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

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
        return followingUserDtos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable for our text view and image view.
        private TextView name,userId;
        private ImageView profile;

        private Button follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our variables.
            name = itemView.findViewById(R.id.name);
            userId = itemView.findViewById(R.id.user_id);

            follow= itemView.findViewById(R.id.follow);

            profile= itemView.findViewById(R.id.profile);
            organizationApi = rs.getRetrofit().create(OrganizationApi.class);
        }
    }
}

