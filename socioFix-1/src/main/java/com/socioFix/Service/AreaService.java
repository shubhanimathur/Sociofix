package com.socioFix.Service;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socioFix.model.Location;
import com.socioFix.model.geoLocations.Area;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;
@Service
public class AreaService {
	
	
//	public ArrayList<Area> getNearbyAreas(Location loc){
//		int radius = 5000;
//		String url = "https://overpass-api.de/api/interpreter?data=[out:json];(node[\"place\"=\"suburb\"](around:5000," + loc.getLatitude() + "," + loc.getLongitude() + ");node[\"place\"=\"village\"](around:5000," + loc.getLatitude() + "," + loc.getLongitude() + ");node[\"place\"=\"city\"](around:5000," + loc.getLatitude() + "," + loc.getLongitude() + ");node[\"place\"=\"town\"](around:5000," + loc.getLatitude() + "," + loc.getLongitude() + "););out;";
//		System.out.println(url);
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode response = null;
//		try {
//			response = mapper.readTree(new URL(url));
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ArrayList<Area> areas = new ArrayList<>();
//		if (response!=null && response.has("elements")) {
//			JsonNode elements = response.get("elements");
//			for (JsonNode element : elements) {
//				if (element.has("tags")) {
//					JsonNode tags = element.get("tags");
//					if (tags.has("place")) {
//						String place = tags.get("place").asText();
//						if (Arrays.asList("city", "town", "suburb", "village").contains(place)) {
//							Area area = new Area();
//							if(tags.has("name")&&tags.has("lat")&&tags.has("lon")&&tags.has("id")) {
//							area.setAreaName(tags.get("name").asText());
//							area.setLatitude(element.get("lat").asDouble());
//							area.setLongitude(element.get("lon").asDouble());
//							area.setAreaId(element.get("id").asText());
//							areas.add(area);
//							System.out.println(area.getAreaId()+"\n"+area.getLatitude()+"\n"+area.getLongitude()+"\n"+area.getAreaName()+"\n");
//							}
//						}
//					}
//				}
//			}
//			return areas;
//		}
//		return null;
//	}

	
	public ArrayList<Area> getNearbyAreas(Location loc){
		int radius = 5000;
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode response = null;
		
		int trials =0;
		while(response==null && trials <4) {
		
		
			trials++;
		
		
		String url = "https://overpass-api.de/api/interpreter?data=[out:json];(node[\"place\"=\"suburb\"](around:5000," + loc.getLatitude() + "," + loc.getLongitude() + ");node[\"place\"=\"village\"](around:5000," + loc.getLatitude() + "," + loc.getLongitude() + ");node[\"place\"=\"city\"](around:5000," + loc.getLatitude() + "," + loc.getLongitude() + ");node[\"place\"=\"town\"](around:5000," + loc.getLatitude() + "," + loc.getLongitude() + "););out;";
		
		
		
		System.out.println(url);
	
		try {
			response = mapper.readTree(new URL(url));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("time outtt ");
			e.printStackTrace();
		}
		
	
		 System.out.println("Responseeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"+ response);
		 if(response!=null && response.toString().charAt(0)!='{') {
			 response = null;
		 }
		 
		 
		}
		
		ArrayList<Area> areas = new ArrayList<>();
		System.out.println("Areas printttt karrriiiiiii ------ ****** "+areas);
		System.out.println(response!=null);
		System.out.println(response.has("elements"));
		if (response!=null && response.has("elements")) {
			JsonNode elements = response.get("elements");
			System.out.println("Elementssssssss:"+elements);
			for (JsonNode element : elements) {
				if (element.has("tags") && element.has("lat") && element.has("lon") && element.has("id")) {
					JsonNode tags = element.get("tags");
					System.out.println("TAGSSSS:"+tags);
					if (tags.has("place")) {
						System.out.println("in place........................................................");
						String place = tags.get("place").asText();
						System.out.println("place ...!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!..." +place);
						if (Arrays.asList("city", "town", "suburb", "village").contains(place)) {
							System.out.println("in in town suburb villaghe ..##########################################################");
							Area area = new Area();
							if(tags.has("name")) {
								
							
								
								System.out.println("in lat long  .////////////////////////////////////////////////////////////");
								area.setAreaName(tags.get("name").asText());
								area.setLatitude(element.get("lat").asDouble());
								area.setLongitude(element.get("lon").asDouble());
								area.setAreaId(element.get("id").asText());
								areas.add(area);
								System.out.println("*************************************************************************************");
								System.out.println(area.getAreaId()+"\n"+area.getLatitude()+"\n"+area.getLongitude()+"\n"+area.getAreaName()+"\n");
							}
						}
					}
				}
			}
			System.out.println("AREASSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS!!!!!!!!!!!!!!!----------"+areas);
			return areas;
		}
		return null;
	}


	public Area findNearestArea(Location loc, List<Area> nearbyAreas) {
		double lat1 = Math.toRadians(loc.getLatitude());
		double lon1 = Math.toRadians(loc.getLongitude());
		double minDistance = Double.POSITIVE_INFINITY;
		Area nearestArea = null;
		for (Area area : nearbyAreas) {
			double lat2 = Math.toRadians(area.getLatitude());
			double lon2 = Math.toRadians(area.getLongitude());
			double dLat = lat2 - lat1;
			double dLon = lon2 - lon1;
			double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
					Math.cos(lat1) * Math.cos(lat2) *
					Math.sin(dLon / 2) * Math.sin(dLon / 2);
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			double distance = 6371 * c; // Earth's radius in km
			if (distance < minDistance) {
				minDistance = distance;
				nearestArea = area;
			}
		}
		return nearestArea;
	}
}
