package com.socioFix.Controller;

import org.springframework.web.bind.annotation.PostMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.socioFix.Dto.PostDto;
import com.socioFix.Dto.TalukaDto;
import com.socioFix.Dto.WinnerDto;

import com.socioFix.Dto.LocationDto;
import com.socioFix.Dto.NotificationDto;
import com.socioFix.Service.PostService;

import jakarta.servlet.http.HttpServletRequest;
@RestController
public class PostController {

	
	@Autowired
	PostService postService;
	
	@PostMapping("/post/write")
	public ResponseEntity<String> savePost(@RequestBody PostDto postDto) {
		System.out.println("in controller");
	
		ResponseEntity<String> resp = postService.savePost(postDto);
		return resp;
		

	}
	
	@PostMapping("/post/writeWithoutImage")
	public ResponseEntity<String> savePostWithoutImage(@RequestBody PostDto postDto) {
		System.out.println("in controller");
	
		ResponseEntity<String> resp = postService.savePostWithoutImage(postDto);
		return resp;
		

	}
	
	
//	@PostMapping("/post/write")
//	public void saveContact(HttpServletRequest request) throws IOException{
//		System.out.println("in controller");
//		//System.out.println(contact.firstName+ " "+ contact.lastName) ;
////		System.out.println(postDto.getLocationDto().getLatitude());
////		System.out.println(postDto.getSectorDto().getName());
////		
////		postService.savePost(postDto);
////		return ResponseEntity.ok(postDto);
//		 String payload = new BufferedReader(new InputStreamReader(request.getInputStream()))
//	                .lines().collect(Collectors.joining("\n"));
//		 
//		 System.out.println(payload);
//		
//
//	}
	
//	
//	
//	@GetMapping("/fetchContactList")
//	public ResponseEntity<List<User>> getAllContacts() {
//
//		return ResponseEntity.ok(userRepository.findAll());
//
//	}
	
	
	
	
	@PostMapping("/post/upvote")
	public ResponseEntity<List<String>> upvotePost(@RequestParam String user_id,@RequestParam Integer post_id) {
		System.out.println("in controller");
	
		String upvotedResult= postService.upvotePost(user_id, post_id);
		List<String> result = new ArrayList<String>();
		result.add(upvotedResult);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		

	}
	
	@PostMapping("/post/accept")
	public ResponseEntity<List<String>> toAcceptPost(@RequestParam String organization_id,@RequestParam Integer post_id) {
		
		System.out.println("in controller");
		String acceptedResult= postService.acceptPost(organization_id, post_id);
		List<String> result = new ArrayList<String>();
		result.add(acceptedResult);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		

	}
	
	@PostMapping("/post/solve")
	public ResponseEntity<List<String>> toSolvePost(@RequestParam String organization_id,@RequestParam Integer post_id) {
		
		System.out.println("in controller");
		String acceptedResult= postService.solvePost(organization_id, post_id);
		List<String> result = new ArrayList<String>();
		result.add(acceptedResult);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		

	}
	
	@GetMapping("/post/get")
	public ResponseEntity<PostDto> getPost(@RequestParam String user_id,@RequestParam Integer post_id) {
		
		System.out.println("in controller");
		PostDto postDto= postService.getPost(user_id,post_id);
		
	 return new ResponseEntity<>(postDto, HttpStatus.OK);
		

	}
	
	
	
	@PostMapping("/post/save")
	public ResponseEntity<List<String>> savePost(@RequestParam String user_id,@RequestParam Integer post_id) {
		System.out.println("in controller");
	
		String savedResult= postService.savePost(user_id, post_id);
		List<String> result = new ArrayList<String>();
		result.add(savedResult);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		

	}
	
	
	
	
	@PostMapping("/post/getByMyLocality/mostRecent")
	public ResponseEntity<List<PostDto>> getPostbyMyLocalityMostRecent  (@RequestBody LocationDto locationDto, 
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
	       
	        List<PostDto> postDtos= postService.getPostbyMyLocalityMostRecent(user_id,locationDto,page,size);
	        
	        return ResponseEntity.ok(postDtos);
		}
	
	
	@PostMapping("/post/getByLocationAndSector/mostPopular")
	public ResponseEntity<List<PostDto>> getPostbyLocationAndSectorMostPopular  (@RequestBody Map<String, Object> requestBody, 
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
	        List<String> sectors = (List<String>) requestBody.get("Sector");
	        List<String> talukas = (List<String>) requestBody.get("Taluka");
	        List<String> areas =(List<String>) requestBody.get("Area");
	        List<PostDto> postDtos= postService.getPostbyLocationAndSectorMostPopular(user_id,sectors,talukas,areas,page,size);
	        
	        return ResponseEntity.ok(postDtos);
		}
	
	@PostMapping("/post/getByLocationAndSector/mostRecent")
	public ResponseEntity<List<PostDto>> getPostbyLocationAndSectorMostRecent  (@RequestBody Map<String, Object> requestBody,
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		 
	        List<String> sectors = (List<String>) requestBody.get("Sector");
	        List<String> talukas = (List<String>) requestBody.get("Taluka");
	        List<String> areas =(List<String>) requestBody.get("Area");
	        List<PostDto> postDtos= postService.getPostbyLocationAndSectorMostRecent(user_id,sectors,talukas,areas,page,size);
	        
	        return ResponseEntity.ok(postDtos);
		}
	
	@PostMapping("/post/getBySector/mostRecent")
	public ResponseEntity<List<PostDto>> getPostbySectorMostRecent  (@RequestBody Map<String, Object> requestBody, 
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		
	        List<String> sectors = (List<String>) requestBody.get("Sector");
	   
	        List<PostDto> postDtos= postService.getPostbySectorMostRecent(user_id,sectors,page,size);
	        
	        return ResponseEntity.ok(postDtos);
		}
	
	@PostMapping("/post/getBySector/mostPopular")
	public ResponseEntity<List<PostDto>> getPostbySectorMostPopular (@RequestBody Map<String, Object> requestBody, 
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		
	        List<String> sectors = (List<String>) requestBody.get("Sector");
	   
	        List<PostDto> postDtos= postService.getPostbySectorMostPopular(user_id,sectors,page,size);
	        
	        return ResponseEntity.ok(postDtos);
		}
	

	@PostMapping("/post/getByLocation/mostRecent")
	public ResponseEntity<List<PostDto>> getPostbyLocationMostRecent  (@RequestBody Map<String, Object> requestBody, 
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		 
	    
	        List<String> talukas = (List<String>) requestBody.get("Taluka");
	        List<String> areas =(List<String>) requestBody.get("Area");
	        List<PostDto> postDtos= postService.getPostbyLocationMostRecent(user_id,talukas,areas,page,size);
	        
	        return ResponseEntity.ok(postDtos);
		}
	
	@PostMapping("/post/getByLocation/mostPopular")
	public ResponseEntity<List<PostDto>> getPostbyLocationMostPopular  (@RequestBody Map<String, Object> requestBody,
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		 
	    
	        List<String> talukas = (List<String>) requestBody.get("Taluka");
	        List<String> areas =(List<String>) requestBody.get("Area");
	        List<PostDto> postDtos= postService.getPostbyLocationMostPopular(user_id,talukas,areas,page,size);
	        
	        return ResponseEntity.ok(postDtos);
		}
	
	
	
	@PostMapping("/post/getByDefault/mostPopular")
	public ResponseEntity<List<PostDto>> getPostbyDefaultMostPopular  (
			@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		 
	    
	       
	        List<PostDto> postDtos= postService.getPostbyDefaultMostPopular(user_id,page,size);
	        
	        return ResponseEntity.ok(postDtos);
		}
	
	
	
	
	//Notification related 
	
	@GetMapping("/post/latest-post-taluka")
	public ResponseEntity<TalukaDto> getLatestPostTaluka(@RequestParam("userEmail") String userEmail) {
	    // retrieve the latest post for the specified user from the database
	    TalukaDto currenttalukaDto = postService.getLatestPostTaluka(userEmail);
	    System.out.println("Getting talukaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+currenttalukaDto.getTalukaId()+currenttalukaDto.getTalukaPolygon());
	    return ResponseEntity.ok(currenttalukaDto);
	}
	
	
//	@PostMapping("/post/write")
//	public void saveContact(HttpServletRequest request) throws IOException{
//		System.out.println("in controller");
//		//System.out.println(contact.firstName+ " "+ contact.lastName) ;
////		System.out.println(postDto.getLocationDto().getLatitude());
////		System.out.println(postDto.getSectorDto().getName());
////		
////		postService.savePost(postDto);
////		return ResponseEntity.ok(postDto);
//		 String payload = new BufferedReader(new InputStreamReader(request.getInputStream()))
//	                .lines().collect(Collectors.joining("\n"));
//		 
//		 System.out.println(payload);
//		
//
//	}
	
//	
//	
//	@GetMapping("/fetchContactList")
//	public ResponseEntity<List<User>> getAllContacts() {
//
//		return ResponseEntity.ok(userRepository.findAll());
//
//	}
	
	
	@GetMapping("/post/notifications")
    public ResponseEntity<Set<NotificationDto>> getNotificationsForLatestPostByUser(@RequestParam("userEmail") String userEmail) {
		
		Set<NotificationDto> notifications = postService.getNotificationsForLatestPostByUser(userEmail);
        return ResponseEntity.ok(notifications);
    }
	
	@GetMapping("/winner/organizations")
	public ResponseEntity<List<WinnerDto>> getTop10OrganizationsWithSolvedPosts() {
	    List<WinnerDto> winnerOrgs = postService.getTop10OrganizationsWithSolvedPosts();
	    if (winnerOrgs.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	    return new ResponseEntity<>(winnerOrgs, HttpStatus.OK);
	}
	
	@GetMapping("/winner/users")
    public ResponseEntity<List<WinnerDto>> getTopTenWinnerUsers() {
        List<WinnerDto> winnerUsers = postService.getTopTenWinnerUsers();
        if (winnerUsers.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	    return new ResponseEntity<>(winnerUsers, HttpStatus.OK);
    }
	
	
	
	
}
