package com.socioFix.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.socioFix.Dto.DriveDto;
import com.socioFix.Dto.FollowingUserDto;
import com.socioFix.Dto.OrganizationDto;
import com.socioFix.Dto.TalukaDto;
import com.socioFix.Service.OrganizationService;
import com.socioFix.model.Organization;

import com.socioFix.model.Sector;
import com.socioFix.model.UserFollowingOrganization;
import com.socioFix.repository.OrganizationRepository;

import com.socioFix.repository.SectorRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@RestController
public class OrganizationController {

	@Autowired
	OrganizationService organizationService;
	
	@Autowired
	OrganizationRepository organizationRepository;
	
	@Autowired
	SectorRepository sectorRepository;
	
//	@Autowired
//	UserFollowingOrganization userFollowingOrganizationsRepository;
	
	

	public static ModelMapper modelMapper = new ModelMapper();
//	@PostMapping("/organization/save")
//	public ResponseEntity<OrganizationDto> organizationPost(@RequestBody OrganizationDto organizationDto) {
//	System.out.println("in controller");
//	//System.out.println(organizationDto.getBase_location().getLatitude());
//	organizationService.saveOrganization(organizationDto);
//	return ResponseEntity.ok(organizationDto);
//	}

//	@PostMapping("/organization/save")
//	@Transactional
//	public ResponseEntity<Organization> organizationPost(@RequestBody Organization organization) {
//		
//	System.out.println("in controller");
//	System.out.println("before org save ");
//	System.out.println(organization.getOrganizationId());
//	
//	Organization organization2 = organizationRepository.save(organization);
//
//	return ResponseEntity.ok(organization2);
//	}
	
	@PostMapping("/organization/save")
	@Transactional
	public ResponseEntity<OrganizationDto> organizationPost(@RequestBody OrganizationDto organizationDto) {
		
	System.out.println("in controller");
	System.out.println("before org save ");
	//System.out.println(organization.getOrganizationId());
	
	

	return ResponseEntity.ok(organizationService.saveOrganization(organizationDto));
	}
	
//	public ResponseEntity<OrganizationDto> organizationPost(@RequestBody OrganizationDto organizationDto) {
//		
//	System.out.println("in controller");
//	System.out.println("before org save ");
//	//System.out.println(organization.getOrganizationId());
//	
//	
//
//	return ResponseEntity.ok(organizationService.saveOrganization(organizationDto));
//	}


    @GetMapping("/organization/get")
    public ResponseEntity<OrganizationDto> organizationPost(@RequestParam String organization_id) {
    	System.out.println("in controller");
    	
    	OrganizationDto organizationDto= organizationService.getOrganization(organization_id);
    	
    	
    	
    	
    	return ResponseEntity.ok(organizationDto);
    	}
    
    
    
    
   
    
    @PostMapping("/organization/follow")
   	@Transactional
   	public ResponseEntity<Organization> organizationFollow1(@RequestBody Organization organization) {
   	
       	organization = organizationRepository.save(organization);
   //for(UserFollowingOrganization u : organization.getUserFollowingOrganizations()) {
//       		
//   			userFollowingOrganizationsRepository.save(u);
//       		
//       	}
//       	
    
   	return ResponseEntity.ok(organization);
       }
//	@Transactional
    
    @PostMapping("/organization/followUsers")
   	public ResponseEntity<List<String>> organizationFollow(@RequestParam String organization_id,@RequestParam String user_id) {
   	
       	String resultFollow= organizationService.organizationFollow(organization_id,user_id);
       	List<String> result = new ArrayList<String>();
		result.add(resultFollow);
    
return new ResponseEntity<>(result, HttpStatus.OK);
       }
    
    
    @PostMapping("/organization/followUsersStatus")
   	public ResponseEntity<List<String>> followOrganizationStatus(@RequestParam String organization_id,@RequestParam String user_id) {
   	
       	String resultFollow= organizationService.organizationFollowStatus(organization_id,user_id);
       	List<String> result = new ArrayList<String>();
		result.add(resultFollow);
    
return new ResponseEntity<>(result, HttpStatus.OK);
       }
    
    @GetMapping("/organization/getFollowingUsers")
	public ResponseEntity<List<FollowingUserDto>> getFollowingUsers(@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
		List<FollowingUserDto> followingUserDto= organizationService.getFollowingUsers(user_id,page,size);
		return ResponseEntity.ok(followingUserDto);
	}
	
    @GetMapping("/organization/getAcceptedPostsNo")
	public ResponseEntity<List<String>> getAcceptedPostsNo(@RequestParam String organization_id) {
		System.out.println("in controller");
	
		String acceptedPostsNo= organizationService.getAcceptedPostsNo(organization_id);
		List<String> result = new ArrayList<String>();
		result.add(acceptedPostsNo);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		

	}
    // @RequestParam Integer page, @RequestParam Integer size
    
    @PostMapping("/organization/getOrganizationsByLocationAndSector")
	public ResponseEntity<List<FollowingUserDto>> getOrganizationsByLocationAndSector (@RequestBody Map<String, Object> requestBody,
			 @RequestParam Integer page, @RequestParam Integer size) {
		List<String> sectors = (List<String>) requestBody.get("Sector");
		List<String> talukas = (List<String>) requestBody.get("Taluka");
		List<String> areas =(List<String>) requestBody.get("Area");
		
		List<FollowingUserDto> followingUserDtos = organizationService.getOrganizationsByLocationAndSector(sectors,talukas,areas,page,size);
	
		return ResponseEntity.ok(followingUserDtos);
	}
    
    @PostMapping("/organization/getOrganizationsByLocation")
	public ResponseEntity<List<FollowingUserDto>> getOrganizationsByLocation (@RequestBody Map<String, Object> requestBody,
			 @RequestParam Integer page, @RequestParam Integer size) {
		List<String> sectors = (List<String>) requestBody.get("Sector");
		List<String> talukas = (List<String>) requestBody.get("Taluka");
		List<String> areas =(List<String>) requestBody.get("Area");
		
		List<FollowingUserDto> followingUserDtos = organizationService.getOrganizationsByLocation(talukas,areas,page,size);
		
		return ResponseEntity.ok(followingUserDtos);
	}
    @PostMapping("/organization/getOrganizationsBySector")
	public ResponseEntity<List<FollowingUserDto>> getOrganizationsBySector (@RequestBody Map<String, Object> requestBody,
			 @RequestParam Integer page, @RequestParam Integer size) {
		List<String> sectors = (List<String>) requestBody.get("Sector");
		List<String> talukas = (List<String>) requestBody.get("Taluka");
		List<String> areas =(List<String>) requestBody.get("Area");
		List<FollowingUserDto> followingUserDtos = organizationService.getOrganizationsBySector(sectors,page,size);
	
		return ResponseEntity.ok(followingUserDtos);
	}
    
    @GetMapping("/organization/getAllOrganizations")
   	public ResponseEntity<List<FollowingUserDto>> getAllOrganizations (
   			 @RequestParam Integer page, @RequestParam Integer size) {
   		
   		
   		List<FollowingUserDto> followingUserDtos = organizationService.getAllOrganizations(page,size);
   	
   		return ResponseEntity.ok(followingUserDtos);
   	}
    
    @GetMapping("/organization/getAllOrganizationsBySearch")
   	public ResponseEntity<List<FollowingUserDto>> getAllOrganizationsBySearch (@RequestParam String search,
   			 @RequestParam Integer page, @RequestParam Integer size) {
   		
   		
   		List<FollowingUserDto> followingUserDtos = organizationService.getAllOrganizationsBySearch(search,page,size);
   	
   		return ResponseEntity.ok(followingUserDtos);
   	}
    
    
    
    
  
}
    
	

