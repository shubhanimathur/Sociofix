package com.example.project31.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.example.project31.Dto.DriveDto;
import com.example.project31.Dto.OrganizationDto;
import com.example.project31.R;
import com.example.project31.post.CreateDrive;
import com.example.project31.post.DriveViewFull;
import com.example.project31.post.PostViewFull;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.profile.ProfileOrganizationAll;
import com.example.project31.retrofit.DriveApi;
import com.example.project31.retrofit.OrganizationApi;
import com.example.project31.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriveAdapter extends RecyclerView.Adapter<DriveAdapter.ViewHolder> {

    List<DriveDto> driveDtos;
    OrganizationApi organizationApi;
    OrganizationDto organizationDto;
    Call<OrganizationDto> callGetOrganization=null;
    String imagePath="https://demo4-s3.s3.us-east-2.amazonaws.com/";


    // variable for our array list and context.
    //private ArrayList<UserModal> userModalArrayList;
    private Context context;
    String user_id = SharedPreferencesHelper.getInstance(context).getUserEmail();
    String userOrOrganization = SharedPreferencesHelper.getInstance(context).getOrganization();

    // creating a constructor.
    public DriveAdapter(List<DriveDto> driveDtos, Context context) {
        this.driveDtos = driveDtos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.drive_item1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // getting data from our array list in our modal class.
        //UserModal userModal = userModalArrayList.get(position);
        DriveDto driveDto= driveDtos.get(position);

        // on below line we are setting data to our text view.
        holder.name.setText(driveDto.getName());
        String location = driveDto.getLocation().getTaluka()+", "+driveDto.getLocation().getAreaName();
        holder.location.setText(location);
        holder.location.setTextColor(Color.BLUE);

        holder.sector.setText(driveDto.getSector().getSectorId());
        if(driveDto.getWhenAt()!=null){
            int timePosititon = driveDto.getWhenAt().toString().indexOf("T");
            String dateStr = driveDto.getWhenAt().toString().substring(0,timePosititon);
            String timeStr =driveDto.getWhenAt().getHour()+":"+driveDto.getWhenAt().getMinute();
            holder.time.setText(timeStr+" "+dateStr);
        }

        holder.description.setText(driveDto.getDescription());
        holder.upvotes.setText(driveDto.getUpvotes().toString());
        holder.volunteerNo.setText(""+driveDto.getVolunteers().toString()+" volunteers");

        if(driveDto.getMyUpvote()==1)
            holder.upvotes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upvote_true, 0, 0, 0);
        if( driveDto.getMyVolunteerStatus()!= null && driveDto.getMyVolunteerStatus()==1){
            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
            holder.volunteer.setText("Volunteered");
            holder.volunteer.setTextSize(pixels);
        }
        if(user_id.equals(driveDto.getUserEmail())){
            holder.volunteer.setVisibility(View.GONE);
        }
        if(Constant.serverImages==1 && driveDto.getProfileImg()!=null) {
            Glide.with(context).load(imagePath+driveDto.getProfileImg()).into(holder.profile);

        }


        if(Constant.serverImages==1 && driveDto.getImagePath()!=null) {

            if( driveDto.getImagePath().charAt(0)=='U'){

                holder.blurImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(imagePath+driveDto.getImagePath())
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
                        .into(holder.driveImage);
                holder.loadingImg.setVisibility(View.GONE);

            }else{

                Glide.with(context).load(imagePath+driveDto.getImagePath())
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
                        .into(holder.driveImage);
                holder.loadingImg.setVisibility(View.GONE);



            }




        }else{
            holder.driveImage.setImageResource(R.drawable.default_img3);
            holder.loadingImg.setVisibility(View.GONE);
            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "post Image path null");
        }

        RetrofitService rs = new RetrofitService();
        DriveApi driveApi = rs.getRetrofit().create(DriveApi.class);

        String organization= SharedPreferencesHelper.getInstance(context).getOrganization();



        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "save clicked drive id: "+driveDto.getDriveId());

                Call<List<String>> call=driveApi.saveDrive(user_id,driveDto.getDriveId());
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        List<String> saveResult = response.body();
                        if(saveResult.get(0).equals("Saved")){
                            Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Saved Successfully");
                            Toast.makeText(context, "Drive Saved", Toast.LENGTH_SHORT).show();


                        }else{
                            Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Removed saved");
                            Toast.makeText(context, "Drive Unsaved", Toast.LENGTH_SHORT).show();



                        }

                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Error occurred in upvote",t);


                    }
                });

            }
        });
        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToMap3 = new Intent(context, MapsActivity3.class);
                intentToMap3.putExtra("locationDto", driveDto.getLocation());
                context.startActivity(intentToMap3);

            }
        });

        holder.volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "volunteer clicked drive id: "+driveDto.getDriveId());

                Call<List<String>> call=driveApi.volunteerDrive(user_id,driveDto.getDriveId());
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        List<String> volunteerResult = response.body();
                        if(volunteerResult.get(0).equals("Volunteered Successfully")){


                            AlertDialog alert11 = new AlertDialog.Builder(context).setMessage(Html.fromHtml("<i><font color='#808080' size='1'>With each step you take towards making a difference, may your journey be filled with hope and positivity</font>.<br><br> <font color='#808080' size='3'>Have a happy drive!</font></i>")).show();
                            TextView textView = (TextView) alert11.findViewById(android.R.id.message);
                            textView.setTextSize(11);
                            textView.setGravity(Gravity.CENTER);
                            alert11.show();


                            Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Volunteered Successfully");

                            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                            holder.volunteer.setText("Volunteered");
                            holder.volunteer.setTextSize(pixels);
                            holder.volunteer.setPadding(20,10,20,10);
                            holder.volunteer.setBackground(ContextCompat.getDrawable(context, R.drawable.text_view_border));
                            holder.volunteer.setGravity(Gravity.CENTER);

                            float pixels1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                            holder.volunteerNo.setTextSize(pixels1);

                            Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Drive vol before u no "+driveDto.getVolunteers().toString());
                            driveDto.setVolunteers(driveDto.getVolunteers()+1);
                            Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "drive vol no after u no "+driveDto.getVolunteers().toString());
                            holder.volunteerNo.setText(""+driveDto.getVolunteers().toString()+" volunteers");

                        }else{
                            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                            holder.volunteer.setText("Volunteer");
                            holder.volunteer.setTextSize(pixels);
                            holder.volunteer.setPadding(20,10,20,10);
                            holder.volunteer.setBackground(ContextCompat.getDrawable(context, R.drawable.text_view_border2));
                            holder.volunteer.setGravity(Gravity.CENTER);

                            float pixels1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
                            holder.volunteerNo.setTextSize(pixels1);

                            Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Drive vol before u no "+driveDto.getVolunteers().toString());
                            driveDto.setVolunteers(driveDto.getVolunteers()-1);
                            Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "drive vol no after u no "+driveDto.getVolunteers().toString());
                            holder.volunteerNo.setText(""+driveDto.getVolunteers().toString()+" volunteers");

                        }

                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Error occurred in upvote",t);


                    }
                });
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_id.equals(driveDto.getUserEmail())){

                        Intent intent = new Intent(context, ProfileOrganization.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("to_user_id", user_id);
                    intent.putExtras(bundle);
                        context.startActivity(intent);



                }else{


                        Intent intent = new Intent(context, ProfileOrganizationAll.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("to_user_id", driveDto.getUserEmail());
                    intent.putExtras(bundle);
                        context.startActivity(intent);



                }
            }
        });

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_id.equals(driveDto.getUserEmail())){

                        Intent intent = new Intent(context, ProfileOrganization.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("to_user_id", user_id);
                    intent.putExtras(bundle);
                        context.startActivity(intent);


                }else{

                        Intent intent = new Intent(context, ProfileOrganizationAll.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("to_user_id", driveDto.getUserEmail());
                        intent.putExtras(bundle);
                    Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, " goin to profile org all");
              //      Toast.makeText(context, "going to profile org all", Toast.LENGTH_SHORT).show();

                    context.startActivity(intent);


                }
            }
        });



        holder.driveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DriveViewFull.class);
                Bundle bundle = new Bundle();
                bundle.putString("drive_id", driveDto.getDriveId().toString());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.blurImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(context).load(imagePath+driveDto.getImagePath())
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
                        }).into(holder.driveImage);
                holder.loadingImg.setVisibility(View.GONE);
                holder.blurImage.setVisibility(View.GONE);

            }
        });


        holder.upvotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "upvote clicked drive id: "+driveDto.getDriveId());

                Call<List<String>> call=driveApi.upvoteDrive(user_id,driveDto.getDriveId());
                call.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        List<String> upvoteResult = response.body();
                        if(upvoteResult.get(0).equals("Upvoted Successfully")){
                            Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Upvoted Successfully");
                            holder.upvotes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upvote_true, 0, 0, 0);
                            Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Ubefore u no "+driveDto.getUpvotes().toString());
                            driveDto.setUpvotes(driveDto.getUpvotes()+1);
                            Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Uafter u no "+driveDto.getUpvotes().toString());
                            holder.upvotes.setText(driveDto.getUpvotes().toString());

                        }else{
                            Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Removed Upvote");
                            holder.upvotes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upvote_false, 0, 0, 0);
                            Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Ubefore down u no "+driveDto.getUpvotes().toString());
                            driveDto.setUpvotes(driveDto.getUpvotes()-1);
                            Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Uafter down u no "+driveDto.getUpvotes().toString());
                            holder.upvotes.setText(driveDto.getUpvotes().toString());

                        }

                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Logger.getLogger(DriveAdapter.class.getName()).log(Level.SEVERE, "Error occurred in upvote",t);


                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return driveDtos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable for our text view and image view.
        private TextView name, location, sector,time,description,upvotes,save,blank,volunteerNo;
        private ImageView driveImage,profile,blurImage;;
    //    private View accept_reject;

        private Button volunteer;
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
            volunteer= itemView.findViewById(R.id.volunteer);
            driveImage = itemView.findViewById(R.id.driveImage);
      //      accept_reject = itemView.findViewById(R.id.status);
            volunteerNo= itemView.findViewById(R.id.volunteerNo);
            blank= itemView.findViewById(R.id.blank);
            profile= itemView.findViewById(R.id.profile);
            loadingImg= itemView.findViewById(R.id.loadingImg);
            blurImage = itemView.findViewById(R.id.blurImage);

        }
    }
}

