package com.example.project31.post;

import android.os.Environment;

public class FilePaths {

    //"storage/emutated/@
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

     public String PICTURES = ROOT_DIR + "/Pictures";
    public String DCIM = ROOT_DIR + "/DCIM";

}

