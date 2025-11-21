package com.example.project31.Utils;

import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project31.Dto.NotificationDisplayDto;
import com.example.project31.Dto.UserDto;
import com.example.project31.R;

import com.example.project31.homePage.HomePage;
import com.example.project31.notification.Notification;
import com.example.project31.post.CreatePost;
import com.example.project31.post.DriveViewFull;
import com.example.project31.post.PostViewFull;
import com.example.project31.profile.ProfileAll;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.profile.ProfileOrganizationAll;
import com.example.project31.retrofit.PostApi;
import com.example.project31.retrofit.UserApi;
import com.example.project31.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    List<NotificationDisplayDto> notificationDisplayDtos;


    UserApi userApi;
    String notificationType;



    // variable for our array list and context.
    //private ArrayList<UserModal> userModalArrayList;
    private Context context;
    String user_id = SharedPreferencesHelper.getInstance(context).getUserEmail();
    String userOrOrganization = SharedPreferencesHelper.getInstance(context).getOrganization();
    RetrofitService rs = new RetrofitService();

    RetrofitService rs1 = new RetrofitService();
    PostApi postApi = rs1.getRetrofit().create(PostApi.class);
    String[] contentsContributeNotification =null;

    Integer isHelp;

    // creating a constructor.
    public NotificationAdapter(List<NotificationDisplayDto> notificationDisplayDtos, Context context,Integer isHelp) {
        this.notificationDisplayDtos = notificationDisplayDtos;
        this.context = context;
        this.isHelp= isHelp;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // getting data from our array list in our modal class.
        //UserModal userModal = userModalArrayList.get(position);
        NotificationDisplayDto notificationDisplayDto= notificationDisplayDtos.get(position);
         //Yutika Patel(^)yutikapatel6june(^)1758963(^)
        // I want to adopt the dog(^)Received help from a user.
        Integer padding =23;
        holder.symbol.setPadding(padding,padding,padding,padding);

        notificationType=notificationDisplayDto.getNotificationType();
        holder.description.setText(notificationDisplayDto.getDescription());
        holder.description.setHint(notificationDisplayDto.getNotificationType());




        if(userOrOrganization.equals("0")){

            if(isHelp==1){

                if(notificationType.equals(Constant.acceptedNotificationType)) {

                    holder.symbol.setImageResource(R.drawable.ic_accepted);


                } else if(notificationType.equals(Constant.solvedNotificationType)) {

                    holder.symbol.setImageResource(R.drawable.ic_solved);


                }else if(notificationDisplayDto.getNotificationType().equals(Constant.contributeNotificationType) ){

                    contentsContributeNotification = notificationDisplayDto.getDescription().split("\\(\\^\\)");

                    Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "kdhcjh"+notificationDisplayDto.getDescription());


                    holder.description.setText("Your post has received help from individual user. Click to check it out!");
                    holder.symbol.setImageResource(R.drawable.ic_help_24);


                }else{
                    Logger.getLogger(NotificationAdapter.class.getName()).log(Level.SEVERE, "No symbol match");
                }


            }else{

                //activity notification

                if(notificationType.equals(Constant.upvoteNotificationType)) {
                    // Handle upvote notification
                    holder.symbol.setImageResource(R.drawable.ic_upvote);

                }
                else if(notificationType.equals(Constant.reminderNotificationType)) {
                    holder.symbol.setImageResource(R.drawable.ic_baseline_drive_24);

                }else{
                    Logger.getLogger(NotificationAdapter.class.getName()).log(Level.SEVERE, "No symbol match");
                }

            }


        }else{
            if(isHelp==1){


                if(notificationDisplayDto.getNotificationType().equals(Constant.contributeNotificationType) ){

                    holder.symbol.setImageResource(R.drawable.ic_help_24);


                } else if(notificationType.equals(Constant.acceptedNotificationType)) {

                    holder.symbol.setImageResource(R.drawable.ic_accepted);


                } else if(notificationType.equals(Constant.toAcceptNotificationType)) {

                    holder.symbol.setImageResource(R.drawable.ic_to_accept);


                }else{
                    Logger.getLogger(NotificationAdapter.class.getName()).log(Level.SEVERE, "No symbol match");
                }

            }else{

                if(notificationType.equals(Constant.upvoteNotificationType)) {
                    // Handle upvote notification
                    holder.symbol.setImageResource(R.drawable.ic_upvote);
                    holder.symbol.setPadding(padding,padding,padding,padding);
                }
                else if(notificationType.equals(Constant.reminderNotificationType)) {
                    holder.symbol.setImageResource(R.drawable.ic_baseline_drive_24);
                    holder.symbol.setPadding(padding,padding,padding,padding);

                }
                else if(notificationType.equals(Constant.followingNotificationOrganizationType)||
                        notificationType.equals(Constant.followingNotificationUserType)) {
                    // Handle following notification for organization
                    holder.symbol.setImageResource(R.drawable.ic_baseline_group_add_24);
                    holder.symbol.setPadding(padding,padding,padding,padding);


                } else if(notificationType.equals(Constant.volunteeredNotificationType)) {
                    holder.symbol.setImageResource(R.drawable.ic_volunteer);
                    holder.symbol.setPadding(padding,padding,padding,padding);
                } else {
                    // Handle other notification types or throw an error
                    Logger.getLogger(NotificationAdapter.class.getName()).log(Level.SEVERE, "No symbol match");

                }

            }

        }


        holder.notificationItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                notificationType =holder.description.getHint().toString();
                Logger.getLogger(NotificationAdapter.class.getName()).log(Level.SEVERE, " is help and userOr org "+
                        isHelp.toString()+userOrOrganization+" "+notificationType);

                if(userOrOrganization.equals("0")){

                    if(isHelp==1){


                        if(notificationType.equals(Constant.acceptedNotificationType)||
                                notificationType.equals(Constant.solvedNotificationType)){

                            Intent intent = new Intent(context, PostViewFull.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("post_id", notificationDisplayDto.getPostId().toString());
                            intent.putExtras(bundle);
                            context.startActivity(intent);

                        }else if(notificationType.equals(Constant.contributeNotificationType)){
                            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "user contri clicked dialog to appear!");
                                this.contributeUser();
                        }else{
                            Logger.getLogger(NotificationAdapter.class.getName()).log(Level.SEVERE, "No on click match");
                        }


                    }else{
                           //user activity on click
                        if(notificationType.equals(Constant.upvoteNotificationType)

                        ){
                            Intent intent = new Intent(context, PostViewFull.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("post_id", notificationDisplayDto.getPostId().toString());
                            intent.putExtras(bundle);
                            context.startActivity(intent);

                        }
                        else  if(notificationType.equals(Constant.reminderNotificationType)){

                            Intent intent = new Intent(context, DriveViewFull.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("drive_id", notificationDisplayDto.getDriveId().toString());
                            intent.putExtras(bundle);
                            context.startActivity(intent);


                        }else{
                            Logger.getLogger(NotificationAdapter.class.getName()).log(Level.SEVERE, "No on click match");
                        }

                    }


                }else{
                    //organization on click notification
                    if(isHelp==1){
//                             All of these
//                             if(notificationType.equals(Constant.acceptedNotificationType)||
//                             notificationType.equals(Constant.toAcceptNotificationType)||
//                             notificationType.equals(Constant.contributeNotificationType)){
//                             }
                        Intent intent = new Intent(context, PostViewFull.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("post_id", notificationDisplayDto.getPostId().toString());
                        intent.putExtras(bundle);
                        context.startActivity(intent);


                    }else{

                        if(notificationType.equals(Constant.followingNotificationOrganizationType))
                        {

                            Intent intent = new Intent(context, ProfileOrganizationAll.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("to_user_id", notificationDisplayDto.getByUserId());
                            intent.putExtras(bundle);
                            context.startActivity(intent);



                        }else if(  notificationType.equals(Constant.followingNotificationUserType)){
                            Intent intent = new Intent(context, ProfileAll.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("to_user_id", notificationDisplayDto.getByUserId());
                            intent.putExtras(bundle);
                            context.startActivity(intent);


                        }else if(notificationType.equals(Constant.volunteeredNotificationType)||notificationType.equals(Constant.reminderNotificationType)) {

                            Intent intent = new Intent(context, DriveViewFull.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("drive_id", notificationDisplayDto.getDriveId().toString());
                            intent.putExtras(bundle);
                            context.startActivity(intent);


                        }else{

                            Logger.getLogger(NotificationAdapter.class.getName()).log(Level.SEVERE, "No on click match");

                        }


                    }

                }




            }

            public void contributeUser(){

                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "In contri user function");
                Dialog dialog=new Dialog(context);

                //dialog.setContentView(R.layout.dialog_hq_location);
                dialog.setContentView(R.layout.dialog_contribute_notification);

                TextView name = dialog.findViewById(R.id.etName);
                TextView emailId = dialog.findViewById(R.id.etEmailAddress);
                TextView mobile =dialog.findViewById(R.id.etMobile);
                TextView helpDescription =dialog.findViewById(R.id.etDescription);
                //description = Yutika Patel*8745693155#I want to adopt the dog.

                contentsContributeNotification = notificationDisplayDto.getDescription().split("\\(\\^\\)");

                Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "here"+notificationDisplayDto.getDescription());


                name.setText(contentsContributeNotification[0]);
                emailId.setText(contentsContributeNotification[1]);
                mobile.setText(contentsContributeNotification[2]);
                helpDescription.setText(contentsContributeNotification[3]);


                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setGravity(Gravity.CENTER);

                dialog.show();

                Button btnAccept = dialog.findViewById(R.id.btnAccept);
                Button btnAcceptAndClose = dialog.findViewById(R.id.btnAcceptAndClose);
                Button btnPost = dialog.findViewById(R.id.btnPost);

                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Great you received some help!");
                        dialog.dismiss();
                    }
                });

                btnAcceptAndClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Post accepted else ");
                        Call<List<String>> call=postApi.solvePost(notificationDisplayDto.getByUserId(),notificationDisplayDto.getPostId());
                        call.enqueue(new Callback<List<String>>() {
                            @Override
                            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                                List<String> acceptedResult = response.body();
                                if(acceptedResult.get(0).equals("Solved")){
                                    Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Solved status- Successfully");
                                    Toast.makeText(context, "Post status updated to solved and post is closed", Toast.LENGTH_SHORT).show();
                                    // Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Post solved by you ");

                                }else{
                                    Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Post wasn't a pending post ");

                                }

                            }

                            @Override
                            public void onFailure(Call<List<String>> call, Throwable t) {
                                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "Error occurred in solving",t);


                            }
                        });
                        dialog.dismiss();

                    }
                });

                btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, PostViewFull.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("post_id", notificationDisplayDto.getPostId().toString());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
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
        return notificationDisplayDtos.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable for our text view and image view.
        private TextView description;
        private ImageView symbol;
        private LinearLayout notificationItemLL;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our variables.
            description = itemView.findViewById(R.id.description);
            notificationItemLL= itemView.findViewById(R.id.notificationItemLL);
            symbol= itemView.findViewById(R.id.symbol);
            userApi = rs.getRetrofit().create(UserApi.class);

        }
    }
}

