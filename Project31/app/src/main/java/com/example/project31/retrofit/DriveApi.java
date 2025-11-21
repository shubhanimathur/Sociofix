package com.example.project31.retrofit;

import com.example.project31.Dto.DriveDto;
import com.example.project31.Dto.LocationDto;
import com.example.project31.Dto.PostDto;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DriveApi {

//    @POST("/drive/write")
//    Call<DriveDto> writeDrive(@Body DriveDto driveDto);

    @POST("/drive/write")
    Call<String> writeDrive(@Body DriveDto driveDto);

    @POST("/drive/upvote")
    Call<List<String>> upvoteDrive( @Query("user_id") String user_id, @Query("drive_id") Integer drive_id);

    @POST("/drive/volunteer")
    Call<List<String>> volunteerDrive( @Query("user_id") String user_id, @Query("drive_id") Integer drive_id);


    @POST("/drive/save")
    Call<List<String>> saveDrive( @Query("user_id") String user_id, @Query("drive_id") Integer drive_id);

    @HTTP(method="POST", path="/drive/getByLocationAndSector/mostPopular",hasBody=true)
    Call<List<DriveDto>> getDrivebyLocationAndSectorMostPopular(@Body Map<String, Object> requestBody, @Query("user_id") String user_id,
                                                                @Query("page") Integer page, @Query("size") Integer size);

    @HTTP(method="POST", path="/drive/getByLocationAndSector/mostRecent",hasBody=true)
    Call<List<DriveDto>> getDrivebyLocationAndSectorMostRecent(@Body Map<String, Object> requestBody, @Query("user_id") String user_id,
                                                               @Query("page") Integer page, @Query("size") Integer size);



    @HTTP(method="POST", path="/drive/getBySector/mostPopular",hasBody=true)
    Call<List<DriveDto>> getDrivebySectorMostPopular(@Body Map<String, Object> requestBody, @Query("user_id") String user_id,
                                                     @Query("page") Integer page, @Query("size") Integer size);

    @HTTP(method="POST", path="/drive/getBySector/mostRecent",hasBody=true)
    Call<List<DriveDto>> getDrivebySectorMostRecent(@Body Map<String, Object> requestBody, @Query("user_id") String user_id,
                                                    @Query("page") Integer page, @Query("size") Integer size);

    @HTTP(method="POST", path="/drive/getByLocation/mostPopular",hasBody=true)
    Call<List<DriveDto>> getDrivebyLocationMostPopular(@Body Map<String, Object> requestBody, @Query("user_id") String user_id,
                                                       @Query("page") Integer page, @Query("size") Integer size);

    @HTTP(method="POST", path="/drive/getByLocation/mostRecent",hasBody=true)
    Call<List<DriveDto>> getDrivebyLocationMostRecent(@Body Map<String, Object> requestBody, @Query("user_id") String user_id,
                                                      @Query("page") Integer page, @Query("size") Integer size);

    @HTTP(method="POST", path="/drive/getByDefault/mostPopular",hasBody=true)
    Call<List<DriveDto>> getDrivebyDefaultMostPopular(@Query("user_id") String user_id,
                                                      @Query("page") Integer page, @Query("size") Integer size);

    @HTTP(method="POST", path="/drive/getByMyLocality/mostRecent",hasBody=true)
    Call<List<DriveDto>> getDrivebyMyLocalityMostRecent(@Body LocationDto locationDto, @Query("user_id") String user_id,
                                                      @Query("page") Integer page, @Query("size") Integer size);

    @GET("/drive/get")
    Call<DriveDto> getDrive(@Query("user_id") String user_id, @Query("drive_id") Integer drive_id);


}

