package com.example.project31.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.project31.R;

public class FAQ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_faq);
    }
}