package com.example.project31.settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.project31.Dto.LocationDto;
import com.example.project31.Dto.UserDto;
import com.example.project31.R;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.post.CreateDrive;
import com.example.project31.post.CreatePost;
import com.example.project31.post.Gallery_camera;
import com.example.project31.profile.Profile;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.retrofit.NotificationApi;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.UserApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileUser extends AppCompatActivity {

    EditText etName;
    EditText etBio;
    EditText etContactNo;
    TextView myImageViewText;

    Button btnSave;

    ImageView profile;
    byte[] bitmapdata;
    String userOrOrganization;
    String user_id;
    UserDto userDto;
    UserApi userApi;

    Integer fromCamera=0;

    Bitmap tempBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_edit_profile_user);
        Logger.getLogger(EditProfileUser.class.getName()).log(Level.SEVERE, "In edit profile user activity");

        Call<UserDto> callGetUser=null;

        RetrofitService rs = new RetrofitService();
        userApi = rs.getRetrofit().create(UserApi.class);

        user_id=SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();

        callGetUser = userApi.getUser(user_id);



        callGetUser.enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                userDto = response.body();
                if(userDto.getName()!=null) etName.setText(userDto.getName());
                if(userDto.getBio()!=null) etBio.setText(userDto.getBio());
                if(userDto.getContactNo()!=null) etContactNo.setText(userDto.getContactNo());
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, "Error occurred in user data fetch",t);
     //           Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

            }
        });

        userOrOrganization = SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization();
        user_id= SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();

        etName = findViewById(R.id.etName);
        etBio = findViewById(R.id.etBio);
        etContactNo = findViewById(R.id.etContactNo);
        btnSave = findViewById(R.id.btnSave);
        profile= findViewById(R.id.profile);
        myImageViewText= (TextView)findViewById(R.id.myImageViewText);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userDto = new UserDto();
                userDto.setUserId(SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail());
                userDto.setName(etName.getText().toString());
                userDto.setContactNo(etContactNo.getText().toString());
                userDto.setBio(etBio.getText().toString());
//                userDto.setByteImage(bitmapdata);
                if(bitmapdata!=null) {
                    String encodedImage = Base64.getEncoder().encodeToString(bitmapdata);
                    userDto.setStringOfImage(encodedImage);
                }

                if(fromCamera==1){

                    try {
                        saveToGallery(tempBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                RetrofitService rsLong = new RetrofitService("Long call","Long upload");
                UserApi userApi = rsLong.getRetrofit().create(UserApi.class);

                userApi.editUser(userDto).enqueue(new Callback<UserDto>() {
                    @Override
                    public void onResponse(Call<UserDto> call, Response<UserDto> response) {
             //           Toast.makeText(getApplicationContext(),  "User profile edited", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(EditProfileUser.class.getName()).log(Level.SEVERE, "User profile edited");
                        Bundle bundle = new Bundle();
                        bundle.putString("to_user_id", userDto.getUserId());
                        if(userOrOrganization.equals("0")){
                            Intent intent = new Intent(EditProfileUser.this, Profile.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(EditProfileUser.this, ProfileOrganization.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<UserDto> call, Throwable t) {

                    }
                });


                finish();
            }

        });

        ActivityResultLauncher<Intent> onAcitvityResult= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
               //     Toast.makeText(getApplicationContext(),"previous screen",Toast.LENGTH_SHORT).show();
                    Intent intent = result.getData();
                    if (intent.hasExtra("Bitmap")) {
                        fromCamera=1;

                        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("Bitmap");
                        bitmap = scaleBitmap(bitmap);
                        profile.setImageBitmap(bitmap);
                        myImageViewText.setVisibility(TextView.GONE);

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        bitmapdata = bos.toByteArray();
                        tempBitmap= bitmap;

                    }
                    if(intent.hasExtra("Uri")){

                        fromCamera=0;
                        new CountDownTimer(1000, 1000) {
                            public void onTick(long millisUntilFinished) {


                            }
                            public void onFinish() {

                                Bundle bundle = intent.getExtras();
                                String Uri = bundle.getString("Uri");

                                myImageViewText.setVisibility(TextView.GONE);
                                Glide.with(getApplicationContext())
                                        .load(Uri)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                      //          Toast.makeText(getApplicationContext(),"h "+"Failed",Toast.LENGTH_SHORT).show();
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                           //     Toast.makeText(getApplicationContext(),"h "+"Success",Toast.LENGTH_SHORT).show();
                                                return false;
                                            }
                                        })
                                        .into(profile);

                                Bitmap bitmap = convertURL(Uri,500,500);
                                Boolean isNull= bitmap==null;
                                if(bitmap==null){
                                    return;
                                }
                                tempBitmap= bitmap;
                                Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "bitmap " +bitmap+" "+isNull);
                                myImageViewText.setVisibility(TextView.GONE);
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                bitmapdata = bos.toByteArray();

                //                Toast.makeText(getApplicationContext(),"done conversions!!!!!",Toast.LENGTH_SHORT).show();




                            }}.start();



                    }

                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryScreen = new Intent(EditProfileUser.this, Gallery_camera.class);
                //intent.putExtra("DriveDto",driveDto);
//                startActivityF
//                startActivity(intent);
                onAcitvityResult.launch(galleryScreen);
            }
        });



        //finish();
    }

    private Bitmap scaleBitmap(Bitmap bitmap) {
// Get the width and height of the captured image
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();

// Get the width and height of the ImageView
        int imageViewWidth = profile.getWidth();
        // int imageViewHeight = post_image.getHeight();
        int imageViewHeight =  800;

// Calculate the aspect ratio of the image and the ImageView
        float imageAspectRatio = (float) imageWidth / (float) imageHeight;
        float imageViewAspectRatio = (float) imageViewWidth / (float) imageViewHeight;

// Calculate the scale factor to fit the image in the ImageView without stretching it
        float scaleFactor;
        if (imageAspectRatio > imageViewAspectRatio) {
            // The image is wider than the ImageView, so scale to fit the width
            scaleFactor = (float) imageViewWidth / (float) imageWidth;
        }
        else
        {
            // The image is taller than the ImageView, so scale to fit the height
            scaleFactor = (float) imageViewHeight / (float) imageHeight;
        }

// Calculate the new width and height of the image after scaling
        int newImageWidth = (int) (imageWidth * scaleFactor);
        int newImageHeight = (int) (imageHeight * scaleFactor);

// Create a new Bitmap object with the scaled size
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newImageWidth, newImageHeight, true);

// Calculate the width and height of the side borders
        int borderWidth = (imageViewWidth - newImageWidth) / 2;
        int borderHeight = (imageViewHeight - newImageHeight) / 2;

// Create a new Bitmap object with the side borders
        Bitmap borderedBitmap = Bitmap.createBitmap(imageViewWidth, imageViewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(borderedBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(scaledBitmap, borderWidth, borderHeight, null);

        return borderedBitmap;
        // return  scaledBitmap;
//// Set the bordered Bitmap to the ImageView
//        imageView.setImageBitmap(borderedBitmap);
    }



    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public Bitmap convertURL(String imgURL, int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeSampledBitmapFromFile(imgURL, reqWidth, reqHeight);
            // Get the orientation information from the image metadata
            ExifInterface exif = new ExifInterface(imgURL);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            // Apply the orientation to the bitmap
            if (orientation != ExifInterface.ORIENTATION_UNDEFINED) {
                Matrix matrix = new Matrix();
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                    default:
                        break;
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void saveToGallery(Bitmap bitmap) throws IOException {

// Set the file name and path
//        String fileName = UserDto.class.getName()+".jpg";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = ""+timeStamp+"image.png";

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +timeStamp+"/" +fileName;

// Create the content values object and set the file attributes
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "My Image");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATA, filePath);

// Insert the image into the media store
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

// Open the output stream and compress the image
        OutputStream outputStream = getContentResolver().openOutputStream(uri);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

// Close the output stream
        outputStream.close();
        System.out.println("Saved to Gallery");
        Toast.makeText(getApplicationContext(),"Saved to Gallery!",Toast.LENGTH_SHORT).show();
    }





}

