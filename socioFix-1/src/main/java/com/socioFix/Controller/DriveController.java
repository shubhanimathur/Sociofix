package com.socioFix.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.socioFix.Dto.DriveDto;
import com.socioFix.Dto.LocationDto;
import com.socioFix.Dto.PostDto;
import com.socioFix.Service.DriveService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class DriveController {
	@Autowired
	DriveService driveService;
	
	@PostMapping("/drive/write")
	public ResponseEntity<String> saveDrive(@RequestBody DriveDto driveDto) {
		System.out.println("in controller");
		ResponseEntity<String> resp = driveService.saveDrive(driveDto);
		return resp;
	}
	
	
	
	
	
	
	// @DriveMapping("/drive/write")
	// public void saveContact(HttpServletRequest request) throws IOException{
	// System.out.println("in controller");
	// //System.out.println(contact.firstName+ " "+ contact.lastName) ;
	//// System.out.println(driveDto.getLocationDto().getLatitude());
	//// System.out.println(driveDto.getSectorDto().getName());
	//// 
	//// driveService.saveDrive(driveDto);
	//// return ResponseEntity.ok(driveDto);
	// String payload = new BufferedReader(new InputStreamReader(request.getInputStream()))
	// .lines().collect(Collectors.joining("\n"));
	// 
	// System.out.println(payload);
	// 
	//
	// }
	// 
	// 
	// @GetMapping("/fetchContactList")
	// public ResponseEntity<List<User>> getAllContacts() {
	//
	// return ResponseEntity.ok(userRepository.findAll());
	//
	// }
	@PostMapping("/drive/upvote")
	public ResponseEntity<List<String>> upvoteDrive(@RequestParam String user_id,@RequestParam Integer drive_id) {
		System.out.println("in controller");
		String upvotedResult= driveService.upvoteDrive(user_id, drive_id);
		List<String> result = new ArrayList<String>();
		result.add(upvotedResult);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PostMapping("/drive/volunteer")
	public ResponseEntity<List<String>> volunteerDrive(@RequestParam String user_id,@RequestParam Integer drive_id) {
		System.out.println("in controller");
		String upvotedResult= driveService.volunteerDrive(user_id, drive_id);
		List<String> result = new ArrayList<String>();
		result.add(upvotedResult);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	@PostMapping("/drive/save")
	public ResponseEntity<List<String>> saveDrive(@RequestParam String user_id,@RequestParam Integer drive_id) {
		System.out.println("in controller");
		String savedResult= driveService.saveDrive(user_id, drive_id);
		List<String> result = new ArrayList<String>();
		result.add(savedResult);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	

	@GetMapping("/drive/get")
	public ResponseEntity<DriveDto> getDrive(@RequestParam String user_id,@RequestParam Integer drive_id) {
		
		System.out.println("in controller");
		DriveDto driveDto= driveService.getDrive(user_id,drive_id);
		
	 return new ResponseEntity<>(driveDto, HttpStatus.OK);
		

	}
	@PostMapping("/drive/getByMyLocality/mostRecent")
	public ResponseEntity<List<DriveDto>> getDrivebyMyLocalityMostRecent (@RequestBody LocationDto locationDto, 
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		
		List<DriveDto> driveDtos= driveService.getDrivebyMyLocalityMostRecent(user_id,locationDto,page,size);
		return ResponseEntity.ok(driveDtos);
	}
	
	
	@PostMapping("/drive/getByLocationAndSector/mostPopular")
	public ResponseEntity<List<DriveDto>> getDrivebyLocationAndSectorMostPopular (@RequestBody Map<String, Object> requestBody, 
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
		List<String> sectors = (List<String>) requestBody.get("Sector");
		List<String> talukas = (List<String>) requestBody.get("Taluka");
		List<String> areas =(List<String>) requestBody.get("Area");
		List<DriveDto> driveDtos= driveService.getDrivebyLocationAndSectorMostPopular(user_id,sectors,talukas,areas,page,size);
		return ResponseEntity.ok(driveDtos);
	}
	@PostMapping("/drive/getByLocationAndSector/mostRecent")
	public ResponseEntity<List<DriveDto>> getDrivebyLocationAndSectorMostRecent (@RequestBody Map<String, Object> requestBody,
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		List<String> sectors = (List<String>) requestBody.get("Sector");
		List<String> talukas = (List<String>) requestBody.get("Taluka");
		List<String> areas =(List<String>) requestBody.get("Area");
		List<DriveDto> driveDtos= driveService.getDrivebyLocationAndSectorMostRecent(user_id,sectors,talukas,areas,page,size);
		return ResponseEntity.ok(driveDtos);
	}
	@PostMapping("/drive/getBySector/mostRecent")
	public ResponseEntity<List<DriveDto>> getDrivebySectorMostRecent (@RequestBody Map<String, Object> requestBody, 
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		List<String> sectors = (List<String>) requestBody.get("Sector");
		List<DriveDto> driveDtos= driveService.getDrivebySectorMostRecent(user_id,sectors,page,size);
		return ResponseEntity.ok(driveDtos);
	}
	@PostMapping("/drive/getBySector/mostPopular")
	public ResponseEntity<List<DriveDto>> getDrivebySectorMostPopular (@RequestBody Map<String, Object> requestBody, 
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		List<String> sectors = (List<String>) requestBody.get("Sector");
		List<DriveDto> driveDtos= driveService.getDrivebySectorMostPopular(user_id,sectors,page,size);
		return ResponseEntity.ok(driveDtos);
	}
	@PostMapping("/drive/getByLocation/mostRecent")
	public ResponseEntity<List<DriveDto>> getDrivebyLocationMostRecent (@RequestBody Map<String, Object> requestBody, 
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		List<String> talukas = (List<String>) requestBody.get("Taluka");
		List<String> areas =(List<String>) requestBody.get("Area");
		List<DriveDto> driveDtos= driveService.getDrivebyLocationMostRecent(user_id,talukas,areas,page,size);
		return ResponseEntity.ok(driveDtos);
	}
	@PostMapping("/drive/getByLocation/mostPopular")
	public ResponseEntity<List<DriveDto>> getDrivebyLocationMostPopular (@RequestBody Map<String, Object> requestBody,
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		List<String> talukas = (List<String>) requestBody.get("Taluka");
		List<String> areas =(List<String>) requestBody.get("Area");
		List<DriveDto> driveDtos= driveService.getDrivebyLocationMostPopular(user_id,talukas,areas,page,size);
		return ResponseEntity.ok(driveDtos);
	}
	
	@PostMapping("/drive/getByDefault/mostPopular")
	public ResponseEntity<List<DriveDto>> getDrivebyDeafultMostPopular (
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		
		List<DriveDto> driveDtos= driveService.getDrivebyDefaultMostPopular(user_id,page,size);
		return ResponseEntity.ok(driveDtos);
	}
	
}

