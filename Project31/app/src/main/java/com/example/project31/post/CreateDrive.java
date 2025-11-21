package com.example.project31.post;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.project31.Dto.DriveDto;
import com.example.project31.Dto.FilterDto;
import com.example.project31.Dto.SectorDto;
import com.example.project31.Dto.LocationDto;
import com.example.project31.MainActivity;
import com.example.project31.R;
import com.example.project31.Utils.Constant;
import com.example.project31.Utils.MapsActivity;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.homePage.HomePage;
import com.example.project31.notification.Notification;
import com.example.project31.profile.Profile;
import com.example.project31.profile.ProfileOrganization;
import com.example.project31.retrofit.DriveApi;
import com.example.project31.retrofit.PostApi;
import com.example.project31.retrofit.RetrofitService;
import com.example.project31.retrofit.SectorApi;
import com.example.project31.retrofit.TextFilterApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
//
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateDrive extends AppCompatActivity {

    EditText txtTime,txtDate;
    ImageView drive_image;
    TextView myImageViewText, tvLocationDisplay;
    Button upload;
    EditText editTextDescription;
    LocationDto locationDto;

    DriveApi specialUploadApi;

    Dialog dialogWait;

    TextView sector_tv;
    Dialog dialog;

    DriveApi driveApi;
    SectorApi sectorApi;
    DriveDto driveDto;

    LocalDate date ;
    LocalDateTime timestamp;
    String userEmail;
    List<String> sectorNames;

    byte[] bitmapdata;
    ProgressBar progressBar;

    Integer fromCamera=0;

    Bitmap tempBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//

        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        setContentView(R.layout.activity_create_drive);

        TextView home=findViewById(R.id.home);
        TextView drives= findViewById(R.id.drives);
        TextView add_post=findViewById(R.id.add_post);
        TextView notification= findViewById(R.id.notification);
        TextView profile_tab= findViewById(R.id.profile_tab);

        userEmail= SharedPreferencesHelper.getInstance(getApplicationContext()).getUserEmail();
        String userOrOrganization = SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateDrive.this, HomePage.class);
                startActivity(intent);
            }
        });
        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateDrive.this, HomePage.class);
                startActivity(intent);;
            }
        });
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPreferencesHelper.getInstance(getApplicationContext()).getOrganization().equals("0")){
                    Intent intent = new Intent(CreateDrive.this, CreateDrive.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(CreateDrive.this, CreateDrive.class);
                    startActivity(intent);
                }
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateDrive.this, Notification.class);
                startActivity(intent);
            }
        });
        profile_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("to_user_id", userEmail);
                if(userOrOrganization.equals("0")){
                    Intent intent = new Intent(CreateDrive.this, Profile.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(CreateDrive.this, ProfileOrganization.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });


        txtTime=(EditText)findViewById(R.id.selectTime);
        txtDate = (EditText)findViewById(R.id.selectDate);
        drive_image = (ImageView)findViewById(R.id.drive_image);
        myImageViewText= (TextView)findViewById(R.id.myImageViewText);

        tvLocationDisplay= (TextView)findViewById(R.id.tvLocationDisplay);

        Context context = getApplicationContext();
        upload = (Button)findViewById(R.id.upload);
        editTextDescription= (EditText) findViewById(R.id.editTextDescription);
        RetrofitService rs = new RetrofitService();
        RetrofitService rsUploadPost = new RetrofitService("Long upload","long upload");
        //sector_spinner =(Spinner) findViewById(R.id.sector_spinner);

        sector_tv=  findViewById(R.id.sector_tv);
        driveApi = rs.getRetrofit().create(DriveApi.class);
        specialUploadApi= rsUploadPost.getRetrofit().create(DriveApi.class);
        sectorApi = rs.getRetrofit().create(SectorApi.class);
        progressBar = findViewById(R.id.progressBar);





        Call<List<String>> call = sectorApi.getSectorNames();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                sectorNames = response.body();
              //  Toast.makeText(getApplicationContext(),"on response",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Logger.getLogger(CreateDrive.class.getName()).log(Level.SEVERE, "Error occurred in spinner",t);
               // Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();

            }
        });



        sector_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize dialog
           //     Toast.makeText(getApplicationContext(),"in sector view"+sectorNames.get(0),Toast.LENGTH_SHORT).show();
                dialog=new Dialog(CreateDrive.this);

                dialog.setContentView(R.layout.dialog_seachable_single_spinner);

                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                Button done = dialog.findViewById(R.id.done);
                // MultiSelectSearchAdapter adapter = new MultiSelectSearchAdapter(getApplicationContext(),R.layout.list_item,sectorNames);
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,sectorNames);
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        sector_tv.setText(adapter.getItem(position));

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });



            }
        });



        ActivityResultLauncher<Intent> onAcitvityResult= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                 //   Toast.makeText(getApplicationContext(),"previous screen",Toast.LENGTH_SHORT).show();
                    Intent intent = result.getData();
                    if (intent.hasExtra("Bitmap")) {
                        fromCamera=1;

                        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("Bitmap");
                        drive_image.setImageBitmap(bitmap);
                        myImageViewText.setVisibility(TextView.GONE);


//                        Matrix matrix = new Matrix();
//                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, 500, 500, matrix, true);

                        tempBitmap=bitmap;
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        bitmapdata = bos.toByteArray();
//                        try {
//                            saveToGallery(bitmap);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                    }
                    if(intent.hasExtra("Uri")){

                        fromCamera=0;

                        progressBar.setVisibility(View.VISIBLE);
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
                                   //     Toast.makeText(getApplicationContext(),"h "+"Failed",Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                 //       Toast.makeText(getApplicationContext(),"h "+"Success",Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                })
                                .into(drive_image);
                        progressBar.setVisibility(View.GONE);
                        Bitmap bitmap = convertURL(Uri,500,500);
                        tempBitmap=bitmap;
                        Boolean isNull= bitmap==null;
                        if(bitmap==null){
                            return;
                        }
                        Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "bitmap " +bitmap+" "+isNull);
                        myImageViewText.setVisibility(TextView.GONE);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        bitmapdata = bos.toByteArray();


            //            Toast.makeText(getApplicationContext(),"done conversions!!!!!",Toast.LENGTH_SHORT).show();

                            }}.start();
                    }

                    if (intent.hasExtra("address")) {
                        Bundle bundle = intent.getExtras();
                        String address = bundle.getString("address");
                        tvLocationDisplay.setText(address);

                    }
                    if (intent.hasExtra("LocationDto")) {

                        locationDto = (LocationDto) intent.getSerializableExtra("LocationDto");
               //         Toast.makeText(getApplicationContext(),"locaton dto"+ locationDto.getLatitude(),Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


//        ActivityResultLauncher<Intent> onAcitvityResultLocation= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                if (result.getResultCode() == Activity.RESULT_OK) {
//                    Toast.makeText(getApplicationContext(),"previous screen",Toast.LENGTH_SHORT).show();
//                    Intent intent = result.getData();
//
//                    if (intent.hasExtra("address")) {
//                        Bundle bundle = intent.getExtras();
//                        String address = bundle.getString("address");
//                        tvLocationDisplay.setText(address);
//
//                    }
//                    if (intent.hasExtra("LocationDto")) {
//
////                        locationDto= (LocationDto) getIntent().getSerializableExtra("LocationDto");
//                        locationDto = (LocationDto) intent.getSerializableExtra("LocationDto");
//                        Toast.makeText(getApplicationContext(),"locaton dto"+ locationDto,Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            }
//        });




        ImageView drive_image = (ImageView) findViewById(R.id.drive_image);
        drive_image.setClickable(true);
        drive_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryScreen = new Intent(CreateDrive.this, Gallery_camera.class);
                //intent.putExtra("DriveDto",driveDto);
//                startActivityF
//                startActivity(intent);
                onAcitvityResult.launch(galleryScreen);
            }
        });

        tvLocationDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationScreen = new Intent(CreateDrive.this, MapsActivity.class);
                onAcitvityResult.launch(locationScreen);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                if(locationDto!=null && locationDto.getLatitude()!=null && locationDto.getLongitude()!=null &&
                        editTextDescription!=null && editTextDescription.getText().length()>0 && sector_tv!=null
                        && bitmapdata!=null)
                {


                }else{

                    Toast.makeText(getApplicationContext(),"Please check for empty fields",Toast.LENGTH_SHORT).show();
                    return;

                }


                if(fromCamera==1){

                    try {
                        saveToGallery(tempBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



       //         Toast.makeText(getApplicationContext(), "in upload" + timestamp, Toast.LENGTH_SHORT).show();
                //new LocationDto(14.365,4578.45)
                driveDto = new DriveDto(locationDto, "" + editTextDescription.getText(), new SectorDto((String) sector_tv.getText()), timestamp, userEmail,null);
                String encodedImage = Base64.getEncoder().encodeToString(bitmapdata);
                driveDto.setStringOfImage(encodedImage);
                //DriveDto driveDto = new DriveDto(null,"hvch",null);

                if (Constant.censoring.equals("yes")){

                    RetrofitService rsNeutrino = new RetrofitService(1);
                TextFilterApi service = rsNeutrino.getRetrofitNeutrino().create(TextFilterApi.class);
                FilterDto filterDto = new FilterDto(driveDto.getDescription(), "*", "strict");

                Call<Object> call = service.filterData(TextFilterApi.filterUserId, TextFilterApi.filterApiKey, filterDto);
                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        // Toast.makeText(getApplicationContext(),"in response"+response.body(),Toast.LENGTH_SHORT).show();
                        // Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "in response ");

                        if (response.isSuccessful() && response.body() != null) {

                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            String json = gson.toJson(response.body());
                            //Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "Response Body "+json);
                            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                            if (jsonObject.has("censored-content")) {
                                String censoredContent = jsonObject.get("censored-content").getAsString();
                                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "json object censored-content " + censoredContent);
                                driveDto.setCensoredDescription(censoredContent);
                                dialogWait = new Dialog(view.getContext());

                                //dialog.setContentView(R.layout.dialog_hq_location);
                                dialogWait.setContentView(R.layout.dialog_wait_drive);

                                dialogWait.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                dialogWait.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogWait.getWindow().setGravity(Gravity.CENTER);
                                dialogWait.show();

                                writeDrive(driveDto);


                            }
                        } else {

                 //           Toast.makeText(getApplicationContext(), " in error null ", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "Error occurred in censoring" + t);
                    }
                });

            }else{

                    Dialog dialog1 = new Dialog(view.getContext());

                    //dialog.setContentView(R.layout.dialog_hq_location);
                    dialog1.setContentView(R.layout.dialog_wait_drive);

                    dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog1.getWindow().setGravity(Gravity.CENTER);
                    dialog1.show();

                  writeDrive(driveDto);
                }










            }
        });






    }

    public void openGallery(View view){
//        Intent intent = new Intent(CreateDrive.this, OpenGallery.class);
//        startActivity(intent);



    }
    public void selectTime(View view){

        if(date==null){
            Toast.makeText(getApplicationContext(),"Please select the date first ",Toast.LENGTH_SHORT).show();
            return;

        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            date = LocalDate.now();
//        }


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(hourOfDay + ":" + minute);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            timestamp= date.atTime(hourOfDay,minute);
                          //  Toast.makeText(getApplicationContext()," "+timestamp,Toast.LENGTH_LONG).show();
                        }
                    }
                }, 24, 60, false);
        timePickerDialog.show();
    }

    public void selectDate(View view){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = LocalDate.now();
        }

        final Calendar c = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                CreateDrive.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        date = LocalDate.of(year, monthOfYear+1, dayOfMonth);

                    }
                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.show();

    }

    public void writeDrive(DriveDto driveDto){
        specialUploadApi.writeDrive(driveDto)
                .enqueue(new Callback<String>() {


                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        //Toast.makeText(MainActivity.this,  "Save Success", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),"Uploaded !",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateDrive.this, ProfileOrganization.class);

                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),  "Please follow app guidelines while posting"+t, Toast.LENGTH_SHORT).show();
                        Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Error occurred",t);
                        dialogWait.dismiss();
                    }
                });

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
//    public Bitmap convertURL(String imgURL)
//    {
//        Bitmap bitmap = null;
//        try {
//            File file = new File(imgURL);
//            FileInputStream stream = new FileInputStream(file);
////            bitmap = BitmapFactory.decodeStream(stream);
////            // Now you have the Bitmap image from the file
//            // Get the orientation information from the image metadata
//            ExifInterface exif = new ExifInterface(imgURL);
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//
//            // Create a Bitmap image from the input stream and apply the orientation
//            bitmap = BitmapFactory.decodeStream(stream);
//            if (orientation != ExifInterface.ORIENTATION_UNDEFINED) {
//                Matrix matrix = new Matrix();
//                switch (orientation) {
//                    case ExifInterface.ORIENTATION_ROTATE_90:
//                        matrix.postRotate(90);
//                        break;
//                    case ExifInterface.ORIENTATION_ROTATE_180:
//                        matrix.postRotate(180);
//                        break;
//                    case ExifInterface.ORIENTATION_ROTATE_270:
//                        matrix.postRotate(270);
//                        break;
//                    default:
//                        break;
//                }
//                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            }
//
//            return bitmap;
//        } catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Null aayi pic");
//        return bitmap;
//    }

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







}

