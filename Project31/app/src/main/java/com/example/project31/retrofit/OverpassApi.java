package com.example.project31.retrofit;

import com.example.project31.Dto.Overpass.OverpassResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OverpassApi {
    @GET("interpreter")
    Call<OverpassResponse> getSuburbs(
            @Query("data") String data
    );
}