package com.example.project31.retrofit;

import com.example.project31.Dto.PostDto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SectorApi {

    @GET("/sector/getNames")
    Call<List<String>> getSectorNames();

    
}
