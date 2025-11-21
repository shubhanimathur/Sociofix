package com.socioFix;

import java.net.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.socioFix.Dto.UserDto;
import com.socioFix.Service.PostService;
import com.socioFix.Service.TextFilter;
import com.socioFix.Service.UserService;
import com.socioFix.model.User;

@SpringBootApplication
public class SocioFix1Application {
	
	@Autowired
	private AmazonS3 amazonS3;
	private static final String BUCKET = "demo4-s3";


	public static void main(String[] args) {
		SpringApplication.run(SocioFix1Application.class, args);
		System.out.println("Helluuu");
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			System.out.println(localHost. getHostAddress());
			 LocalDateTime today = LocalDateTime.now();
			 System.out.println(today);

				
			
		}catch(Exception e) {
			
		}
	

		
	
	
		
		
	}
	
	
}
