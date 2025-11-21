package com.socioFix.Controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

	
	@Value("${aws.accessKeyId})
	private String accessKeyId;

	@Value("${aws.secretAccessKey})
	private String secretAccessKey;

	
	private final String awsRegion = "us-east-2";
    	private final String awsAccessKeyId = accessKeyId;
    	private final String awsSecretAccessKey = secretAccessKey;


    @Bean
    public AmazonS3 amazonS3Client() {
//        return AmazonS3ClientBuilder.standard()
//                .withCredentials(new DefaultAWSCredentialsProviderChain())
//                .withRegion(Regions.US_EAST_2)
//                .build();
//    }
    	
    	 BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);

         return AmazonS3ClientBuilder.standard()
                 .withRegion(awsRegion)
                 .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                 .build();
     }
}
