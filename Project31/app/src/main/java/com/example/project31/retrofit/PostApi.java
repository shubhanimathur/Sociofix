package com.example.project31.retrofit;

import com.example.project31.Dto.LocationDto;
import com.example.project31.Dto.NotificationDto;
import com.example.project31.Dto.PostDto;
import com.example.project31.Dto.TalukaDto;
import com.example.project31.Dto.WinnerDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PostApi {

//    @POST("/post/write")
//    Call<PostDto> writePost(@Body PostDto postDto);

    @Headers("Timeout:240000")
    @POST("/post/write")
    Call<String> writePost(@Body PostDto postDto);

    @Headers("Timeout:240000")
    @POST("/post/writeWithoutImage")
    Call<String> writePostWithoutImage(@Body PostDto postDto);

    @POST("/post/upvote")
    Call<List<String>> upvotePost( @Query("user_id") String user_id, @Query("post_id") Integer post_id);

    @POST("/post/accept")
    Call<List<String>> acceptPost( @Query("organization_id") String organization_id, @Query("post_id") Integer post_id);

    @POST("/post/solve")
    Call<List<String>> solvePost( @Query("organization_id") String organization_id, @Query("post_id") Integer post_id);

    @POST("/post/save")
    Call<List<String>> savePost( @Query("user_id") String user_id, @Query("post_id") Integer post_id);


    @HTTP(method="POST", path="/post/getByLocationAndSector/mostPopular",hasBody=true)
    Call<List<PostDto>> getPostbyLocationAndSectorMostPopular(@Body Map<String, Object> requestBody, @Query("user_id") String user_id,
                                                              @Query("page") Integer page, @Query("size") Integer size);

    @HTTP(method="POST", path="/post/getByLocationAndSector/mostRecent",hasBody=true)
    Call<List<PostDto>> getPostbyLocationAndSectorMostRecent(@Body Map<String, Object> requestBody, @Query("user_id") String user_id,
                                                              @Query("page") Integer page, @Query("size") Integer size);



    @HTTP(method="POST", path="/post/getBySector/mostPopular",hasBody=true)
    Call<List<PostDto>> getPostbySectorMostPopular(@Body Map<String, Object> requestBody, @Query("user_id") String user_id,
                                                   @Query("page") Integer page, @Query("size") Integer size);

    @HTTP(method="POST", path="/post/getBySector/mostRecent",hasBody=true)
    Call<List<PostDto>> getPostbySectorMostRecent(@Body Map<String, Object> requestBody, @Query("user_id") String user_id,
                                                   @Query("page") Integer page, @Query("size") Integer size);

    @HTTP(method="POST", path="/post/getByLocation/mostPopular",hasBody=true)
    Call<List<PostDto>> getPostbyLocationMostPopular(@Body Map<String, Object> requestBody, @Query("user_id") String user_id,
                                                   @Query("page") Integer page, @Query("size") Integer size);

    @HTTP(method="POST", path="/post/getByLocation/mostRecent",hasBody=true)
    Call<List<PostDto>> getPostbyLocationMostRecent(@Body Map<String, Object> requestBody, @Query("user_id") String user_id,
                                                     @Query("page") Integer page, @Query("size") Integer size);

    @HTTP(method="POST", path="/post/getByDefault/mostPopular",hasBody=true)
    Call<List<PostDto>> getPostbyDefaultMostPopular(@Query("user_id") String user_id,
                                                     @Query("page") Integer page, @Query("size") Integer size);

    @HTTP(method="POST", path="/post/getByMyLocality/mostRecent",hasBody=true)
    Call<List<PostDto>> getPostbyMyLocalityMostRecent(@Body LocationDto locationDto, @Query("user_id") String user_id,
                                                      @Query("page") Integer page, @Query("size") Integer size);

    @GET("/post/get")
    Call<PostDto> getPost( @Query("user_id") String user_id, @Query("post_id") Integer post_id);

    // notification calls

    @GET("/post/latest-post-taluka")
    Call<TalukaDto> getLatestPostTaluka(@Query("userEmail") String userEmail);

    @GET("/post/notifications")
    Call<Set<NotificationDto>> getNotificationsForLatestPostByUser(@Query("userEmail") String userEmail);

    @GET("/winner/organizations")
    Call<List<WinnerDto>> getTop10OrganizationsWithSolvedPosts();

    @GET("/winner/users")
    Call<List<WinnerDto>> getTopTenWinnerUsers();

}
