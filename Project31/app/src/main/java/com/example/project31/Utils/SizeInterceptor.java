package com.example.project31.Utils;

import android.util.Log;

import com.example.project31.post.CreatePost;
import com.example.project31.post.Gallery_camera;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
public class SizeInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        long contentLength = response.body().contentLength();

try{
    Request request = chain.request();

    if (request.method().equals("POST") || request.method().equals("PUT") || request.method().equals("PATCH")) {
        long requestLength = request.body().contentLength();
        Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Request size: " + requestLength + " ");

        Buffer buffer = new Buffer();
        request.body().writeTo(buffer);
        String requestBodyString = buffer.readUtf8();
        Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Request size: " + request.body().contentLength() + " ");
        Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Request body detailllll: " + requestBodyString + " ");


        // Parse JSON request body into a map
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> requestMap = objectMapper.readValue(requestBodyString, new TypeReference<Map<String, Object>>(){});

        // Print the size of each field in the JSON request body
        for (String field : requestMap.keySet()) {
            String fieldValue = objectMapper.writeValueAsString(requestMap.get(field));
            long fieldSize = fieldValue.getBytes(StandardCharsets.UTF_8).length;
            Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Size of " + field + ": " + fieldSize + " ");
        }

        Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Request body: " + requestBodyString + " ");






    }




//    long requestLength = request.body().contentLength();
//    Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Requetttttttttt size: " +requestLength+" ");

}catch(Exception e){

    Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "exception ... " +e+" ");
}


        Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Response size: " +contentLength+" ");
        return response;
    }
}