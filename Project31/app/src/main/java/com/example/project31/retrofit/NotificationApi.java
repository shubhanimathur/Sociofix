package com.example.project31.retrofit;



import com.example.project31.Dto.NotificationDto;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface NotificationApi {


    @POST("/notification/save")
    Call<String> saveNotification(@Body NotificationDto notificationDto) ;
}



