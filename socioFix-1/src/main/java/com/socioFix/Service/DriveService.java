package com.socioFix.Service;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.socioFix.Dto.DriveDto;
import com.socioFix.Dto.LocationDto;
import com.socioFix.model.Location;
import com.socioFix.model.Organization;
import com.socioFix.model.Post;
import com.socioFix.model.Drive;
import com.socioFix.model.Sector;
import com.socioFix.model.User;
import com.socioFix.model.Organization;
import com.socioFix.model.Notification.Notification;
import com.socioFix.model.geoLocations.Area;

import com.socioFix.repository.DriveRepository;
import com.socioFix.repository.NotificationRepository;
import com.socioFix.repository.SectorRepository;
import com.socioFix.repository.UserRepository;
import com.socioFix.repository.OrganizationRepository;
@Service
public class DriveService {
	@Autowired
	DriveRepository driveRepository;
	@Autowired 
	SectorRepository sectorRepository;
	@Autowired 
	OrganizationRepository organizationRepository;
	
	@Autowired
	OrganizationRepository OrganizationRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	LocationService locationService;
	@Autowired
	AreaService areaService;
	@Autowired
	NotificationService notificationService;
	
	@Autowired 
	NotificationRepository notificationRepository;
	
	@Autowired
	TalukaService talukaService;
	
	
	// 
	// @Autowired
	// DomainMapper domainMapper;
	private static final Gson gson = new Gson();
	
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
	
	public ResponseEntity<String> saveDrive(DriveDto driveDto) {
		Drive drive= this.toDrive(driveDto);
		
		if(driveDto.getStringOfImage()!=null) {
			driveDto.setByteImage(Base64.getDecoder().decode(driveDto.getStringOfImage()));
			}
		

		if(Constant.censoring.equals("yes")) {
			
			TextFilter tf = new TextFilter();
		    String adequateCensored= tf.adequateCensor(driveDto.getDescription(), driveDto.getCensoredDescription());
		    drive.setDescription(adequateCensored);
		    
		}
		    
		
		drive.setUpvotes(0);
		drive.setVolunteers(0);
		LocalDateTime today = LocalDateTime.now();
		drive.setCreatedAt(today);
		//ModelMapper modelMapper = new ModelMapper();
		// 
		// Drive drive = new Drive();
		// drive.setDescription(driveDto.getDescription());
		// 
		// Location location = new Location();
		// 
		// location.setLatitude(driveDto.getLocationDto().getLatitude());
		// 
		// drive.setLocation(location);
		// 
		// Sector sector= new Sector();
		// sector.setName(driveDto.getSectorDto().getName());
		// 
		// drive.setSector(sector);
		// drive.setCreatedAt(driveDto.getCreatedAt());
		//drive = DomainMapper.INSTANCE.toDrive(driveDto);
		//Drive drive = modelMapper.map(driveDto, Drive.class);
		Sector sector = sectorRepository.findById(driveDto.getSector().getSectorId()).orElse(null);
		if(sector!=null) {
			drive.setSector(sector);
		}
		Organization organization = organizationRepository.findById(driveDto.getUserEmail()).orElseThrow(() -> new NullPointerException("Organization not found"));
		drive.setOrganization(organization);
		String addressSuccess = this.setDriveLocation(drive);
		if(addressSuccess.equals("Not Pune District")) {
			System.out.println("Not Pune");
			return new ResponseEntity<>(gson.toJson("Sorry we only serve in Pune District"),HttpStatus.NOT_ACCEPTABLE);
		}
		else if(addressSuccess.equals("Location not found")) {
			System.out.println("Location not found");
			//yutika save drive
			drive = driveRepository.save( drive);
			
			ArrayList<Notification> notiAl = new ArrayList<Notification>();
			for(User toUser : organization.getFollowingUsers()) {
				Notification notification = new Notification();
				notification.setToUser(toUser);
				notification.setByUser(drive.getOrganization());
				notification.setNotificationType("reminder");
				// This is posted a new drive for org u follow // diff processing  time calculate
				notification.setDescription(drive.getOrganization().getName()+" posted a new drive");
				// Set the created_at field to the current timestamp
				notification.setCreatedAt( LocalDateTime.now());
				notification.setDrive(drive);
				notiAl.add(notification);
				//notificationRepository.save(notification);
			}
			Notification notification = new Notification();
			notification.setToUser(drive.getOrganization());
			notification.setByUser(drive.getOrganization());
			notification.setNotificationType("toBeReminded");
			
			Integer position = drive.getWhenAt().toString().indexOf("T");
			String date =  drive.getWhenAt().toString().substring(0, position);
			String time = drive.getWhenAt().toString().substring(position+1);
			
			notification.setDescription("Reminder!!! Upcoming drive on "+date+" at "+time);
			// Set the created_at field to the current timestamp
			notification.setCreatedAt(drive.getWhenAt());
			notification.setDrive(drive);
			notiAl.add(notification);
			notificationRepository.saveAll(notiAl);

			
			
			 key = organization.getUserId() + today.toString()+".png";
				uploadImage(driveDto.getByteImage(),BUCKET, key,drive.getDriveId());
				System.out.println("Done");
				
				
				
				   ImageAnalyzer ia = new ImageAnalyzer();
				     String new_key=ia.detectLabels(BUCKET, drive.getImagePath());
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

				    		uploadImage(driveDto.getByteImage(),BUCKET, new_key,drive.getDriveId());
				 	

						 drive.setCreatedAt(today);
						drive = driveRepository.save( drive);
						
				    	 
				     }
			
			
			
			
			return new ResponseEntity<>(gson.toJson("Seems like details related to your location don't exist on OpenStreetMap. We will still drive your request, but wouldn't be able to guarantee immediate response. Address : ("+drive.getLocation().getLatitude()+", "+drive.getLocation().getLongitude()+"), "+drive.getLocation().getAreaName()+", "+drive.getLocation().getTaluka()+", "+drive.getLocation().getState()) ,HttpStatus.OK);
		}
		System.out.println("Successssssss4");
		System.out.println("Before save drive email"+ drive.getOrganization().getUserId());
		drive = driveRepository.save( drive);
		System.out.println("After save drive email"+ drive.getOrganization().getUserId());
		
		
		ArrayList<Notification> notiAl = new ArrayList<Notification>();
		for(User toUser : organization.getFollowingUsers()) {
			Notification notification = new Notification();
			notification.setToUser(toUser);
			notification.setByUser(drive.getOrganization());
			notification.setNotificationType("reminder");
			notification.setDescription(drive.getOrganization().getName()+" posted a new drive");
			// Set the created_at field to the current timestamp
			notification.setCreatedAt( LocalDateTime.now());
			notification.setDrive(drive);
			notiAl.add(notification);
			//notificationRepository.save(notification);
		}

		Notification notification = new Notification();
		notification.setToUser(drive.getOrganization());
		notification.setByUser(drive.getOrganization());
		notification.setNotificationType("toBeReminded");
		
		Integer position = drive.getWhenAt().toString().indexOf("T");
		String date =  drive.getWhenAt().toString().substring(0, position);
		String time = drive.getWhenAt().toString().substring(position+1);
		
		notification.setDescription("Reminder!!! Upcoming drive on "+date+" at "+time);
	
		// Set the created_at field to the current timestamp
		notification.setCreatedAt( drive.getWhenAt());
		notification.setDrive(drive);
		notiAl.add(notification);
		notificationRepository.saveAll(notiAl);
		
		  key = organization.getUserId() + today.toString()+".png";
			uploadImage(driveDto.getByteImage(),BUCKET, key,drive.getDriveId());
			System.out.println("Done");
			
		
			
			
			   ImageAnalyzer ia = new ImageAnalyzer();
			     String new_key=ia.detectLabels(BUCKET, drive.getImagePath());
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

			    		uploadImage(driveDto.getByteImage(),BUCKET, new_key,drive.getDriveId());
			 	

					 drive.setCreatedAt(today);
					drive = driveRepository.save( drive);
					
			    	 
			     }
			     
		
		
		return new ResponseEntity<>(gson.toJson("Successfully Driveed, Address : ("+drive.getLocation().getLatitude()+","+drive.getLocation().getLongitude()+"), "+drive.getLocation().getAreaName()+", "+drive.getLocation().getTaluka()+", "+drive.getLocation().getState()),HttpStatus.OK);
	}
	
	
	public void uploadImage(byte[] imageBytes, String bucketName, String key, Integer drive_id) //upload image object on s3 bucket
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
	        setImageUrlandSaveDrive(drive_id,key);
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

	
	
	
	public void setImageUrlandSaveDrive(Integer drive_id,String key)
	
	{
		for(int i=0;i<3;i++){
			
			Drive drive = driveRepository.findById(drive_id).orElseThrow( null );
		 if(drive!=null) {
			
				drive.setImagePath(key);
				drive = driveRepository.save( drive);
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

	
	public String setDriveLocation(Drive drive) {
		if(drive.getLocation().getTaluka()==null) {
			System.out.println("Successssssss2");
			return locationService.setAddressDetailsForDrive(drive);
		}
		ArrayList<Area> nearbyAreas = areaService.getNearbyAreas(drive.getLocation());
		System.out.println(" beforeeeeeeeeeeeeeeeeeeee ");
		
		System.out.println(" afterrrrrrrrrrrrrrrrrrr ");
		return "Save directly";
	}
	
	
	public String upvoteDrive(String user_id, Integer drive_id) {
		
		Drive drive = driveRepository.findById(drive_id).orElseThrow( null );
		
		if(drive.getUpvotedDrivesUsers()==null) {
			drive.setUpvotedDrivesUsers(new HashSet<User>());
		}
		User user =userRepository.findById(user_id).orElse(null);
		
		System.out.println("Conatins:"+ drive.getUpvotedDrivesUsers().contains(user));
		if( drive.getUpvotedDrivesUsers().contains(user)) {
			drive.getUpvotedDrivesUsers().remove(userRepository.findById(user_id).orElseThrow(null));
			drive.setUpvotes(drive.getUpvotes()-1);
			driveRepository.save(drive);
			return "Removed Upvote";
		}else {
			drive.getUpvotedDrivesUsers().add(userRepository.findById(user_id).orElseThrow(null));
			if(drive.getUpvotes()==null) {
				drive.setUpvotes(0);
			}
			drive.setUpvotes(drive.getUpvotes()+1);
			driveRepository.save(drive);
			
			
			Notification notification = new Notification();
			notification.setToUser(drive.getOrganization());
			notification.setByUser(user);
			notification.setNotificationType("upvoted");
			notification.setDescription(user.getName()+" upvoted your drive");
			// Set the created_at field to the current timestamp
			notification.setCreatedAt( LocalDateTime.now());
			notification.setDrive(drive);
			notificationRepository.save(notification);
			
			return "Upvoted Successfully";
		}
	}
	
public String volunteerDrive(String user_id, Integer drive_id) {
		
	Drive drive = driveRepository.findById(drive_id).orElseThrow( null );
	if(drive.getVolunteeredDrivesUsers()==null) {
	drive.setVolunteeredDrivesUsers(new HashSet<User>());
	}
	User user =userRepository.findById(user_id).orElse(null);
	System.out.println("Conatins:"+ drive.getVolunteeredDrivesUsers().contains(user));
	if( drive.getVolunteeredDrivesUsers().contains(user)) {
	drive.getVolunteeredDrivesUsers().remove(userRepository.findById(user_id).orElseThrow(null));
	drive.setVolunteers(drive.getVolunteers()-1);
	
	driveRepository.save(drive);
	return "Removed volunteer";
	}else {
		
	drive.getVolunteeredDrivesUsers().add(userRepository.findById(user_id).orElseThrow(null));
	
	if(drive.getVolunteers()==null) {
	drive.setVolunteers(0);
	}
	drive.setVolunteers(drive.getVolunteers()+1);
	driveRepository.save(drive);
	
	
	ArrayList<Notification> notiAl = new ArrayList<Notification>();
	Notification notification = new Notification();
	notification.setToUser(drive.getOrganization());
	notification.setByUser(user);
	notification.setNotificationType("volunteer");
	notification.setDescription(user.getName()+" volunteered for your drive");
	// Set the created_at field to the current timestamp
	notification.setCreatedAt( LocalDateTime.now());
	notification.setDrive(drive);
	notiAl.add(notification);
	//notificationRepository.save(notification);
	Notification notiToVolunteer = new Notification();
	notiToVolunteer.setToUser(user);
	notiToVolunteer.setByUser(drive.getOrganization());
	notiToVolunteer.setNotificationType("toBeReminded");
	
	Integer position = drive.getWhenAt().toString().indexOf("T");
	String date =  drive.getWhenAt().toString().substring(0, position);
	String time = drive.getWhenAt().toString().substring(position+1);
	
	notiToVolunteer.setDescription("Reminder!!! Upcoming drive on "+date+" at "+time);
	
	// Set the created_at field to the current timestamp
	notiToVolunteer.setCreatedAt( drive.getWhenAt());
	notiToVolunteer.setDrive(drive);
	notiAl.add(notiToVolunteer);
	//notificationRepository.save(notiToVolunteer);
	notificationRepository.saveAll(notiAl);
	
	
	
	
	return "Volunteered Successfully";
	}

	}

	public String saveDrive(String organization_id, Integer drive_id) {
		Drive drive = driveRepository.findById(drive_id).orElseThrow( null );
		if(drive.getSavedDrivesUsers()==null) {
			drive.setSavedDrivesUsers(new HashSet<User>());
		}
		User user =userRepository.findById(organization_id).orElse(null);
		System.out.println("Conatins:"+ drive.getSavedDrivesUsers().contains(user));
		if( drive.getSavedDrivesUsers().contains(user)) {
			drive.getSavedDrivesUsers().remove(userRepository.findById(organization_id).orElseThrow(null));
			driveRepository.save(drive);
			return "Unsaved";
		}else {
			drive.getSavedDrivesUsers().add(userRepository.findById(organization_id).orElseThrow(null));
			driveRepository.save(drive);
			return "Saved";
		}
	}
	
	
	



	
	
	
	
	
	
	
	public DriveDto toDriveDto(Drive drive) {
		ModelMapper modelMapper = new ModelMapper();
		String areaId;
		areaId = drive.getLocation().getAreaId();
		drive.getLocation().setAreaId(null);
		DriveDto driveDto=modelMapper.map(drive,DriveDto.class);
		driveDto.getLocation().setAreaId(areaId);
		driveDto.setUserEmail(drive.getOrganization().getUserId());
		driveDto.setName(drive.getOrganization().getName());
		driveDto.setImagePath(drive.getImagePath());
		driveDto.setProfileImg(drive.getOrganization().getProfileImg());

	
		return driveDto;
	}
	public Drive toDrive(DriveDto driveDto) {
		String areaId;
		areaId = driveDto.getLocation().getAreaId();
		ModelMapper modelMapper = new ModelMapper();
		driveDto.getLocation().setAreaId(null);
		Drive drive=modelMapper.map(driveDto,Drive.class);
		drive.getLocation().setAreaId(areaId);
		return drive;
	}
	public Integer getMyUpvote(String user_id,Drive drive) {
		User user =userRepository.findById(user_id).orElse(null);
		System.out.println("Conatins my upvote:"+ drive.getUpvotedDrivesUsers().contains(user));
//		for(User u : drive.getUpvotedDrivesUsers()) {
//			System.out.println("Upvoted organization "+ u.getUserId());
//		}
		if( drive.getUpvotedDrivesUsers().contains(user)) {
			System.out.println("Conatinsssssssssssssssssssssssssss"+ drive.getUpvotedDrivesUsers().contains(user));
			return 1;
		}else {
			System.out.println("notttttttttttttt conatinsss:"+ drive.getUpvotedDrivesUsers().contains(user));
			return 0;
		}
	}
	public Integer getMyVolunteerStatus(String user_id,Drive drive) {
		User user =userRepository.findById(user_id).orElse(null);
		System.out.println("Conatins my upvote:"+ drive.getUpvotedDrivesUsers().contains(user));
//		for(User u : drive.getVolunteeredDrivesUsers()) {
//			System.out.println("Upvoted organization "+ u.getUserId());
//		}
		if( drive.getVolunteeredDrivesUsers().contains(user)) {
			return 1;
		}else {
			return 0;
		}
	}

		
	public List<DriveDto> getDrivebyLocationAndSectorMostPopular(String user_id, List<String> sectors, List<String> talukas,
			List<String> areas,Integer page,Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Drive> drives = driveRepository.getByLocationAndSectorMostPopular(sectors, talukas, areas,pageable).getContent();
		List<DriveDto> driveDtos = new ArrayList<DriveDto>();
		for(Drive p:drives) {
			DriveDto driveDto= this.toDriveDto(p);
			driveDto.setMyUpvote(this.getMyUpvote(user_id, p));
			
			driveDto.setMyVolunteerStatus(this.getMyVolunteerStatus(user_id, p));
			System.out.println("drive dto my upvote" + driveDto.getMyUpvote());
			driveDtos.add(driveDto);
		}
		return driveDtos;
	}
	public List<DriveDto> getDrivebyLocationAndSectorMostRecent(String user_id, List<String> sectors, List<String> talukas,
			List<String> areas,Integer page,Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Drive> drives = driveRepository.getByLocationAndSectorMostRecent(sectors, talukas, areas,pageable).getContent();
		List<DriveDto> driveDtos = new ArrayList<DriveDto>();
		for(Drive p:drives) {
			System.out.println(p.getDriveId());
			DriveDto driveDto= this.toDriveDto(p);
			driveDto.setMyVolunteerStatus(this.getMyVolunteerStatus(user_id, p));
			driveDto.setMyUpvote(this.getMyUpvote(user_id, p));
			driveDtos.add(driveDto);
		}
		return driveDtos;
	}
	public List<DriveDto> getDrivebySectorMostRecent(String user_id, List<String> sectors,Integer page,Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Drive> drives = driveRepository.getBySectorMostRecent(sectors,pageable).getContent();
		List<DriveDto> driveDtos = new ArrayList<DriveDto>();
		for(Drive p:drives) {
			System.out.println(p.getDriveId());
			DriveDto driveDto= this.toDriveDto(p);
			driveDto.setMyUpvote(this.getMyUpvote(user_id, p));
			driveDto.setMyVolunteerStatus(this.getMyVolunteerStatus(user_id, p));
			driveDtos.add(driveDto);
		}
		return driveDtos;
	}
	public List<DriveDto> getDrivebySectorMostPopular(String user_id, List<String> sectors,Integer page,Integer size) {
		// int page = 0;
		// int size = 3;
		Pageable pageable = PageRequest.of(page, size);
		List<Drive> drives = driveRepository.getBySectorMostPopular(sectors,pageable).getContent();
		List<DriveDto> driveDtos = new ArrayList<DriveDto>();
		for(Drive p:drives) {
			System.out.println(p.getDriveId());
			DriveDto driveDto= this.toDriveDto(p);
			driveDto.setMyUpvote(this.getMyUpvote(user_id, p));
			driveDto.setMyVolunteerStatus(this.getMyVolunteerStatus(user_id, p));
			System.out.println("drive dto my upvote" + driveDto.getMyUpvote());
			driveDtos.add(driveDto);
		}
		return driveDtos;
	}
	public List<DriveDto> getDrivebyLocationMostRecent(String user_id, List<String> talukas, List<String> areas,Integer page,Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Drive> drives = driveRepository.getByLocationMostRecent(talukas,areas,pageable ).getContent();
		List<DriveDto> driveDtos = new ArrayList<DriveDto>();
		for(Drive p:drives) {
			System.out.println(p.getDriveId());
			DriveDto driveDto= this.toDriveDto(p);
			driveDto.setMyVolunteerStatus(this.getMyVolunteerStatus(user_id, p));
			driveDto.setMyUpvote(this.getMyUpvote(user_id, p));
			driveDtos.add(driveDto);
		}
		return driveDtos;
	}
	public List<DriveDto> getDrivebyLocationMostPopular(String user_id, List<String> talukas, List<String> areas,Integer page,Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Drive> drives = driveRepository.getByLocationMostPopular(talukas,areas,pageable).getContent();
		List<DriveDto> driveDtos = new ArrayList<DriveDto>();
		for(Drive p:drives) {
			System.out.println(p.getDriveId());
			DriveDto driveDto= this.toDriveDto(p);
			driveDto.setMyVolunteerStatus(this.getMyVolunteerStatus(user_id, p));
			driveDto.setMyUpvote(this.getMyUpvote(user_id, p));
			driveDtos.add(driveDto);
		}
		return driveDtos;
	}

	public List<DriveDto> getDrivebyDefaultMostPopular(String user_id, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Drive> drives = driveRepository.getByDeafultMostPopular(pageable).getContent();
		List<DriveDto> driveDtos = new ArrayList<DriveDto>();
		for(Drive p:drives) {
			System.out.println(p.getDriveId());
			DriveDto driveDto= this.toDriveDto(p);
			driveDto.setMyVolunteerStatus(this.getMyVolunteerStatus(user_id, p));
			driveDto.setMyUpvote(this.getMyUpvote(user_id, p));
			driveDtos.add(driveDto);
		}
		return driveDtos;
		
		
	}

	public DriveDto getDrive(String user_id, Integer drive_id) {
	
		Drive drive = driveRepository.findById(drive_id).orElseThrow(() -> new NullPointerException("Drive not found"));
		
			DriveDto driveDto= this.toDriveDto(drive);
			driveDto.setMyVolunteerStatus(this.getMyVolunteerStatus(user_id, drive));
			driveDto.setMyUpvote(this.getMyUpvote(user_id, drive));
		
		
		return driveDto;
	}

	public List<DriveDto> getDrivebyMyLocalityMostRecent(String user_id, LocationDto locationDto, Integer page,
			Integer size) {
		
		
		Pageable pageable = PageRequest.of(page, size);
		String taluka;
		if(locationDto.getLatitude()==null || locationDto.getLongitude()==null) {
			
			taluka="Pune City";
			
		}else {
			
			taluka= talukaService.getTalukaFromLatLng(locationDto);
			
		}
		
		 List<String> talukas = new ArrayList<String>();
		 talukas.add(taluka);
		 List<String> areas = new ArrayList<String>();
		 
		
		List<Drive> drives = driveRepository.getByLocationMostRecent(talukas,areas,pageable ).getContent();
		List<DriveDto> driveDtos = new ArrayList<DriveDto>();
		for(Drive p:drives) {
			System.out.println(p.getDriveId());
			DriveDto driveDto= this.toDriveDto(p);
			driveDto.setMyVolunteerStatus(this.getMyVolunteerStatus(user_id, p));
			driveDto.setMyUpvote(this.getMyUpvote(user_id, p));
			driveDtos.add(driveDto);
		}
		return driveDtos;
	
	}
	
}

