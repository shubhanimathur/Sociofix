package com.example.project31.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project31.MainActivity;
import com.example.project31.R;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.authentication.Cognito;
import com.example.project31.notification.Notification;
import com.example.project31.profile.AcceptedPosts;
import com.example.project31.profile.Profile;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.profile.ProfileOrganizationPostAll;

public class Settings extends AppCompatActivity {

    TextView profileEdit, passwordSecurity,
            permissions, logOut, help,
            about,  yourPosts, savedPost,upvotedPost,faq;

    String userOrOrganization;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_settings);



// Find views by ID

        profileEdit = findViewById(R.id.profile_edit);
        passwordSecurity = findViewById(R.id.password_security);
        permissions = findViewById(R.id.permissions);
        logOut = findViewById(R.id.log_out);

        help = findViewById(R.id.help);
        about = findViewById(R.id.about);
        faq= findViewById(R.id.faq);

        yourPosts = findViewById(R.id.your_posts);
        savedPost=findViewById(R.id.saved_posts);
        upvotedPost=findViewById(R.id.upvoted_posts);

        userOrOrganization = SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization();


        if(userOrOrganization.equals("1")){
            yourPosts.setText("Your Drives");
            savedPost.setText("Your Saved Drives");
             upvotedPost.setText("Your Upvoted Drives");
        }

        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings.this, EditProfileUser.class);
                startActivity(intent);

            }
        });

        passwordSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle passwordSecurity click event
            }
        });


        permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle permissions click event
                Intent intent = new Intent(Settings.this, Permissions.class);
                startActivity(intent);
            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cognito authentication = new Cognito(getApplicationContext());
                authentication.logout();
                SharedPreferencesHelper.getInstance(getApplicationContext()).removeUser();
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);


            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle help click event
                Intent intent = new Intent(Settings.this, SettingsHelp.class);

// start the new activity
                startActivity(intent);
            }
        });


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, AboutUs.class);
                startActivity(intent);
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, FAQ.class);

// start the new activity
                startActivity(intent);
            }
        });


        yourPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle yourPosts click event
                Bundle bundle = new Bundle();
                Intent intent;
                if(userOrOrganization.equals("0")){
                     intent = new Intent(Settings.this, Profile.class);
                    bundle.putString("from_settings", "posts" );
                }else{
                     intent = new Intent(Settings.this, ProfileOrganization.class);
                    bundle.putString("from_settings", "drives" );

                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        savedPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent;
                if(userOrOrganization.equals("0")){
                    intent = new Intent(Settings.this, Profile.class);
                    bundle.putString("from_settings", "saved_posts" );
                }else{
                    intent = new Intent(Settings.this, ProfileOrganization.class);
                    bundle.putString("from_settings", "saved_drives" );

                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        upvotedPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent;
                if(userOrOrganization.equals("0")){
                    intent = new Intent(Settings.this, Profile.class);
                    bundle.putString("from_settings", "upvoted_posts" );
                }else{
                    intent = new Intent(Settings.this, ProfileOrganization.class);
                    bundle.putString("from_settings", "upvoted_drives" );

                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });





    }
}