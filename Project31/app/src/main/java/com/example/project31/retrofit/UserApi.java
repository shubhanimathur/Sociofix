package com.example.project31.retrofit;

import com.example.project31.Dto.DriveDto;
import com.example.project31.Dto.FollowingUserDto;
import com.example.project31.Dto.NotificationDisplayDto;
import com.example.project31.Dto.NotificationDto;
import com.example.project31.Dto.OrganizationDto;
import com.example.project31.Dto.PostDto;
import com.example.project31.Dto.UserDto;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {


    @POST("/user/save")
    Call<UserDto> writeUser(@Body UserDto userDto);

    @GET("/user/getUserPosts")
    Call<List<PostDto>> getUserPosts(@Query("user_id") String user_id,@Query("view_user_id") String view_user_id,
                                     @Query("page") Integer page, @Query("size") Integer size);

    @GET("/user/getUserUpvotedPosts")
    Call<List<PostDto>> getUserUpvotedPosts(@Query("user_id") String user_id,
                                     @Query("page") Integer page, @Query("size") Integer size);
    @GET("/user/getUserSavedPosts")
    Call<List<PostDto>> getUserSavedPosts(@Query("user_id") String user_id,
                                     @Query("page") Integer page, @Query("size") Integer size);

    @GET("/user/getUserSolvedPosts")
    Call<List<PostDto>> getUserSolvedPosts(@Query("user_id") String user_id,@Query("view_user_id") String view_user_id,
                                          @Query("page") Integer page, @Query("size") Integer size);

    @GET("/user/getUserAcceptedPosts")
    Call<List<PostDto>> getUserAcceptedPosts(@Query("user_id") String user_id,@Query("view_user_id") String view_user_id,
                                           @Query("page") Integer page, @Query("size") Integer size);



    @GET("/user/get")
    Call<UserDto> getUser(@Query("user_id") String user_id);

    @GET("/user/getUserDrives")
    Call<List<DriveDto>> getUserDrives(@Query("user_id") String user_id,@Query("view_user_id") String view_user_id,
                                       @Query("page") Integer page, @Query("size") Integer size);

    @GET("/user/getUserUpvotedDrives")
    Call<List<DriveDto>> getUserUpvotedDrives(@Query("user_id") String user_id,
                                              @Query("page") Integer page, @Query("size") Integer size);
    @GET("/user/getUserSavedDrives")
    Call<List<DriveDto>> getUserSavedDrives(@Query("user_id") String user_id,
                                            @Query("page") Integer page, @Query("size") Integer size);


    @GET("/user/getUserVolunteeredDrives")
    Call<List<DriveDto>> getUserVolunteeredDrives(@Query("user_id") String user_id,@Query("view_user_id") String view_user_id,
                                            @Query("page") Integer page, @Query("size") Integer size);


    @POST("/constant/censoring")
    Call<List<String>> censoringState( @Query("censoring_state") String censoring_state);

    @GET("/user/getFollowingOrganizations")
    Call<List<FollowingUserDto>> getFollowingOrganizations(@Query("user_id") String user_id,
                                                           @Query("page") Integer page, @Query("size") Integer size);

    @GET("/user/getDrivesForFollowedOrganizations")
    Call<List<DriveDto>>  getDrivesForFollowedOrganizations(@Query("user_id") String user_id,@Query("view_user_id") String view_user_id,
                                                            @Query("page") Integer page, @Query("size") Integer size) ;

    @GET("/user/getNotifications")
    Call<List<NotificationDisplayDto>> getNotifications (@Query("user_id") String user_id,
                                                         @Query("page") Integer page, @Query("size") Integer size);

    @GET("/user/getNotificationsUserHelp")
    Call<List<NotificationDisplayDto>> getNotificationsUserHelp (@Query("user_id") String user_id,
                                                         @Query("page") Integer page, @Query("size") Integer size);
    @GET("/user/getNotificationsUserActivity")
    Call<List<NotificationDisplayDto>> getNotificationsUserActivity (@Query("user_id") String user_id,
                                                         @Query("page") Integer page, @Query("size") Integer size);
    @GET("/user/getNotificationsOrganizationHelp")
    Call<List<NotificationDisplayDto>> getNotificationsOrganizationHelp (@Query("user_id") String user_id,
                                                         @Query("page") Integer page, @Query("size") Integer size);
    @GET("/user/getNotificationsOrganizationActivity")
    Call<List<NotificationDisplayDto>> getNotificationsOrganizationActivity (@Query("user_id") String user_id,
                                                         @Query("page") Integer page, @Query("size") Integer size);


    @POST("/user/edit")
    Call<UserDto> editUser(@Body UserDto userDto);



    }
