package com.socioFix.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.socioFix.Dto.LocationDto;
import com.socioFix.model.geoLocations.Taluka;
import com.socioFix.repository.TalukaRepository;


@Service
public class TalukaService {

	@Autowired
	TalukaRepository talukaRepository;
	
	public static ModelMapper modelMapper = new ModelMapper();
	
	public List<String> getTalukaNames() {
		
		//List<String> talukaNames = new ArrayList<>();
	List<String> talukaNames = talukaRepository.findAllTalukaIds();
	System.out.println(talukaNames);
	return talukaNames ;
	
	}
	
	
	public Taluka getTalukaFromId(String taluka_id) {
		Optional<Taluka> talukaOptional = talukaRepository.findById(taluka_id);

        Taluka taluka = talukaOptional.get();
        //PGpolygon polygon = taluka.getTalukaPolygon();
       // return  polygon.toString();
        return taluka;
	}

	public String getTalukaFromLatLng(LocationDto loc) {
		//Location loc = post.getLocation();
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
		String taluka ;
		if(loc.getTaluka()==null)
			taluka="Pune City";
		taluka = loc.getTaluka();
		System.out.println(taluka);
		
		if(taluka!=null && (taluka.equals("Baramati") ||taluka.equals("Daund") ||taluka.equals("Indapur") ||taluka.equals("Bhor") ||taluka.equals("Purandhar") ||taluka.equals("Velhe") ||taluka.equals("Haveli") ||taluka.equals("Pune City") ||taluka.equals("Khed") ||taluka.equals("Ambegaon") ||taluka.equals("Junnar ") ||taluka.equals("Shirur") ||taluka.equals("Mawal") ||taluka.equals("Mulshi")||taluka.equals("Pune"))) {
			System.out.println("Successsss Allowed");
		}
		return taluka;
	}

	

}
