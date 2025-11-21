package com.socioFix.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.ModerationLabel;
import com.amazonaws.services.rekognition.model.S3Object;

public class ImageAnalyzer 
{

	@Value("${aws.accessKeyId})
	private String accessKeyId;

	@Value("${aws.secretAccessKey})
	private String secretAccessKey;

	private static final String BUCKET = "demo4-s3";
	String new_key =null;

	public String detectLabels(String bucketName, String objectKey) 
	{
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);

		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
			    .withRegion(Regions.US_EAST_2)
			    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
			    .build();
	
//		URI uri=URI.create(objectUrl);
//		System.out.println(uri);
		//String objectKey = uri.getPath().substring(1);
		System.out.println(objectKey);
		String l=null;
		
		try 
		{
			DetectModerationLabelsRequest request = new DetectModerationLabelsRequest()
			        .withImage(new Image().withS3Object(new S3Object().withName(objectKey).withBucket(bucketName)))
			        .withMinConfidence(45F);
			
		    
		    DetectModerationLabelsResult result = rekognitionClient.detectModerationLabels(request);
		    List<ModerationLabel> labels = result.getModerationLabels();
		    System.out.print(labels);
		    
		    for(ModerationLabel label : labels)	
		    {
		    	if(label.getParentName().equals(null) || label.getParentName()==null)
		    		labels.remove(label);
		    }
		    System.out.println("Detected labels for " + objectKey);
		    if (labels.isEmpty()){
		    	new_key = "S"+objectKey;
		    	return new_key;
		    }
		    else 
		    {
			    for (ModerationLabel label : labels) 
				    {
				        System.out.println("Label: " + label.getName()
				                + "\n Confidence: " + label.getConfidence().toString() + "%"
				                + "\n Parent:" + label.getParentName());
	//			        new_key = nameObj(l,labels,objectKey);
	//					return new_key;
				    }
			    
			    Collections.sort(labels, new Comparator<ModerationLabel>() {
		            @Override
		            public int compare(ModerationLabel ml1, ModerationLabel ml2) {
		                if (ml1.getConfidence() > ml2.getConfidence()) {
		                    return -1;
		                } else if (ml1.getConfidence() < ml2.getConfidence()) {
		                    return 1;
		                } else {
		                    return 0;
		                }
		            }
		        });
			    System.out.println(labels);
			    String flag = "Safe";
			    float lowest = labels.get(0).getConfidence() - 15.0f;
			    for(ModerationLabel label : labels)
			    {
			        l = label.getName();
			        if(label.getConfidence() > lowest)
			        {
				        new_key = nameObj(l,labels,objectKey);
				        if(new_key == null || new_key.equals(null))
				        	break;
				        else if (new_key.charAt(0) == 'U')
				        	flag = "UnsafeBlur";
				        else if(new_key.charAt(0) == 'S' && (flag.equals("UnsafeBlur")||flag == "UnsafeBlur"))
				        	flag = "UnsafeBlur";
				        else if(new_key.charAt(0) == 'S' && (!(flag.equals("UnsafeBlur"))||flag == "UnsafeBlur"))
				        	flag = "Safe";
			        }
			        
			    }
			    return new_key;
		    }
			   
		}
		 catch (AmazonRekognitionException e) 
		 {
		    e.printStackTrace();
		 }
		return new_key; 
}

	private String nameObj(String l, List<ModerationLabel> labels, String objectKey) {
		String[] safe = {"Barechested Male","Weapons", "Middle Finger", "Drug Products", "Pills", "Drug Paraphernalia", "Tobacco Products",
	            "Smoking", "Drinking", "Alcoholic Beverages","Female Swimwear Or Underwear", "Male Swimwear Or Underwear",  "Gambling" };
	    String[] unsafeBlur = {"Revealing Clothes", "Air Crash", "Explosions And Blasts", "Nazi Party", "White Supremacy", "Extremist", "Partial Nudity",
	            "Graphic Violence", "Physical Violence", "Self Injury", "Drug Use", "Corpses"};
	    String[] unsafe = {"Nudity", "Graphic Male Nudity", "Graphic Female Nudity", "Sexual Activity", "Illustrated Explicit Nudity","Adult Toys",
	            "Sexual Situations", "Weapon Violence", "Emaciated Bodies", "Hanging"};
	    
	    for (int i = 0; i < unsafe.length; i++) {
            if (l.equals(unsafe[i])) {
                System.out.println("Not possible to save post");
                new_key = null;
                break; // Add break statement here to exit the loop once a match is found
            }
            
            for (int i1 = 0; i1 < unsafeBlur.length; i1++) {
	            if (l.equals(unsafeBlur[i1])) {
	                new_key ="U"+ objectKey;
	                break; // Add break statement here to exit the loop once a match is found
	            }
	        }
            
		 for (int i1 = 0; i1 < safe.length; i1++) {
	            if (l.equals(safe[i1]) || labels.isEmpty()) {
	                new_key  = "S"+objectKey;
	                break;
	            }
	        }
	        }
	      return new_key;  
	}
}


