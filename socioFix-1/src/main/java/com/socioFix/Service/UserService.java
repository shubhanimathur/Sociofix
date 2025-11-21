package com.socioFix.Service;


import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.socioFix.Dto.DriveDto;
import com.socioFix.Dto.FollowingUserDto;
import com.socioFix.Dto.NotificationDisplayDto;
import com.socioFix.Dto.NotificationDto;
import com.socioFix.Dto.OrganizationDto;
import com.socioFix.Dto.PostDto;
import com.socioFix.Dto.UserDto;
import com.socioFix.model.Drive;
import com.socioFix.model.Organization;
import com.socioFix.model.Post;
import com.socioFix.model.Sector;
import com.socioFix.model.User;
import com.socioFix.model.UserFollowingOrganization;
import com.socioFix.model.Notification.Notification;
import com.socioFix.repository.DriveRepository;
import com.socioFix.repository.NotificationRepository;
import com.socioFix.repository.PostRepository;
import com.socioFix.repository.SectorRepository;
import com.socioFix.repository.UserFollowingOrganizationRepository;
import com.socioFix.repository.UserRepository;

import jakarta.persistence.DiscriminatorValue;

@Service
public class UserService {


	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PostRepository postRepository;
	
	@Autowired 
	PostService postService;
	
	@Autowired
	DriveRepository driveRepository;
	
	@Autowired
	DriveService driveService;
	
	
	@Autowired
	NotificationRepository notificationRepository;
	
	@Autowired
	UserFollowingOrganizationRepository userFollowingOrganizationRepository;
	
	@Autowired
	private AmazonS3 amazonS3;
	private static final String BUCKET = "demo4-s3";

	@Value("${aws.accessKeyId})
	private String accessKeyId;

	@Value("${aws.secretAccessKey})
	private String secretAccessKey;


	BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
	AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(Regions.US_EAST_2).build();

	String key="";
	String imagePath="https://demo4-s3.s3.us-east-2.amazonaws.com/";
	

	
	public static ModelMapper modelMapper = new ModelMapper();
	
	public UserDto saveUser(UserDto userDto) {
		
		User user = modelMapper.map(userDto, User.class);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setCreatedAt(timestamp);
		user.setUserOrOrganization("User");
		user = userRepository.save(user);
		
		return modelMapper.map(user, UserDto.class);
		
	}
	
//	public List<PostDto> getPostbyLocationAndSectorMostPopular(String user_id, List<String> sectors, List<String> talukas,
//			List<String> areas,Integer page,Integer size) {
//		
//		Pageable pageable = PageRequest.of(page, size);
//		
//		List<Post> posts = postRepository.getByLocationAndSectorMostPopular(sectors, talukas, areas,pageable).getContent();
//		List<PostDto> postDtos = new ArrayList<PostDto>();
//		for(Post p:posts) {
//			PostDto postDto= this.toPostDto(p);
//			postDto.setMyUpvote(this.getMyUpvote(user_id, p));
//			System.out.println("post dto my upvote" + postDto.getMyUpvote());
//			postDtos.add(postDto);
//		}
//		
//		return postDtos;
//	}

	
	@Async 
	public void uploadImage(byte[] imageBytes, String bucketName, String key, String user_id) //upload image object on s3 bucket
	{
		
		int trials =0;
		while(trials <4) {
		
		
			trials++;
	    try {
	        ObjectMetadata metadata = new ObjectMetadata();
	        metadata.setContentLength(imageBytes.length);
	        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
	        PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream, metadata);
	        request.setCannedAcl(CannedAccessControlList.PublicRead);
	        amazonS3.putObject(request);
	        setImageUrlandSaveUser(user_id,key);
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

	
	public void setImageUrlandSaveUser(String user_id,String key)
	
	{
		for(int i=0;i<3;i++){
		 User user = userRepository.findById(user_id).orElseThrow( null );
		 if(user!=null) {
			 	user.setProfileImg(key);
				 user = userRepository.save( user);
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

	
     
	
	

	public List<PostDto> getUserPosts(String user_id, String view_user_id, Integer page, Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		List<Post> posts = postRepository.getUserPosts(user_id,pageable).getContent();
		
		List<PostDto> postDtos = new ArrayList<PostDto>();
		for(Post p:posts) {
			PostDto postDto= postService.toPostDto(p);
			postDto.setMyUpvote(postService.getMyUpvote(view_user_id, p));
			System.out.println("post dto my upvote" + postDto.getMyUpvote());
			postDtos.add(postDto);
		}
		
		return postDtos;
		
		
	}
	
	

	public List<PostDto> getUserUpvotedPosts(String user_id, Integer page, Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		List<Post> posts = postRepository.findAllByUpvotedUser(user_id,pageable).getContent();
		
		List<PostDto> postDtos = new ArrayList<PostDto>();
		for(Post p:posts) {
			PostDto postDto= postService.toPostDto(p);
			postDto.setMyUpvote(postService.getMyUpvote(user_id, p));
			System.out.println("post dto my upvote" + postDto.getMyUpvote());
			postDtos.add(postDto);
		}
		
		return postDtos;
		
		
	}
	
public List<PostDto> getUserSavedPosts(String user_id, Integer page, Integer size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		List<Post> posts = postRepository.findAllBySavedUser(user_id,pageable).getContent();
		
		List<PostDto> postDtos = new ArrayList<PostDto>();
		for(Post p:posts) {
			PostDto postDto= postService.toPostDto(p);
			postDto.setMyUpvote(postService.getMyUpvote(user_id, p));
			System.out.println("post dto my upvote" + postDto.getMyUpvote());
			postDtos.add(postDto);
		}
		
		return postDtos;
		
		
	}

public List<DriveDto> getUserVolunteeredDrives(String user_id, String view_user_id, Integer page, Integer size) {
	Pageable pageable = PageRequest.of(page, size);
	List<Drive> drives = driveRepository.findAllByVolunteeredUser(user_id,pageable).getContent();
	System.out.println("size of vol frives" + drives.size());
	List<DriveDto> driveDtos = new ArrayList<DriveDto>();
	for(Drive p:drives) {
		DriveDto driveDto= driveService.toDriveDto(p);
		driveDto.setMyUpvote(driveService.getMyUpvote(view_user_id, p));
		driveDto.setMyVolunteerStatus(driveService.getMyVolunteerStatus(view_user_id, p));
		System.out.println("drive dto my upvote" + driveDto.getMyUpvote());
		driveDtos.add(driveDto);
	}
	return driveDtos;
}



public UserDto getUser(String user_id) {
	
	User user = userRepository.findById(user_id).orElseThrow(() -> new NullPointerException("user not found"));
	UserDto userDto = modelMapper.map(user, UserDto.class);
	if(user.getPosts()==null)
		userDto.setPostsNo(0);
	else
		userDto.setPostsNo(user.getPosts().size());
	if(user.getFollowingOrganizations()==null)
		userDto.setFollowingOrgNo(0);
	else
		userDto.setFollowingOrgNo(user.getFollowingOrganizations().size());
	
	
	
	if(user.getVolunteeredDrives()==null)
		userDto.setVolunteerNo(0);
	else
		userDto.setVolunteerNo(user.getVolunteeredDrives().size());
	
	
	
	return userDto;
	
}

public List<PostDto> getUserAcceptedPosts(String user_id, String view_user_id, Integer page, Integer size) {
	
	
	Pageable pageable = PageRequest.of(page, size);
	
	List<Post> posts = postRepository.getUserAcceptedPosts(user_id,"Accepted",pageable).getContent();
	
	List<PostDto> postDtos = new ArrayList<PostDto>();
	for(Post p:posts) {
		PostDto postDto= postService.toPostDto(p);
		postDto.setMyUpvote(postService.getMyUpvote(view_user_id, p));
		System.out.println("post dto my upvote" + postDto.getMyUpvote());
		postDtos.add(postDto);
	}
	
	return postDtos;
	
	
}

public List<PostDto> getUserSolvedPosts(String user_id, String view_user_id, Integer page, Integer size) {
	
	
	Pageable pageable = PageRequest.of(page, size);
	
	List<Post> posts = postRepository.getUserSolvedPosts(user_id,"Solved",pageable).getContent();
	
	List<PostDto> postDtos = new ArrayList<PostDto>();
	for(Post p:posts) {
		PostDto postDto= postService.toPostDto(p);
		postDto.setMyUpvote(postService.getMyUpvote(view_user_id, p));
		System.out.println("post dto my upvote" + postDto.getMyUpvote());
		postDtos.add(postDto);
	}
	
	return postDtos;
	
	
}


public List<DriveDto> getUserDrives(String user_id, String view_user_id, Integer page, Integer size) {
	Pageable pageable = PageRequest.of(page, size);
	List<Drive> drives = driveRepository.getUserDrives(user_id,pageable).getContent();
	List<DriveDto> driveDtos = new ArrayList<DriveDto>();
	for(Drive p:drives) {
		DriveDto driveDto= driveService.toDriveDto(p);
		driveDto.setMyUpvote(driveService.getMyUpvote(view_user_id, p));
		driveDto.setMyVolunteerStatus(driveService.getMyVolunteerStatus(view_user_id, p));
		System.out.println("drive dto my upvote" + driveDto.getMyUpvote());
		driveDtos.add(driveDto);
	}
	return driveDtos;
}

public List<DriveDto> getDrivesForFollowedOrganizations(String user_id, String view_user_id, Integer page, Integer size) {
	Pageable pageable = PageRequest.of(page, size);
	List<Drive> drives =userFollowingOrganizationRepository.getDrivesForFollowedOrganizations(user_id,pageable).getContent();
	List<DriveDto> driveDtos = new ArrayList<DriveDto>();
	for(Drive p:drives) {
		DriveDto driveDto= driveService.toDriveDto(p);
		driveDto.setMyUpvote(driveService.getMyUpvote(view_user_id, p));
		driveDto.setMyVolunteerStatus(driveService.getMyVolunteerStatus(view_user_id, p));
		System.out.println("drive dto my upvote" + driveDto.getMyUpvote());
		driveDtos.add(driveDto);
	}
	return driveDtos;
}








public List<DriveDto> getUserUpvotedDrives(String user_id, Integer page, Integer size) {
	Pageable pageable = PageRequest.of(page, size);
	List<Drive> drives = driveRepository.findAllByUpvotedUser(user_id,pageable).getContent();
	System.out.println("post drive size" + drives.size());
	List<DriveDto> driveDtos = new ArrayList<DriveDto>();
	for(Drive p:drives) {
		DriveDto driveDto= driveService.toDriveDto(p);
		driveDto.setMyUpvote(driveService.getMyUpvote(user_id, p));
		System.out.println("drive dto my upvote" + driveDto.getMyUpvote());
		driveDtos.add(driveDto);
	}
	return driveDtos;
}



public List<DriveDto> getUserSavedDrives(String user_id, Integer page, Integer size) {
	Pageable pageable = PageRequest.of(page, size);
	
	List<Drive> drives = driveRepository.findAllBySavedUser(user_id,pageable).getContent();
	List<DriveDto> driveDtos = new ArrayList<DriveDto>();
	for(Drive p:drives) {
		DriveDto driveDto= driveService.toDriveDto(p);
		driveDto.setMyUpvote(driveService.getMyUpvote(user_id, p));
		System.out.println("drive dto my upvote" + driveDto.getMyUpvote());
		driveDtos.add(driveDto);
	}
	return driveDtos;
}

public List<FollowingUserDto> getFollowingOrganizations(String user_id, Integer page, Integer size) {
	
	
Pageable pageable = PageRequest.of(page, size);
	
	List<Organization> organizations = userFollowingOrganizationRepository.findFollowingOrganizationsByUserId(user_id,pageable).getContent();
	
	List<FollowingUserDto> followingUserDtos = new ArrayList<FollowingUserDto>();
	
	
	FollowingUserDto followingUserDto ;
	
	for(Organization organization:organizations) {
		
		followingUserDto = new FollowingUserDto(organization.getOrganizationId(),organization.getName(),organization.getProfileImg(),1,"Organization");
		
		followingUserDtos.add(followingUserDto);
		
	}
	return followingUserDtos;


	
	
	
	
}

public String toExculdeFromFilter(String text) {
	
	 TextFilter tf = new TextFilter();
     String filteredText = tf.filter(text);
     System.out.println(filteredText);
    // String back = tf.backConvert(filteredText);
    // System.out.println(back);
	
	return filteredText;
}


public NotificationDisplayDto createNotificationDisplayDto(Notification notification) {
	
	NotificationDisplayDto notificationDisplayDto ;
	
	notificationDisplayDto=  new NotificationDisplayDto(notification.getNotificationId(),notification.getToUser().getUserId(),
			notification.getDescription(),notification.getCreatedAt(),notification.getNotificationType()
			);

	
	if(notification.getByUser()!=null) {
		notificationDisplayDto.setByUserId(notification.getByUser().getUserId());
	}
	if(notification.getPost()!=null) {
		notificationDisplayDto.setPostId(notification.getPost().getPostId());
		
	}
	
	if(notification.getDrive()!=null) {
		notificationDisplayDto.setDriveId(notification.getDrive().getDriveId());
	} 
	return notificationDisplayDto;
}

public List<NotificationDisplayDto> getNotifications(String user_id, Integer page, Integer size) {
	
	Pageable pageable = PageRequest.of(page, size);
	
	List<Notification> notifications = notificationRepository.getNotifications(user_id, pageable).getContent();
	List<NotificationDisplayDto> notificationDisplayDtos = new ArrayList<NotificationDisplayDto>();
	NotificationDisplayDto notificationDisplayDto ;
	for(Notification notification:notifications ) {

		notificationDisplayDtos.add(createNotificationDisplayDto(notification));
	}
	
	return notificationDisplayDtos;
}



public List<NotificationDisplayDto> getNotificationsUserHelp(String user_id, Integer page, Integer size) {
Pageable pageable = PageRequest.of(page, size);
	
	List<Notification> notifications = notificationRepository.getNotificationsUserHelp(user_id, pageable).getContent();
	List<NotificationDisplayDto> notificationDisplayDtos = new ArrayList<NotificationDisplayDto>();
	NotificationDisplayDto notificationDisplayDto ;
	for(Notification notification:notifications ) {

		notificationDisplayDtos.add(createNotificationDisplayDto(notification));
	}
	
	return notificationDisplayDtos;

}

public List<NotificationDisplayDto> getNotificationsUserActivity(String user_id, Integer page, Integer size) {
Pageable pageable = PageRequest.of(page, size);
	
	List<Notification> notifications = notificationRepository.getNotificationsUserActivity(user_id, pageable).getContent();
	List<NotificationDisplayDto> notificationDisplayDtos = new ArrayList<NotificationDisplayDto>();
	NotificationDisplayDto notificationDisplayDto ;
	for(Notification notification:notifications ) {

		notificationDisplayDtos.add(createNotificationDisplayDto(notification));
	}
	
	return notificationDisplayDtos;

}

public List<NotificationDisplayDto> getNotificationsOrganizationHelp(String user_id, Integer page, Integer size) {
Pageable pageable = PageRequest.of(page, size);
	
	List<Notification> notifications = notificationRepository.getNotificationsOrganizationHelp(user_id, pageable).getContent();
	List<NotificationDisplayDto> notificationDisplayDtos = new ArrayList<NotificationDisplayDto>();
	NotificationDisplayDto notificationDisplayDto ;
	for(Notification notification:notifications ) {

		notificationDisplayDtos.add(createNotificationDisplayDto(notification));
	}
	
	return notificationDisplayDtos;

}

public List<NotificationDisplayDto> getNotificationsOrganizationActivity(String user_id, Integer page, Integer size) {
Pageable pageable = PageRequest.of(page, size);
	
	List<Notification> notifications = notificationRepository.getNotificationsOrganizationActivity(user_id, pageable).getContent();
	List<NotificationDisplayDto> notificationDisplayDtos = new ArrayList<NotificationDisplayDto>();
	NotificationDisplayDto notificationDisplayDto ;
	for(Notification notification:notifications ) {

		notificationDisplayDtos.add(createNotificationDisplayDto(notification));
	}
	
	return notificationDisplayDtos;

}

public UserDto editUser(UserDto userDto) {
	
	if(userDto.getStringOfImage()!=null) {
		userDto.setByteImage(Base64.getDecoder().decode(userDto.getStringOfImage()));
		}
	
	
	
	
	User user = userRepository.findById(userDto.getUserId()).orElse(null);
	user.setName(userDto.getName());
	user.setBio(userDto.getBio());
	user.setContactNo(userDto.getContactNo());
	user = userRepository.save(user);
	 LocalDateTime today = LocalDateTime.now();
	 
	 
	 if(userDto.getStringOfImage()!=null) {
	  key = user.getUserId() + today.toString()+".png";
	  
	  
		uploadImage(userDto.getByteImage(),BUCKET, key,user.getUserId());
	 }
		System.out.println("Done");
		
	
	return modelMapper.map(user, UserDto.class);
	
}



	
}
