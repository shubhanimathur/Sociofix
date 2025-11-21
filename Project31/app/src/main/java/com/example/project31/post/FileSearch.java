package com.example.project31.post;


import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSearch {


    public static ArrayList<String> getDirectoryPaths(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();
        for (int i = 0; i < listfiles.length; i++) {
            if (listfiles[i].isDirectory()) {
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }

    public static ArrayList<String> getFilePaths(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();
        for (int i = listfiles.length-1; i >=0; i--) {

            File temp;
            if (listfiles[i].isFile() ) {
                temp=listfiles[i];
//                Integer position= temp.getAbsolutePath().indexOf('.');
//                String ext = listfiles[i].getAbsolutePath().toString().substring(position+1);
                String extension = MimeTypeMap.getFileExtensionFromUrl(temp.getAbsolutePath());

                if(extension.equalsIgnoreCase("jpg")||
                        extension.equalsIgnoreCase("png")||extension.equalsIgnoreCase("jpeg")) {
                    pathArray.add(temp.getAbsolutePath());
                }
            }
        }
        return pathArray;
    }
}