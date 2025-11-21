package com.example.project31.retrofit;

import com.example.project31.Dto.FilterDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TextFilterApi {

    String filterApiKey= "NuGlAcziYylhdk5sfhfgoDxBaphhQnWLq78b9vKZTnFKQK8r";

    String filterUserId = "filter" ;

    @POST("bad-word-filter")
    Call<Object> filterData(
            @Query("user-id") String user_id, @Query("api-key") String api_key,@Body FilterDto filterDto
    );
}
