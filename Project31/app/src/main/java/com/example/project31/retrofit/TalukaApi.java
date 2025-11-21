package com.example.project31.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TalukaApi {

    @GET("/taluka/getNames")
    Call<List<String>> getTalukaNames();
}
