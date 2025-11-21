package com.socioFix.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.socioFix.Dto.NotificationDto;
import com.socioFix.model.Location;
import com.socioFix.model.Organization;
import com.socioFix.model.Post;
import com.socioFix.model.Sector;
import com.socioFix.model.User;
import com.socioFix.model.Notification.Notification;

import com.socioFix.model.geoLocations.Area;
import com.socioFix.model.geoLocations.Taluka;
import com.socioFix.repository.AreaRepository;
import com.socioFix.repository.NotificationRepository;
import com.socioFix.repository.OrganizationRepository;
import com.socioFix.repository.PostRepository;
import com.socioFix.repository.SectorRepository;
import com.socioFix.repository.TalukaRepository;
import com.socioFix.repository.UserRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import java.util.Set;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//@Service
//public class NotificationService {
//
//	@Autowired
//	private OrganizationRepository organizationRepository;
//	
//	@Autowired
//	private AreaRepository areaRepository;
//	
//	public List setNotificationList(ArrayList<Area> areas, Location loc, Post post) {
//		List<Notification> Notifications = new ArrayList<>();
//	    HashSet<String> orgIds = new HashSet<>();
//	    System.out.println("  111111111111111111111111111111111111   ");
//	    if (areas.isEmpty()) {
//	    	System.out.println("  2222222222222222222222222222222222   ");
//	        return null;
//	    }
//	    
//	    
//	    for (Area area : areas) {
//	    	Area areaFromDb = areaRepository.findById(area.getAreaId()).orElse(null);
//	    	System.out.println("  333333333333333333333333333333333  ");
//	    	if (areaFromDb == null) {
//	    	    // Area does not exist in the database
//	    		System.out.println("  4444444444444444444444444444444444   ");
//	    	    continue;
//	    	}
//	    	Set<Organization> orgs = areaFromDb.getAllOrganizationsServingInAreas();
//	        for (Organization org : orgs) {
//	            String orgId = org.getUserId();
//	            System.out.println(orgId+"   "+ org.getName());
//	            if (!orgIds.contains(orgId)) {
//	                orgIds.add(orgId);
//	                ToAcceptNotification notification = new ToAcceptNotification();
//	                
//	                //Notification notification = new Notification();
//	                
//	                notification.setPost(post);
//	                notification.setToUser(org);
//	                // Set the created_at field to the current timestamp
//	                notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//	                Notifications.add(notification);
//	            }
//	        }
//	    }
//	    return Notifications;
//	}
//
//}


//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.socioFix.model.Location;
//import com.socioFix.model.Organization;
//import com.socioFix.model.Post;
//import com.socioFix.model.Sector;
//import com.socioFix.model.Notification.Notification;
//import com.socioFix.model.Notification.ToAcceptNotification;
//import com.socioFix.model.geoLocations.Area;
//import com.socioFix.model.geoLocations.Taluka;
//import com.socioFix.repository.AreaRepository;
//import com.socioFix.repository.OrganizationRepository;
//import com.socioFix.repository.TalukaRepository;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
import java.util.Optional;
//import java.util.Set;
//
//import org.json.JSONObject;
//import java.sql.Timestamp;

@EnableScheduling
@Service
public class NotificationService {

	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private TalukaRepository talukaRepository;
	
	
	@Autowired
	PostRepository postRepository;
	
	@Autowired 
	SectorRepository sectorRepository;
	
	@Autowired 
	UserRepository userRepository;
	
	@Autowired 
	NotificationRepository notificationRepository;
	
	private static final Gson gson = new Gson();


	
	//@Service
//	public class NotificationService {
//		  
//		  @Autowired
//		  private NotificationRepository notificationRepository;
//
//		  @Scheduled(cron = "0 0 12 * * *") // run every day at noon
//		  public void updateNotificationType() {
//		    List<Notification> notifications = notificationRepository.findByNotificationType("toBeReminded");
//		    LocalDateTime now = LocalDateTime.now();
//		    LocalDateTime oneDayAhead = now.plusDays(1);
//		    for (Notification notification : notifications) {
//		      if (notification.getWhenAt().isBefore(oneDayAhead)) {
//		        notification.setNotificationType("reminder");
//		        notificationRepository.save(notification);
//		      }
//		    }
//		  }
//		}

//	  @Scheduled(cron = "0 53 15 * * *") // run every day at noon
	  @Scheduled(cron = "0 15 18 * * *")
	  public void updateNotificationType() {
		  System.out.println("in update notification");
	    List<Notification> notifications = notificationRepository.findByNotificationType("toBeReminded");
	    System.out.println("notifications size*******************************************************"+ notifications.size())  ;
	    LocalDate todayDate = LocalDateTime.now().toLocalDate();
	    LocalDateTime whenAt;
	    LocalDate whenAtDate;
	    Duration duration ;
	    for (Notification notification : notifications) {
	    	
	    	  
	    	   
	   	  
	    	 whenAt = notification.getCreatedAt();
	    	  whenAtDate = whenAt.toLocalDate();
	    	   duration = Duration.between(todayDate.atStartOfDay(), whenAtDate.atStartOfDay());
		   	    long days = Math.abs(duration.toDays());

	    	if (days==1) {
	    	    System.out.println("The dates are 1 day apart");
	    	    notification.setNotificationType("reminder");
		        notificationRepository.save(notification);
	    	} else {
	    	    System.out.println("The dates are not 1 day apart");
	    	}
	    
	    }
	  }
	
//	public List setNotificationList(ArrayList<Area> areas, Location loc) {
//		List<Notification> Notifications = new ArrayList<>();
//	    HashSet<String> orgIds = new HashSet<>();
//	    System.out.println("  111111111111111111111111111111111111   ");
//	    if (areas.isEmpty()) {
//	    	System.out.println("  2222222222222222222222222222222222   ");
//	        return null;
//	    }
//	    
//	    
//	    for (Area area : areas) {
//	    	Area areaFromDb = areaRepository.findById(area.getAreaId()).orElse(null);
//	    	System.out.println("  333333333333333333333333333333333  ");
//	    	if (areaFromDb == null) {
//	    	    // Area does not exist in the database
//	    		System.out.println("  4444444444444444444444444444444444   ");
//	    	    continue;
//	    	}
//	    	Set<Organization> orgs = areaFromDb.getAllOrganizationsServingInAreas();
//	        for (Organization org : orgs) {
//	            String orgId = org.getUserId();
//	            System.out.println(orgId+"   "+ org.getName());
//	            if (!orgIds.contains(orgId)) {
//	                orgIds.add(orgId);
//	                ToAcceptNotification notification = new ToAcceptNotification();
//	                //notification.setPost(post);
//	                notification.setToUser(org);
//	                // Set the created_at field to the current timestamp
//	                notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//	                Notifications.add(notification);
//	            }
//	        }
//	    }
//	    return Notifications;
//	}
	
	public Set setNotificationSet(ArrayList<Area> areas, Location loc, Sector sector, User fromUser) {
		System.out.println("HEREEEEE");
		Set<Notification> Notifications = new HashSet<>();

		System.out.println(loc.getTaluka()+" ");
		System.out.println(talukaRepository);
		//String currTaluka = loc.getTaluka();
		Taluka talukaFromDb = talukaRepository.findById(loc.getTaluka()).orElse(null);
		System.out.println(talukaFromDb.getTalukaPolygon());
		//		Optional<Taluka> optionalTaluka = talukaRepository.findById(loc.getTaluka());
		//		Taluka talukaFromDb;
		//		if (optionalTaluka.isPresent()) {
		//		 talukaFromDb = optionalTaluka.get();
		//		 // continue with the code
		//		} else {
		//		 // handle the case where the taluka is not found in the database
		//			 talukaFromDb = null;
		//		}
		System.out.println(" 333333333333333333333333333333333 ");
		Notification dummyn = new Notification();
		if (talukaFromDb != null) {
			// Taluka exists in the database
			System.out.println(" 4444444444444444444444444444444444 ");
			Set<Organization> orgs = talukaFromDb.getAllOrganizationsServingInTalukas();

			for (Organization org : orgs) {
				System.out.println(org.getUserId()+" "+ org.getName());
				dummyn.setToUser(org);
				System.out.println(org.getUserId()+ " absence "+ !Notifications.contains(dummyn));
				if (!Notifications.contains(dummyn) && org.getSectors().contains(sector)) {
					//if (!Notifications.contains(org.getUserId()) ) {
					Notification notification = new Notification();
					notification.setToUser(org);
					notification.setByUser(fromUser);
					notification.setNotificationType("toAccept");
					notification.setDescription("Somebody needs your help...");
					// Set the created_at field to the current timestamp
					notification.setCreatedAt( LocalDateTime.now());
					Notifications.add(notification);
				}
			}
		}
		else {
			return null;
		}

		System.out.println(" 111111111111111111111111111111111111 ");
		if (areas.isEmpty()) {
			System.out.println(" 2222222222222222222222222222222222 ");
			return Notifications;
		}


		for (Area area : areas) {
			Area areaFromDb = areaRepository.findById(area.getAreaId()).orElse(null);
			System.out.println(" 333333333333333333333333333333333 ");
			if (areaFromDb == null) {
				// Area does not exist in the database
				System.out.println(" 4444444444444444444444444444444444 ");
				continue;
			}
			Set<Organization> orgs = areaFromDb.getAllOrganizationsServingInAreas();
			for (Organization org : orgs) {
				System.out.println(org.getUserId()+" "+ org.getName());
				dummyn.setToUser(org);
				System.out.println(org.getUserId()+ " absence "+ !Notifications.contains(dummyn));
				if (!Notifications.contains(dummyn) && org.getSectors().contains(sector)) {
					//if (!Notifications.contains(org.getUserId()) ) {
					Notification notification = new Notification();
					notification.setToUser(org);
					notification.setByUser(fromUser);
					notification.setNotificationType("toAccept");
					notification.setDescription("Somebody needs your help...");
					// Set the created_at field to the current timestamp
					notification.setCreatedAt( LocalDateTime.now());
					Notifications.add(notification);
				}
			}
		}
		return Notifications;
	}


	
	
	public ResponseEntity<String> saveNotification(NotificationDto notificationDto){
		 ModelMapper modelMapper = new ModelMapper();
		 //Integer postId = notificationDto.getPost().getPostId();
		 //notificationDto.getPost().setPostId(null);
		 
		
		 
		 Post post = postRepository.findById(notificationDto.getPost().getPostId()).orElse(null);
		 User toUser = userRepository.findById(notificationDto.getToUser().getUserId()).orElse(null);
		 User byUser = userRepository.findById(notificationDto.getByUser().getUserId()).orElse(null);
		Notification notification =modelMapper.map(notificationDto,Notification.class);
		//notification.getPost().setPostId(postId);
 if(notificationDto.getNotificationType().equals("contribute")) {
			 
	 String regex="(^)";
			 String specialDescription= byUser.getName()+regex+byUser.getUserId()+regex+byUser.getContactNo()+regex+notificationDto.getDescription()+regex+"Your post has received help from individual user. Click to check it out!";
			notification.setDescription(specialDescription);
		 }
 
		notification.setPost(post);
		notification.setByUser(byUser);
		notification.setToUser(toUser);
		System.out.println("My notiiiii obj"+notification);
		System.out.println("My notiiiii obj"+notification.getPost().getPostId());
		notificationRepository.save(notification);
		System.out.println("Afterrrrr saveeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
		return new ResponseEntity<>(gson.toJson("Successfully saved"),HttpStatus.ACCEPTED);
	}


	

}
