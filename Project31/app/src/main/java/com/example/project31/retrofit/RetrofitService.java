package com.example.project31.retrofit;

import android.content.Context;

import com.example.project31.Dto.TimeSerialize.LocalDateTimeAdapter;
//import com.example.project31.Dto.TimeSerialize.LocalDateTimeSerializer;

//import com.fasterxml.jackson.core.Version;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.module.SimpleModule;
import com.example.project31.Utils.SharedPreferencesHelper;
import com.example.project31.Utils.SharedPreferencesHelper;
//********
import com.example.project31.Utils.SizeInterceptor;
import com.example.project31.post.CreatePost;
import com.squareup.moshi.Moshi;


import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.OkHttpClient;
import retrofit2.*;

import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitService {
    private Context appContext;

    private Retrofit retrofit;
    private Retrofit retrofitLongTimeOut;

    private Retrofit retrofitOverpass;

    private Retrofit retrofitNeutrino;

    public RetrofitService() {

        inititializeRetrofit();
    }

    public RetrofitService(String overPass){
        inititializeRetrofitOverpass();
    }

    public RetrofitService(String longTimeOut, String postUpload){
        inititializeRetrofitLongTimeOut();
    }

    public RetrofitService(Integer neutrino){inititializeRetrofitNeutrino();}

//    Gson gson = new GsonBuilder()
//            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
//            .create();
//
//    //.addConverterFactory(GsonConverterFactory.create(new Gson()))
//    private void inititializeRetrofit() {
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.57.210:9894")
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//    }

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new SizeInterceptor())
            .build();

// .baseUrl("http://sociofix-env.eba-cjythfgw.ap-south-1.elasticbeanstalk.com/")  .baseUrl("http://192.168.232.210:5000")
    //  .baseUrl("http://sociofix-env.eba-cjythfgw.ap-south-1.elasticbeanstalk.com/")
Moshi moshi = new Moshi.Builder().add(new LocalDateTimeAdapter()).build();
    // .addConverterFactory(MoshiConverterFactory.create(moshi))
    //.addConverterFactory(GsonConverterFactory.create(new Gson()))
    private void inititializeRetrofit() {



        retrofit = new Retrofit.Builder()
                .baseUrl("http://sociofix-env.eba-cjythfgw.ap-south-1.elasticbeanstalk.com/")
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
    }

    //.baseUrl("http://sociofix-env.eba-cjythfgw.ap-south-1.elasticbeanstalk.com/")
    private void inititializeRetrofitLongTimeOut(){


        final OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .readTimeout(240, TimeUnit.SECONDS)
                .connectTimeout(240, TimeUnit.SECONDS)
                .addInterceptor(new SizeInterceptor())
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://sociofix-env.eba-cjythfgw.ap-south-1.elasticbeanstalk.com/")
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(okHttpClient)
                .build();

    }

    private void inititializeRetrofitOverpass() {

        retrofitOverpass = new Retrofit.Builder()
                .baseUrl("https://overpass-api.de/api/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
    }

    public Retrofit getRetrofitNeutrino() {
        return retrofitNeutrino;
    }


    private void inititializeRetrofitNeutrino(){

        retrofitNeutrino = new Retrofit.Builder()
             .baseUrl("https://neutrinoapi.net/")
             .addConverterFactory(MoshiConverterFactory.create(moshi))
             .build();

 }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public Retrofit getRetrofitOverpass() {
        return retrofitOverpass;
    }
}




