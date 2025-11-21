package com.example.project31.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static SharedPreferencesHelper instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREFERENCE_NAME = "current_user";
    private static final String USER_EMAIL = "user_email";
    private static final String ORGANIZATION="0";


    private SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPreferencesHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesHelper(context);
        }
        return instance;
    }

    public void setUserEmail(String email) {
        editor.putString(USER_EMAIL, email);
        editor.apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(USER_EMAIL, null);
    }

    public void setOrganization(String value) {
        editor.putString(ORGANIZATION, value);
        editor.apply();
    }

    public String getOrganization() {
        return sharedPreferences.getString(ORGANIZATION, null);
    }


    public void removeUser(){
        editor.clear();
        editor.apply();
    }


}
