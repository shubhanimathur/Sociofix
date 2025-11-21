package com.socioFix.Service;

import java.net.*;
import java.io.*;

public class TextFilterApi {
    public static void main(String[] args) {
        try {
            // Set the URL and the parameters
            String url = "https://neutrinoapi.net/bad-word-filter";
            String userId = "socioFix";
            String apiKey = "0s9gtijLDGZHXdZQ95bfImhlnQAzFKOs9OciKA11iubRMjiK";
            String parameters = "user-id=" + URLEncoder.encode(userId, "UTF-8") + "&api-key=" + URLEncoder.encode(apiKey, "UTF-8") + "&content=" + URLEncoder.encode("Hello dick boy", "UTF-8");
            
            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            
            // Set the request method to POST
            connection.setRequestMethod("POST");
            
            // Set the content type and the content length of the request
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(parameters.length()));
            
            // Allow output from the connection
            connection.setDoOutput(true);
            
            // Write the parameters to the output stream
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(parameters.getBytes());
            outputStream.flush();
            outputStream.close();
            
            // Read the response from the connection
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();
            
            // Print the response
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
