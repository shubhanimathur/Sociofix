package com.example.project31.post;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.project31.Dto.LocationDto;
import com.example.project31.Dto.PostDto;
import com.example.project31.R;


import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Gallery_camera extends AppCompatActivity {

    Button camera;
    ImageView imageView;
    ProgressBar progressBar;
    GridView gridView ;
    Spinner spinnerDirectory;
    ProgressBar mProgressBar;
    TextView Next;
    PostDto postDto;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int  MY_GALLERY_PERMISSION_REQUEST_CODE= 101;
    private static final int NUM_GRID_COLUMNS=3;
    private ArrayList<String> directories;

    private ArrayList<String> directoriesForView;
    private String mAppend ="file:/";
    private String selectedUri="";
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_camera);

        Context contex = this;
        postDto = new PostDto();
        camera = findViewById(R.id.camera);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        gridView = findViewById(R.id.gridView);
        spinnerDirectory = findViewById(R.id.spinnerDirectory);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        Next = findViewById(R.id.tvNext);
        directories = new ArrayList<>();
        directoriesForView = new ArrayList<>();
        ActivityResultLauncher<String> photoFromCamera;


        ActivityResultLauncher<Intent> photoFromCamera2= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
             //   Toast.makeText(getApplicationContext(),"before checking result ok status",Toast.LENGTH_SHORT).show();
                if (result.getResultCode() == RESULT_OK && result.getData() != null){

                //    Toast.makeText(getApplicationContext(),"Aadhi Img set",Toast.LENGTH_SHORT).show();


                    Bitmap photo = (Bitmap) result.getData().getExtras().get("data");

                    Intent intent = new Intent();
                    intent.setClass(Gallery_camera.this, CreatePost.class);
                    intent.putExtra("Bitmap", photo);
                    setResult(Activity.RESULT_OK,intent);
                    finish();



                }
            }
        });





        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                Intent intent = new Intent();
                intent.setClass(Gallery_camera.this, CreatePost.class);
                intent.putExtra("Uri", selectedUri);
                Logger.getLogger(Gallery_camera.class.getName()).log(Level.SEVERE, "select uri " +selectedUri+" ");
            //    Toast.makeText(contex, "clicked next", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK,intent);
                finish();


            }
        });

        camera.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission((Activity)contex,Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity)contex,new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else {
            //        Toast.makeText(getApplicationContext(),"Before launching",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    photoFromCamera2.launch(intent);
              //      Toast.makeText(getApplicationContext(),"Done launching",Toast.LENGTH_SHORT).show();
                    //startActivity(intent);


                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_GALLERY_PERMISSION_REQUEST_CODE);
        }else{

           // Toast.makeText(getApplicationContext(),"have storage access permission",Toast.LENGTH_SHORT).show();
            init();


        }


    }

    private void init(){
        FilePaths filePaths = new FilePaths();
        ArrayList<String> directoriesTemp1= new ArrayList<>();
        ArrayList<String> directoriesTemp2= new ArrayList<>();

        if(FileSearch.getDirectoryPaths(filePaths.PICTURES)!= null){

            directoriesTemp1= FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }
        if(FileSearch.getDirectoryPaths(filePaths.DCIM)!= null){

            directoriesTemp2= FileSearch.getDirectoryPaths(filePaths.DCIM);
        }
        directories.addAll(directoriesTemp1);

        directories.addAll( directoriesTemp2);

        // directories.add(filePaths.CAMERA);

        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  //Clear those directories who have 0 images  1!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        for(String directory : directories){

            directoriesForView.add(directory.substring(directory.lastIndexOf("/") + 1));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,  directoriesForView);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDirectory.setAdapter(adapter);

        spinnerDirectory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,  int position, long id) {
                //   Toast.makeText(getApplicationContext(),"selected : "+directories.get(position),Toast.LENGTH_SHORT).show();
                setupGridView(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void setupGridView(String selectedDirectory) {
        // Toast.makeText(getApplicationContext(),"grid dir : "+selectedDirectory,Toast.LENGTH_SHORT).show();
        //  Log.d(TAG, "setupGridView: directory chosen: " + selectedDirectory);
        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);
        //set the grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(this,R.layout.layout_grid_imageview,mAppend,imgURLs);
        gridView.setAdapter(adapter);

        if(imgURLs.size()>0){
            //  Collections.reverse(imgURLs);
            setImage(imgURLs.get(0), imageView);
            selectedUri=imgURLs.get(0);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //  Log.d(TAG, "onItemClick: selected an image: " + imgURLs.get(position));

                    setImage(imgURLs.get(position), imageView);
                    selectedUri=imgURLs.get(position);
                }
            });
        }else{
            imageView.setImageResource(0);
            Toast.makeText(getApplicationContext(),"Empty directory !",Toast.LENGTH_SHORT).show();
            mProgressBar.setVisibility(View.GONE);
        }



    }

    private void setImage(String imgURL, ImageView image){
        //Log.d(TAG, "setImage: setting image");
       // Toast.makeText(getApplicationContext(),"setting img",Toast.LENGTH_SHORT).show();

        Glide.with(this)
                .load(imgURL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
             //           Toast.makeText(getApplicationContext(),"h "+"Failed",Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                  //      Toast.makeText(getApplicationContext(),"h "+"Success",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .into(imageView);






    }
}