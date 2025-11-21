package com.socioFix.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.socioFix.model.Drive;
import com.socioFix.model.Location;
import com.socioFix.model.Post;
import com.socioFix.model.geoLocations.Area;
import java.util.ArrayList;
import org.json.JSONObject;
@Service
public class LocationService {
	private String allowed = "Pune District";
	private String notFound = "Location not found";
	private String notAllowed = "Not Pune District";
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private NotificationService notificationService;
	
	
	public String setAddressDetails(Post post) {
		Location loc = post.getLocation();
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("locatioooonnnn "+ loc.getLatitude()+" " + loc.getLongitude());
	
		//String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + loc.getLatitude() + "&lon=" + loc.getLongtitiude() + "&zoom=18&addressdetails=1";
		String response=null;
		int trials =0;
		while(response==null && trials <4) {
		
		
			trials++;
		
			
			try {
				
		
		String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + loc.getLatitude() + "&lon=" + loc.getLongitude() + "&zoom=18&addressdetails=1";
		System.out.println("URL" +url);
		 response = restTemplate.getForObject(url, String.class);
		// Use a JSON parser to extract the necessary information and set it in your Location object
	
		 
			}catch(Exception e) {
				System.out.println("time outtt ");
				
			}
			 System.out.println("Responseeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"+ response);
			 
			 if(response!=null && response.toString().charAt(0)!='{') {
				 response = null;
			 }
			 
			 
			}
		
		JSONObject json = new JSONObject(response);
		//String area = json.getJSONObject("address").getString("city");
		loc.setState(json.getJSONObject("address").getString("state"));
		if(json.getJSONObject("address").has("county")) {
			loc.setTaluka(json.getJSONObject("address").getString("county"));
		}
		else if(json.getJSONObject("address").has("city")){
			if(json.getJSONObject("address").getString("city")=="Kalyani Nagar") {
				System.out.println("In kalyani nagar");
				loc.setTaluka("Pune City");
			}
			else {
					System.out.println(json.getJSONObject("address").getString("city"));
					loc.setTaluka(json.getJSONObject("address").getString("city"));
			}
		}
		
	
		
		String taluka = loc.getTaluka();
		
		System.out.println(taluka);
		if(taluka!=null && (taluka.equals("Baramati") ||taluka.equals("Daund") ||taluka.equals("Indapur") ||taluka.equals("Bhor") ||taluka.equals("Purandhar") ||taluka.equals("Velhe") ||taluka.equals("Haveli") ||taluka.equals("Pune City") ||taluka.equals("Khed") ||taluka.equals("Ambegaon") ||taluka.equals("Junnar ") ||taluka.equals("Shirur") ||taluka.equals("Mawal") ||taluka.equals("Mulshi")||taluka.equals("Pune"))) {
			System.out.println("Successsss Allowed");
			ArrayList<Area> nearbyAreas = areaService.getNearbyAreas(loc);
			
			
			System.out.println("  beforeeeeeeeeeeeeeeeeeeee   ");
			post.setNotifications(notificationService.setNotificationSet(nearbyAreas, loc, post.getSector(),post.getUser()));
			System.out.println("  afterrrrrrrrrrrrrrrrrrrrrr   ");
			
			if(nearbyAreas!=null) {
				
				Area nearestArea = areaService.findNearestArea(loc,nearbyAreas);
				
				loc.setAreaName(nearestArea.getAreaName());
				loc.setAreaId(nearestArea.getAreaId());
				return allowed;
			}
			return notFound;
		}
		System.out.println("Not Allowed");
		return notAllowed;
		
	}
	
	public String setAddressDetailsForDrive(Drive drive) {
		Location loc = drive.getLocation();
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("locatioooonnnn "+ loc.getLatitude()+" " + loc.getLongitude());
		//String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + loc.getLatitude() + "&lon=" + loc.getLongtitiude() + "&zoom=18&addressdetails=1";
		String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + loc.getLatitude() + "&lon=" + loc.getLongitude() + "&zoom=18&addressdetails=1";
		System.out.println("URL" +url);
		String response = restTemplate.getForObject(url, String.class);
		// Use a JSON parser to extract the necessary information and set it in your Location object
		JSONObject json = new JSONObject(response);
		//String area = json.getJSONObject("address").getString("city");
		loc.setState(json.getJSONObject("address").getString("state"));
		if(json.getJSONObject("address").has("county")) {
			loc.setTaluka(json.getJSONObject("address").getString("county"));
		}
		else if(json.getJSONObject("address").has("city")){
			if(json.getJSONObject("address").getString("city")=="Kalyani Nagar") {
				System.out.println("In kalyani nagar");
				loc.setTaluka("Pune City");
			}
			else {
				System.out.println(json.getJSONObject("address").getString("city"));
				loc.setTaluka(json.getJSONObject("address").getString("city"));
			}
		}
		String taluka = loc.getTaluka();
		System.out.println(taluka);
		if(taluka!=null && (taluka.equals("Baramati") ||taluka.equals("Daund") ||taluka.equals("Indapur") ||taluka.equals("Bhor") ||taluka.equals("Purandhar") ||taluka.equals("Velhe") ||taluka.equals("Haveli") ||taluka.equals("Pune City") ||taluka.equals("Khed") ||taluka.equals("Ambegaon") ||taluka.equals("Junnar ") ||taluka.equals("Shirur") ||taluka.equals("Mawal") ||taluka.equals("Mulshi")||taluka.equals("Pune"))) {
			System.out.println("Successsss Allowed");
			ArrayList<Area> nearbyAreas = areaService.getNearbyAreas(loc);
			System.out.println(" beforeeeeeeeeeeeeeeeeeeee ");
		
			System.out.println(" afterrrrrrrrrrrrrrrrrrrrrr ");
			if(nearbyAreas!=null) {
				Area nearestArea = areaService.findNearestArea(loc,nearbyAreas);
				loc.setAreaName(nearestArea.getAreaName());
				loc.setAreaId(nearestArea.getAreaId());
				return allowed;
			}
			return notFound;
		}
		System.out.println("Not Allowed");
		return notAllowed;
	}


	
	
	
	
	
	
	
	
	
}
