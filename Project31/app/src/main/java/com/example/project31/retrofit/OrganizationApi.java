package com.example.project31.retrofit;

import com.example.project31.Dto.FollowingUserDto;
import com.example.project31.Dto.OrganizationDto;
import com.example.project31.Dto.PostDto;
import com.example.project31.Dto.UserDto;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrganizationApi {

    @POST("/organization/save")
    Call< OrganizationDto> writeOrganization(@Body OrganizationDto organizationDto);

    @GET("/organization/get")
    Call<OrganizationDto> getOrganization(@Query("organization_id") String organization_id);

    @POST("/organization/followUsers")
    Call<List<String>> followOrganization(@Query("organization_id") String organization_id,@Query("user_id") String user_id);


    @POST("/organization/followUsersStatus")
    Call<List<String>> followOrganizationStatus(@Query("organization_id") String organization_id,@Query("user_id") String user_id);

    @GET("/organization/getFollowingUsers")
    Call<List<FollowingUserDto>> getFollowingUsers(@Query("user_id") String user_id,
                                                           @Query("page") Integer page, @Query("size") Integer size);

    @GET("/organization/getAcceptedPostsNo")
    Call<List<String>> getAcceptedPostsNo(@Query("organization_id") String organization_id);

    @POST("/organization/getOrganizationsByLocationAndSector")
    Call<List<FollowingUserDto>> getOrganizationsByLocationAndSector (@Body Map<String, Object> requestBody,
                                                                      @Query("page") Integer page, @Query("size") Integer size);

    @POST("/organization/getOrganizationsByLocation")
    Call<List<FollowingUserDto>> getOrganizationsByLocation (@Body Map<String, Object> requestBody,
                                                             @Query("page") Integer page, @Query("size") Integer size);

    @POST("/organization/getOrganizationsBySector")
    Call<List<FollowingUserDto>> getOrganizationsBySector (@Body Map<String, Object> requestBody,
                                                           @Query("page") Integer page, @Query("size") Integer size);

    @GET("/organization/getAllOrganizations")
    Call<List<FollowingUserDto>> getAllOrganizations (
            @Query("page") Integer page, @Query("size") Integer size);

    @GET("/organization/getAllOrganizationsBySearch")
    Call<List<FollowingUserDto>> getAllOrganizationsBySearch (@Query("search") String search,
                                                              @Query("page") Integer page, @Query("size") Integer size);






}
