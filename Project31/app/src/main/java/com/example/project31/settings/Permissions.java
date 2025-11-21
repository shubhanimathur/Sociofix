package com.example.project31.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.example.project31.R;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class Permissions extends AppCompatActivity {

    private Switch notificationSwitch, cameraSwitch, gallerySwitch, locationSwitch;


    private static final int CAMERA_PERMISSION_CODE = 2;
    private static final int GALLERY_PERMISSION_CODE = 3;
    private static final int LOCATION_PERMISSION_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_permissions);


        cameraSwitch = findViewById(R.id.camera_switch);
        gallerySwitch = findViewById(R.id.gallery_switch);
        locationSwitch = findViewById(R.id.location_switch);

        // set switch state based on permission status
        notificationSwitch.setChecked(checkPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY));
        cameraSwitch.setChecked(checkPermission(Manifest.permission.CAMERA));
        gallerySwitch.setChecked(checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE));
        locationSwitch.setChecked(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION));



        cameraSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    grantPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                } else {
                    revokePermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                    // revokePermission(Manifest.permission.CAMERA);
                }
            }
        });

        gallerySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    grantPermission(Manifest.permission.READ_EXTERNAL_STORAGE, GALLERY_PERMISSION_CODE);
                } else {
                    revokePermission(Manifest.permission.READ_EXTERNAL_STORAGE, GALLERY_PERMISSION_CODE);
                    //revokePermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });

        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    grantPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_CODE);
                } else {
                    revokePermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_CODE);
                    //revokePermission(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }
        });

    }

    private boolean checkPermission(String permission) {
        int result = ActivityCompat.checkSelfPermission(this, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void grantPermission(String permission, int requestCode) {
        if (!checkPermission(permission)) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

//    private void revokePermission(String permission) {
//        PackageManager pm = getPackageManager();
//        String packageName = getPackageName();
//        int permissionStatus = pm.checkPermission(permission, packageName);
//        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
//            pm.removePermission(permission);
//            Toast.makeText(this, "Permission revoked", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Permission is not granted", Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void revokePermission(String permission) {
//        if (checkPermission(permission)) {
//            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
//        }
//    }

    private void revokePermission(String permission, int requestCode) {
        if (checkPermission(permission)) {
            Toast.makeText(this, "Go to you mobile's settings to revoke permissions", Toast.LENGTH_SHORT).show();
            //ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

            switch (requestCode) {

                case CAMERA_PERMISSION_CODE:
                    cameraSwitch.setChecked(true);
                    break;

                case GALLERY_PERMISSION_CODE:
                    //Toast.makeText(this, "++++++++++++", Toast.LENGTH_SHORT).show();
                    gallerySwitch.setChecked(true);
                    break;

                case LOCATION_PERMISSION_CODE:
                    locationSwitch.setChecked(true);
                    break;


            }
        }
    }

//    private void revokePermission(String permission) {
//        if (checkPermission(permission)) {
//            PackageManager pm = getPackageManager();
//            String packageName = getPackageName();
//            pm.revokeRuntimePermission(packageName, permission, null);
//        }
//    }

//    private void revokePermission(String permission, int requestCode) {
//        if (checkPermission(permission)) {
//            PackageManager pm = getPackageManager();
//            String packageName = getPackageName();
//            pm.revokeRuntimePermission(permission, packageName);
//        }








    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Toast.makeText(this, "Going to switch", Toast.LENGTH_SHORT).show();
        switch (requestCode) {

            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
                    cameraSwitch.setChecked(true);
                } else {
                    cameraSwitch.setChecked(false);
                }
                break;

            case GALLERY_PERMISSION_CODE:
                //Toast.makeText(this, "++++++++++++", Toast.LENGTH_SHORT).show();
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "In if******************", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Gallery permission granted", Toast.LENGTH_SHORT).show();
                    gallerySwitch.setChecked(true);
                } else {
                    Toast.makeText(this, "In else", Toast.LENGTH_SHORT).show();
                    gallerySwitch.setChecked(false);
                }
                break;

            case LOCATION_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                    locationSwitch.setChecked(true);
                } else {
                    locationSwitch.setChecked(false);
                }
                break;


        }
    }
}

