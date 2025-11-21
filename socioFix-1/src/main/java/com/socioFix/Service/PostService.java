package com.socioFix.Service;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socioFix.Dto.LocationDto;
import com.socioFix.Dto.NotificationDto;
import com.socioFix.Dto.PostDto;
import com.socioFix.Dto.TalukaDto;
import com.socioFix.Dto.WinnerDto;

import com.socioFix.model.Location;
import com.socioFix.model.Organization;
import com.socioFix.model.Post;
import com.socioFix.model.Sector;
import com.socioFix.model.User;
import com.socioFix.model.Notification.Notification;
import com.socioFix.model.geoLocations.Area;
import com.socioFix.model.geoLocations.Taluka;
import com.socioFix.repository.NotificationRepository;
import com.socioFix.repository.OrganizationRepository;
import com.socioFix.repository.PostRepository;
import com.socioFix.repository.SectorRepository;
import com.socioFix.repository.UserRepository;


@Service
public class PostService {

	@Autowired
	PostRepository postRepository;
	
	@Autowired 
	SectorRepository sectorRepository;
	
	@Autowired 
	UserRepository userRepository;
	
	@Autowired 
	NotificationRepository notificationRepository;
	
	@Autowired
	OrganizationRepository organizationRepository;
	
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	AreaService areaService;
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	TalukaService talukaService;
	
	@Autowired
	private AmazonS3 amazonS3;
	private static final String BUCKET = "demo4-s3";
	
	@Value(${aws.accessKeyId})
	private String accessKeyId;
	
	@Value(${aws.secretAccessKey})
	private String secretAccessKey;

	BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
	AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(Regions.US_EAST_2).build();

	String key="";
	String imagePath="https://demo4-s3.s3.us-east-2.amazonaws.com/";
//	@Autowired
//	DomainMapper domainMapper;
	
	
	
	private static final Gson gson = new Gson();

	
	public ResponseEntity<String> savePostWithoutImage(PostDto postDto) {
		
		Post post= this.toPost(postDto);
		if(postDto.getStringOfImage()!=null) {
		postDto.setByteImage(Base64.getDecoder().decode(postDto.getStringOfImage()));
		}
		
		if(Constant.censoring.equals("yes")) {
			
			TextFilter tf = new TextFilter();
		    String adequateCensored= tf.adequateCensor(postDto.getDescription(), postDto.getCensoredDescription());
		    post.setDescription(adequateCensored);
		    
		}
		    

	

		
		 post.setUpvotes(0);
		 post.setStatus("Pending");
		 LocalDateTime today = LocalDateTime.now();
		 post.setCreatedAt(today);
		 
	
		
		
		
		//Post post = modelMapper.map(postDto, Post.class);
		
		Sector sector = sectorRepository.findById(postDto.getSector().getSectorId()).orElse(null);
		if(sector!=null) {
			post.setSector(sector);
		}
		
		User user =  userRepository.findById(postDto.getUserEmail()).orElseThrow(() -> new NullPointerException("User not found"));
		post.setUser(user);
		
		String addressSuccess = this.setPostLocation(post);
		
		if(addressSuccess.equals("Not Pune District")) {
			System.out.println("Not Pune");
			return new ResponseEntity<>(gson.toJson("Sorry we only serve in Pune District"),HttpStatus.NOT_ACCEPTABLE);
		}
		else if(addressSuccess.equals("Location not found")) {
			System.out.println("Location not found");
			//yutika save post

		

		
			System.out.println("Done");
//			String objectUrl = getImageUrl(BUCKET, key);
//			post.setImagePath(objectUrl);
			
			post = postRepository.save( post);
			
//			key = user.getUserId() + today.toString()+".png";
//			uploadImage(postDto.getByteImage(),BUCKET, key,post.getPostId());
			
//			
//			
//			  key = user.getUserId() + today.toString()+".png";
//				uploadImage(postDto.getByteImage(),BUCKET, key,post.getPostId());
//				System.out.println("Done");
//				
//				
//				
//				   ImageAnalyzer ia = new ImageAnalyzer();
//				     String new_key=ia.detectLabels(BUCKET, post.getImagePath());
//				     if(new_key == null || new_key.equals(null))
//				     {
//				    	 s3Client.deleteObject(BUCKET, key);
//				    	 System.out.println("Gone");
//				    	 System.out.println("Post cannot be saved!");
//				    	 return new ResponseEntity<>("Post cannot be saved!", HttpStatus.OK);
//				     }
//				     else 
//				     {
//				    	 s3Client.deleteObject(BUCKET, key);
//				    	 System.out.println("Gone");
//
//				    		uploadImage(postDto.getByteImage(),BUCKET, new_key,post.getPostId());
//				 	
//
//						 post.setCreatedAt(today);
//						post = postRepository.save( post);
//						
//				    	 
//				     }
//			
			
			
			
			
			
			
			return new ResponseEntity<>(gson.toJson("Seems like details related to your location don't exist on OpenStreetMap. We will still post your request, but wouldn't be able to guarantee immediate response. Address : ("+post.getLocation().getLatitude()+", "+post.getLocation().getLongitude()+"), "+post.getLocation().getAreaName()+", "+post.getLocation().getTaluka()+", "+post.getLocation().getState()) ,HttpStatus.OK);
		}
		
	
	     System.out.println("Successssssss4");
	     
	     Set<Notification> notifications = post.getNotifications();
	     
	
//			String objectUrl = getImageUrl(BUCKET, key);
//			System.out.println("obj url********************************************************************************** "+objectUrl);
//			post.setImagePath(objectUrl);
			
			
	     post = postRepository.save( post);
	     
	   
				
	     
	     for(Notification notification: notifications) {
	    	 
	    	 notification.setPost(post);
	     }
	     
	     notificationRepository.saveAll(notifications);
	     
//	     key = user.getUserId() + today.toString()+".png";
//			uploadImage(postDto.getByteImage(),BUCKET, key,post.getPostId());
//			System.out.println("Done");
//			
//			
//			
//			   ImageAnalyzer ia = new ImageAnalyzer();
//			     String new_key=ia.detectLabels(BUCKET, post.getImagePath());
//			     if(new_key == null || new_key.equals(null))
//			     {
//			    	 s3Client.deleteObject(BUCKET, key);
//			    	 System.out.println("Gone");
//			    	 System.out.println("Post cannot be saved!");
//			    	 return new ResponseEntity<>("Post cannot be saved!", HttpStatus.OK);
//			     }
//			     else 
//			     {
//			    	 s3Client.deleteObject(BUCKET, key);
//			    	 System.out.println("Gone");
//
//			    		uploadImage(postDto.getByteImage(),BUCKET, new_key,post.getPostId());
//			 	
//
//					 post.setCreatedAt(today);
//					post = postRepository.save( post);
//					
//			    	 
//			     }
			     
	     
	     
	   return new ResponseEntity<>(gson.toJson("Successfully Posted, Address : ("+post.getLocation().getLatitude()+","+post.getLocation().getLongitude()+"), "+post.getLocation().getAreaName()+", "+post.getLocation().getTaluka()+", "+post.getLocation().getState()),HttpStatus.OK);

		
		
	}
	
	
	public ResponseEntity<String> savePost(PostDto postDto) {
		
		Post post= this.toPost(postDto);
		
		if(postDto.getStringOfImage()!=null) {
		postDto.setByteImage(Base64.getDecoder().decode(postDto.getStringOfImage()));
		}
		
		if(Constant.censoring.equals("yes")) {
			
			TextFilter tf = new TextFilter();
		    String adequateCensored= tf.adequateCensor(postDto.getDescription(), postDto.getCensoredDescription());
		    post.setDescription(adequateCensored);
		    
		}
		    

	

		
		 post.setUpvotes(0);
		 post.setStatus("Pending");
		 LocalDateTime today = LocalDateTime.now();
		 post.setCreatedAt(today);
		 
	
		
		
		
		//Post post = modelMapper.map(postDto, Post.class);
		
		Sector sector = sectorRepository.findById(postDto.getSector().getSectorId()).orElse(null);
		if(sector!=null) {
			post.setSector(sector);
		}
		
		User user =  userRepository.findById(postDto.getUserEmail()).orElseThrow(() -> new NullPointerException("User not found"));
		post.setUser(user);
		
		String addressSuccess = this.setPostLocation(post);
		
		if(addressSuccess.equals("Not Pune District")) {
			System.out.println("Not Pune");
			return new ResponseEntity<>(gson.toJson("Sorry we only serve in Pune District"),HttpStatus.NOT_ACCEPTABLE);
		}
		else if(addressSuccess.equals("Location not found")) {
			System.out.println("Location not found");
			//yutika save post

			

		
			System.out.println("Done");
//			String objectUrl = getImageUrl(BUCKET, key);
//			post.setImagePath(objectUrl);
			
			post = postRepository.save( post);
			
//			key = user.getUserId() + today.toString()+".png";
//			uploadImage(postDto.getByteImage(),BUCKET, key,post.getPostId());
			
			
			
			
			  key = user.getUserId() + today.toString()+".png";
				uploadImage(postDto.getByteImage(),BUCKET, key,post.getPostId());
				System.out.println("Done");
				
				
				
				   ImageAnalyzer ia = new ImageAnalyzer();
				     String new_key=ia.detectLabels(BUCKET, post.getImagePath());
				     if(new_key == null || new_key.equals(null))
				     {
				    	 int trials =0;
				 		while(trials <4) {
				 		
				 		
				 			trials++;
				    	 try {
				    	 s3Client.deleteObject(BUCKET, key);
				    	 break;
				    	 }catch(Exception e) {
				    		 System.out.println("Error in deleteeeeee");
				    		 
				    	 }
				 		}
				    	 System.out.println("Gone");
				    	 System.out.println("Post cannot be saved!");
				    	 return new ResponseEntity<>("Post cannot be saved!", HttpStatus.OK);
				     }
				     else 
				     {
				    	 int trials =0;
					 		while(trials <4) {
					 		
					 		
					 			trials++;
					    	 try {
					    	 s3Client.deleteObject(BUCKET, key);
					    	 break;
					    	 }catch(Exception e) {
					    		 System.out.println("Error in deleteeeeee");
					    		 
					    	 }
					 		}
				    	 System.out.println("Gone");

				    		uploadImage(postDto.getByteImage(),BUCKET, new_key,post.getPostId());
				 	

						 post.setCreatedAt(today);
						post = postRepository.save( post);
						
				    	 
				     }
			
			
			
			
			
			
			
			return new ResponseEntity<>(gson.toJson("Seems like details related to your location don't exist on OpenStreetMap. We will still post your request, but wouldn't be able to guarantee immediate response. Address : ("+post.getLocation().getLatitude()+", "+post.getLocation().getLongitude()+"), "+post.getLocation().getAreaName()+", "+post.getLocation().getTaluka()+", "+post.getLocation().getState()) ,HttpStatus.OK);
		}
		
	
	     System.out.println("Successssssss4");
	     
	     Set<Notification> notifications = post.getNotifications();
	     
	
//			String objectUrl = getImageUrl(BUCKET, key);
//			System.out.println("obj url********************************************************************************** "+objectUrl);
//			post.setImagePath(objectUrl);
			
			
	     post = postRepository.save( post);
	     
	   
				
	     
	     for(Notification notification: notifications) {
	    	 
	    	 notification.setPost(post);
	     }
	     
	     notificationRepository.saveAll(notifications);
	     
	     key = user.getUserId() + today.toString()+".png";
			uploadImage(postDto.getByteImage(),BUCKET, key,post.getPostId());
			System.out.println("Done");
			
			
			
			   ImageAnalyzer ia = new ImageAnalyzer();
			     String new_key=ia.detectLabels(BUCKET, post.getImagePath());
			     if(new_key == null || new_key.equals(null))
			     {
			    	 
			    	 int trials =0;
				 		while(trials <4) {
				 		
				 		
				 			trials++;
				    	 try {
				    	 s3Client.deleteObject(BUCKET, key);
				    	 break;
				    	 }catch(Exception e) {
				    		 System.out.println("Error in deleteeeeee");
				    		 
				    	 }
				 		}
				 		
			    	
			    	 System.out.println("Gone");
			    	 System.out.println("Post cannot be saved!");
			    	 return new ResponseEntity<>("Post cannot be saved!", HttpStatus.OK);
			     }
			     else 
			     {
			    	 int trials =0;
				 		while(trials <4) {
				 		
				 		
				 			trials++;
				    	 try {
				    	 s3Client.deleteObject(BUCKET, key);
				    	 break;
				    	 }catch(Exception e) {
				    		 System.out.println("Error in deleteeeeee");
				    		 
				    	 }
				 		}
			    	 System.out.println("Gone");

			    		uploadImage(postDto.getByteImage(),BUCKET, new_key,post.getPostId());
			 	

					 post.setCreatedAt(today);
					post = postRepository.save( post);
					
			    	 
			     }
			     
	     
	     
	   return new ResponseEntity<>(gson.toJson("Successfully Posted, Address : ("+post.getLocation().getLatitude()+","+post.getLocation().getLongitude()+"), "+post.getLocation().getAreaName()+", "+post.getLocation().getTaluka()+", "+post.getLocation().getState()),HttpStatus.OK);

		
		
	}
	
	 
	public void uploadImage(byte[] imageBytes, String bucketName, String key, Integer post_id) //upload image object on s3 bucket
	{
		int trials =0;
		while(trials <4) {
		
		
			trials++;
			
	    try {
	        ObjectMetadata metadata = new ObjectMetadata();
	        metadata.setContentLength(imageBytes.length);
	        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
//	        amazonS3.putObject(bucketName, key, inputStream, metadata);
	        
	        PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream, metadata);
	        request.setCannedAcl(CannedAccessControlList.PublicRead);
	       
	        amazonS3.putObject(request);
	        setImageUrlandSavePost(post_id,key);
	        
	        break;
	        
	     

	        
	        
	        
	        
	    } catch (AmazonServiceException e) {
	        // Handle the exception
	    	System.out.print("Error uploading file **********************************************************************************: "+ e.getErrorMessage());
	    } catch (SdkClientException e) {
	        // Handle the exception
	    	System.out.print("Error uploading file -*********************************************************************************************** "+ e.getMessage());
	    }
		}
		
		
	}

//	public String getImageUrl(String bucketName, String key)
//	{
//		String objectUrl = "";
//		java.util.Date expiration = new java.util.Date();
//		 long expTimeMillis = expiration.getTime();
//		 expTimeMillis += 1000 * 60 * 60; // 1 hour
//		 expiration.setTime(expTimeMillis);
//
//		 GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
//		         .withMethod(HttpMethod.GET)
//		         .withExpiration(expiration);
//
//		 objectUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
//		 return objectUrl;
//	}
	
	
	public void setImageUrlandSavePost(Integer post_id,String key)
	
	{
		for(int i=0;i<3;i++){
		 Post post = postRepository.findById(post_id).orElseThrow( null );
		 if(post!=null) {
			
				post.setImagePath(key);
				 post = postRepository.save( post);
				 break;
			 
		 }else {
			 try {
				 
				 Thread.sleep(7000);
			 }catch(InterruptedException e){
				 System.out.println("Thread zopla nahi ");
			 }
			 System.out.println("Post not found for setting image");
		 }
		}
	}

	
     
     
     public String upvotePost(String user_id, Integer post_id) {
    		
    	 Post post = postRepository.findById(post_id).orElseThrow( null );
    	 if(post.getUpvotedPostUsers()==null) {
    		 post.setUpvotedPostUsers(new HashSet<User>());
   
    	 }
    	
    	 User user  =userRepository.findById(user_id).orElse(null);
    	
    	 System.out.println("Conatins:"+ post.getUpvotedPostUsers().contains(user));
    	 
    	 if( post.getUpvotedPostUsers().contains(user)) {
    		 
    		 post.getUpvotedPostUsers().remove(userRepository.findById(user_id).orElseThrow(null));
    		
    		 
    		 post.setUpvotes(post.getUpvotes()-1);
    		 postRepository.save(post);
    		 return "Removed Upvote";
    	 }else {
    		 
    		  post.getUpvotedPostUsers().add(userRepository.findById(user_id).orElseThrow(null));
    		 if(post.getUpvotes()==null) {
    			 post.setUpvotes(0);
    		 }
    		 post.setUpvotes(post.getUpvotes()+1);
    		 postRepository.save(post);
    		 
    		 
    		 Notification notification = new Notification();
    		
    		 notification.setToUser(post.getUser());
    		 notification.setByUser(user);
    		 notification.setNotificationType("upvoted");
    		 notification.setDescription(user.getName()+" upvoted your post");
    		 // Set the created_at field to the current timestamp
    		 notification.setCreatedAt( LocalDateTime.now());
    		 notification.setPost(post);
    		 notificationRepository.save(notification);

    		 
    		 return "Upvoted Successfully";
    	 }
    	
    	 
 		
 	}
     
     public String savePost(String user_id, Integer post_id) {
 		
    	 Post post = postRepository.findById(post_id).orElseThrow( null );
    	 if(post.getSavedPostUsers()==null) {
    		 post.setSavedPostUsers(new HashSet<User>());
   
    	 }
    	
    	  
	       
			
    	 
    	 
    	 User user  =userRepository.findById(user_id).orElse(null);
    	
    	 System.out.println("Conatins:"+ post.getSavedPostUsers().contains(user));
    	 
    	 if( post.getSavedPostUsers().contains(user)) {
    		 
    		 post.getSavedPostUsers().remove(userRepository.findById(user_id).orElseThrow(null));
    		
    		 
    		 postRepository.save(post);
    		 return "Unsaved";
    	 }else {
    		 
    		 post.getSavedPostUsers().add(userRepository.findById(user_id).orElseThrow(null));
    		
    		 postRepository.save(post);
    		 return "Saved";
    	 }
    	
    	 
 		
 	}
     
     
     
     public PostDto toPostDto(Post post) {
    	 
    	 ModelMapper modelMapper = new ModelMapper();
    	 String areaId;
    	 
    	 areaId = post.getLocation().getAreaId();
    	 
    	
    	 post.getLocation().setAreaId(null);
    	  	
  		 PostDto postDto=modelMapper.map(post,PostDto.class);
    	 
    	 postDto.getLocation().setAreaId(areaId);
    	 postDto.setUserEmail(post.getUser().getUserId());
    	 
    	 postDto.setName(post.getUser().getName());
    	 postDto.setProfileImg(post.getUser().getProfileImg());
    	 
    	 if(post.getOrganization()!=null) {
    		 
    		 postDto.setOrganizationEmail(post.getOrganization().getOrganizationId());
    		 postDto.setOrganizationName(post.getOrganization().getName());
    	 }
    	 
    	 return postDto;
    	
    	 
    	 
     }
     
     public Post toPost(PostDto postDto) {
    	 String areaId;
    	 
    	 areaId = postDto.getLocation().getAreaId();
    	 
    	 ModelMapper modelMapper = new ModelMapper();
    	 postDto.getLocation().setAreaId(null);
    	  	
  		 Post post=modelMapper.map(postDto,Post.class);
    	 
    	 post.getLocation().setAreaId(areaId);
    	 
    	 
    	 return post;
     }
     
     
     public Integer getMyUpvote(String user_id,Post post) {
    	 
    	 User user  =userRepository.findById(user_id).orElse(null);
     	
    	 System.out.println("Conatins my upvote:"+ post.getUpvotedPostUsers().contains(user));
    	 
    	 for(User u : post.getUpvotedPostUsers()) {
    		 System.out.println("Upvoted user "+  u.getUserId());
    	 }
    	 
    	 if( post.getUpvotedPostUsers().contains(user)) {
    		 
    		 return 1;
    		

    	 }else {
    		 
    		
    		 return 0;
    	 }
    	 
    	 
     }


	public List<PostDto> getPostbyLocationAndSectorMostPopular(String user_id, List<String> sectors, List<String> talukas,
			List<String> areas,Integer page,Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		List<Post> posts = postRepository.getByLocationAndSectorMostPopular(sectors, talukas, areas,pageable).getContent();
		List<PostDto> postDtos = new ArrayList<PostDto>();
		for(Post p:posts) {
			PostDto postDto= this.toPostDto(p);
			postDto.setMyUpvote(this.getMyUpvote(user_id, p));
			System.out.println("post dto my upvote" + postDto.getMyUpvote());
			postDtos.add(postDto);
		}
		
		return postDtos;
	}

	public List<PostDto> getPostbyLocationAndSectorMostRecent(String user_id, List<String> sectors, List<String> talukas,
			List<String> areas,Integer page,Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		List<Post> posts = postRepository.getByLocationAndSectorMostRecent(sectors, talukas, areas,pageable).getContent();
		List<PostDto> postDtos = new ArrayList<PostDto>();
		for(Post p:posts) {
			System.out.println(p.getPostId());
			PostDto postDto= this.toPostDto(p);
			postDto.setMyUpvote(this.getMyUpvote(user_id, p));
			postDtos.add(postDto);
		}
		
		return postDtos;
	}


	public List<PostDto> getPostbySectorMostRecent(String user_id, List<String> sectors,Integer page,Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		List<Post> posts = postRepository.getBySectorMostRecent(sectors,pageable).getContent();
		List<PostDto> postDtos = new ArrayList<PostDto>();
		for(Post p:posts) {
			System.out.println(p.getPostId());
			PostDto postDto= this.toPostDto(p);
			postDto.setMyUpvote(this.getMyUpvote(user_id, p));
			postDtos.add(postDto);
		}
		
		return postDtos;
		
		
		
	}


	public List<PostDto> getPostbySectorMostPopular(String user_id, List<String> sectors,Integer page,Integer size) {
		
//		int page = 0;
//		int size = 3;

		Pageable pageable = PageRequest.of(page, size);
		
		List<Post> posts = postRepository.getBySectorMostPopular(sectors,pageable).getContent();
		List<PostDto> postDtos = new ArrayList<PostDto>();
		for(Post p:posts) {
			System.out.println(p.getPostId());
			PostDto postDto= this.toPostDto(p);
			postDto.setMyUpvote(this.getMyUpvote(user_id, p));
			System.out.println("post dto my upvote" + postDto.getMyUpvote());
			postDtos.add(postDto);
		}
		
		return postDtos;
		
		
	}


	public List<PostDto> getPostbyLocationMostRecent(String user_id, List<String> talukas, List<String> areas,Integer page,Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		List<Post> posts = postRepository.getByLocationMostRecent(talukas,areas,pageable ).getContent();
		List<PostDto> postDtos = new ArrayList<PostDto>();
		for(Post p:posts) {
			System.out.println(p.getPostId());
			PostDto postDto= this.toPostDto(p);
			postDto.setMyUpvote(this.getMyUpvote(user_id, p));
			postDtos.add(postDto);
		}
		
		return postDtos;
		
	}


	public List<PostDto> getPostbyLocationMostPopular(String user_id, List<String> talukas, List<String> areas,Integer page,Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		List<Post> posts = postRepository.getByLocationMostPopular(talukas,areas,pageable).getContent();
		List<PostDto> postDtos = new ArrayList<PostDto>();
		for(Post p:posts) {
			System.out.println(p.getPostId());
			PostDto postDto= this.toPostDto(p);
			postDto.setMyUpvote(this.getMyUpvote(user_id, p));
			postDtos.add(postDto);
		}
		
		return postDtos;
	}
	
	public List<PostDto> getPostbyDefaultMostPopular(String user_id, Integer page, Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		List<Post> posts = postRepository.getByDefaultMostPopular(pageable).getContent();
		List<PostDto> postDtos = new ArrayList<PostDto>();
		for(Post p:posts) {
			System.out.println(p.getPostId());
			PostDto postDto= this.toPostDto(p);
			postDto.setMyUpvote(this.getMyUpvote(user_id, p));
			postDtos.add(postDto);
		}
		
		return postDtos;
	}
	
	


	public String acceptPost(String organization_id, Integer post_id) {
		
		 Post post = postRepository.findById(post_id).orElseThrow( null );
    	if(post.getStatus().equals("Pending")) {
    		
    		 Organization organization  = organizationRepository.findById(organization_id).orElse(null);
    		 
    		 post.setOrganization(organization);
    		 
    		 post.setStatus("Accepted");
    		
    		
    		 
    		 postRepository.save(post);
    		 
    		 ArrayList<Notification> notiAl = new ArrayList<Notification>();

    		 Notification notification = new Notification();
    		 notification.setToUser(post.getUser());
    		 notification.setByUser(organization);
    		 notification.setNotificationType("accepted");
    		 notification.setDescription(organization.getName()+" accepted your post");
    		 // Set the created_at field to the current timestamp
    		 notification.setCreatedAt( LocalDateTime.now());
    		 notification.setPost(post);
    		 notiAl.add(notification);
    		 //notificationRepository.save(notification);

    		 for(Notification noti : post.getNotifications()) {
    			 if(noti.getNotificationType().equals("toAccept")&& !noti.getToUser().getUserId().equals(organization.getUserId())) {
    				 Notification notiToOtherOrg = new Notification();
    				 notiToOtherOrg.setToUser(noti.getToUser());
    				 notiToOtherOrg.setByUser(organization);
    				 notiToOtherOrg.setNotificationType("accepted");
    				 notiToOtherOrg.setDescription("Post accepted by "+organization.getName());
    				 // Set the created_at field to the current timestamp
    				 notiToOtherOrg.setCreatedAt( LocalDateTime.now());
    				 notiToOtherOrg.setPost(post);
    				 notiAl.add(notiToOtherOrg);
    				 //notificationRepository.save(notiToOtherOrg);
    			 }
    		 }

    		 notificationRepository.saveAll(notiAl);


	 
    		 return "Accepted";
    	    	
    	}else {
    		
    		return "Not Pending Post";
    	}
    	
    
    	

    	 
	}
	
	public String solvePost(String organization_id, Integer post_id) {
		
		 Post post = postRepository.findById(post_id).orElseThrow( null );
		 if(post.getStatus().equals("Pending")) {
	   			
	   			System.out.println("Directly solving a pending post");
	   		}
		 
   	if(post.getStatus().equals("Accepted")||post.getStatus().equals("Pending")) {
   		
   		 Organization organization  = organizationRepository.findById(organization_id).orElse(null);
   		 post.setStatus("Solved");
   		 if(organization!=null) {
			 post.setOrganization(organization);
			 postRepository.save(post);
			 
			 Notification notification = new Notification();
			 notification.setToUser(post.getUser());
			 notification.setByUser(organization);
			 notification.setNotificationType("solved");
			 notification.setDescription(organization.getName()+" solved your post");
			 // Set the created_at field to the current timestamp
			 notification.setCreatedAt( LocalDateTime.now());
			 notification.setPost(post);
			notificationRepository.save(notification);
			 
			 
			 
		 }else {
			 
			 postRepository.save(post);
			 
			 ArrayList<Notification> notiAl = new ArrayList<Notification>();

    		 for(Notification noti : post.getNotifications()) {
    			 if(noti.getNotificationType().equals("toAccept")) {
    				 Notification notiToOtherOrg = new Notification();
    				 notiToOtherOrg.setToUser(noti.getToUser());
    				 notiToOtherOrg.setByUser(organization);
    				 notiToOtherOrg.setNotificationType("contribute");
    				 notiToOtherOrg.setDescription("The post by "+post.getUser().getName()+
    						 " has received help from individual user and is solved.");
    				 // Set the created_at field to the current timestamp
    				 notiToOtherOrg.setCreatedAt( LocalDateTime.now());
    				 notiToOtherOrg.setPost(post);
    				 notiAl.add(notiToOtherOrg);
    				 //notificationRepository.save(notiToOtherOrg);
    			 }
    		 }

    		 notificationRepository.saveAll(notiAl);

			 
		
			 
			 
		 }
   		
   		
   		 
   		 
   		 return "Solved";
   	    	
   	}else {
   		
   		
   		return "Not Pending Post";
   	}
   	
   
   	

   	 
	}
	


	// notification 
	
	public TalukaDto getLatestPostTaluka(String userId) {
    	Optional<Post> latestPostOptional = postRepository.findTopByUserUserIdOrderByCreatedAtDesc(userId);
        if (latestPostOptional.isPresent()) {
            // map the Post entity to a PostDto object
            Post latestPost = latestPostOptional.get();
            System.out.println(latestPost);
          
			Taluka currentTaluka = talukaService.getTalukaFromId(latestPost.getLocation().getTaluka());
            Type setType = new TypeToken<TalukaDto>() {}.getType();
    		ModelMapper modelMapper = new ModelMapper();
    		// map the Set<Notification> to Set<NotificationDto> using the TypeToken
    		TalukaDto talukaDto = modelMapper.map(currentTaluka, setType);

    		return talukaDto;
        } else {
            // return null or throw an exception, depending on your requirements
            return null;
        }
    }
     
 	public Post getLatestPostByUser(String userId) {
		// retrieve the latest post for the specified user from the database
        Optional<Post> latestPostOptional = postRepository.findTopByUserUserIdOrderByCreatedAtDesc(userId);
        if (latestPostOptional.isPresent()) {
            // map the Post entity to a PostDto object
            Post latestPost = latestPostOptional.get();
            System.out.println(latestPost);
            return latestPost;
        } else {
            // return null or throw an exception, depending on your requirements
            return null;
        }
	}
	
 	
 	
	
 	
 	
	public Set<NotificationDto> getNotificationsForLatestPostByUser(String userId) {
		Post latestPost = getLatestPostByUser(userId);
		// Create the type tokens for the source and target types
//		TypeToken<Set<Notification>> sourceType = new TypeToken<Set<Notification>>() {};
//		TypeToken<Set<NotificationDto>> targetType = new TypeToken<Set<NotificationDto>>() {};
//
//		// Perform the conversion using ModelMapper
//		ModelMapper modelMapper = new ModelMapper();
//		Set<NotificationDto> notificationDtoSet = modelMapper.map(latestPost.getNotifications(), sourceType.getType(), targetType.getType());
		Set<Notification> notifications = latestPost.getNotifications(); // your set of notifications

		// create a TypeToken for the Set<NotificationDto> type
		Type setType = new TypeToken<Set<NotificationDto>>() {}.getType();
		
		ModelMapper modelMapper = new ModelMapper();

		// map the Set<Notification> to Set<NotificationDto> using the TypeToken
		Set<NotificationDto> notificationDtos = modelMapper.map(notifications, setType);

		return notificationDtos;
	}

	
//	public PostDto getLatestPostForUser(String userId) {
//	// retrieve the latest post for the specified user from the database
//    Optional<Post> latestPostOptional = postRepository.findTopByUserUserIdOrderByCreatedAtDesc(userId);
//    if (latestPostOptional.isPresent()) {
//        // map the Post entity to a PostDto object
//        Post latestPost = latestPostOptional.get();
//        System.out.println(latestPost);
//        for(Notification n : latestPost.getNotifications()) {
//        	System.out.println("FCHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH"+n.getToUser().getUserId());
//        }
//        PostDto latestPostDto = this.toPostDto(latestPost);
//        return latestPostDto;
//    } else {
//        // return null or throw an exception, depending on your requirements
//        return null;
//    }
//}
	
//	public String setPostLocation(Post post) {
//
//		if(post.getLocation().getTaluka()==null) {
//
//			System.out.println("Successssssss2");
//
//			return locationService.setAddressDetails(post);
//
//
//		}
//		
//		ArrayList<Area> nearbyAreas = areaService.getNearbyAreas(post.getLocation());
//		System.out.println("  beforeeeeeeeeeeeeeeeeeeee   trfsfyhdjilkfhbvj");
//		System.out.println("gfek.jsbvhnnsjb,dj,b,jb");
//		post.setNotifications(notificationService.setNotificationSet(nearbyAreas, post.getLocation(), post.getSector()));
//		System.out.println("dangerrrrrrrrrrrrrrrr testttttttt"+post.getNotifications());
//		System.out.println("  afterrrrrrrrrrrrrrrrrrr   ");
//		return "Save directly";
//
//
//	}
	
	 public String setPostLocation(Post post) {
			
			if(post.getLocation().getTaluka()==null) {
				
				System.out.println("Successssssss2");
				
				return locationService.setAddressDetails(post);

		
		}
			
			ArrayList<Area> nearbyAreas = areaService.getNearbyAreas(post.getLocation());
			System.out.println("  beforeeeeeeeeeeeeeeeeeeee   ");
			post.setNotifications(notificationService.setNotificationSet(nearbyAreas, post.getLocation(), post.getSector(),post.getUser()));
			System.out.println("  afterrrrrrrrrrrrrrrrrrr   ");
			
			return "Save directly";

		
	}





	public PostDto getPost(String user_id, Integer post_id) {
		
		Post post = postRepository.findById(post_id).orElseThrow(() -> new NullPointerException("Post not found"));
		
		PostDto postDto= this.toPostDto(post);
		postDto.setMyUpvote(this.getMyUpvote(user_id, post));
		
		return postDto;
	}





	public List<PostDto> getPostbyMyLocalityMostRecent(String user_id, LocationDto locationDto, Integer page,
			Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		// method to get taluka 
		String taluka;
		if(locationDto.getLatitude()==null || locationDto.getLongitude()==null) {
			
			taluka="Pune City";
			
		}else {
			
			taluka= talukaService.getTalukaFromLatLng(locationDto);
			
		}
		 
		
		 List<String> talukas = new ArrayList<String>();
		 talukas.add(taluka);
		 List<String> areas = new ArrayList<String>();
		 
		List<Post> posts = postRepository.getByLocationMostRecent(talukas,areas,pageable).getContent();
		List<PostDto> postDtos = new ArrayList<PostDto>();
		for(Post p:posts) {
			System.out.println(p.getPostId());
			PostDto postDto= this.toPostDto(p);
			postDto.setMyUpvote(this.getMyUpvote(user_id, p));
			postDtos.add(postDto);
		}
		
		return postDtos;
		
	}


	public List<WinnerDto> getTop10OrganizationsWithSolvedPosts() {
	    List<Object[]> result = postRepository.findTop10OrganizationsWithSolvedPosts();
	    List<WinnerDto> winnerOrgs = new ArrayList<>();
	    for (Object[] row : result) {
	        WinnerDto winnerOrg = new WinnerDto((String) row[0], (String) row[1], (Long) row[2]);
//	        winnerOrg.setOrganizationId((String) row[0]);
//	        winnerOrg.setName((String) row[1]);
//	        winnerOrg.setSolvedPosts((Integer) row[2]);
	        System.out.println(winnerOrg.getId()+" >>>>>>>>>>>>>>>>>>>>>>>>>>>>  "+winnerOrg.getName()+" >>>>>>>>>>>>>>>>>>>>"+ winnerOrg.getNumberOfPosts());
	        winnerOrgs.add(winnerOrg);
	    }
	    return winnerOrgs;
	}


	public List<WinnerDto> getTopTenWinnerUsers() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Object[]> result = postRepository.findTopTenUsersWithAcceptedOrSolvedPostsAfterDate(oneMonthAgo);
        List<WinnerDto> winnerUsers = new ArrayList<>();
        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>  ");
        for (Object[] row : result) {
            WinnerDto winnerUser = new WinnerDto((String) row[0],(String) row[1],(Long) row[2]);
//            winnerUser.setUserId((String) row[0]);
//            winnerUser.setName((String) row[1]);
//            winnerUser.setPostedPosts((Long) row[2]);
            
            System.out.println(winnerUser.getId()+" >>>>>>>>>>>>>>>>>>>>>>>>>>>>  "+winnerUser.getName()+" >>>>>>>>>>>>>>>>>>>>"+ winnerUser.getNumberOfPosts());
            winnerUsers.add(winnerUser);
        }
        return winnerUsers;
    }
	



	
	
	
}
