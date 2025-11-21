package com.socioFix.Controller;


import java.util.ArrayList;
import java.util.List;

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
import com.socioFix.Dto.NotificationDisplayDto;
import com.socioFix.Dto.NotificationDto;
import com.socioFix.Dto.OrganizationDto;
import com.socioFix.Dto.PostDto;
import com.socioFix.Dto.UserDto;
import com.socioFix.Service.Constant;
import com.socioFix.Service.PostService;
import com.socioFix.Service.UserService;
import com.socioFix.repository.OrganizationRepository;
import com.socioFix.repository.UserRepository;
import com.socioFix.model.User;

@RestController
public class UserController {

	
	@Autowired
	UserService userService;
	
	
	@Autowired
	UserRepository userRepository;

	public static ModelMapper modelMapper = new ModelMapper();
	
	
	@PostMapping("/user/save")
	public ResponseEntity<UserDto> userPost(@RequestBody UserDto userDto) {
		System.out.println("in controller save");

	
		
		userService.saveUser(userDto);
		System.out.println("after save");
		return ResponseEntity.ok(userDto);
		

	}
	
	@GetMapping("/user/get")
	public ResponseEntity<UserDto> getUser(@RequestParam String user_id) {
	System.out.println("in controller");
	
	UserDto userDto = userService.getUser(user_id);
	
	return ResponseEntity.ok(userDto);
	}
	
	@GetMapping("/user/getUserPosts")
	public ResponseEntity<List<PostDto>> getUserPosts(@RequestParam String user_id, @RequestParam("view_user_id") String view_user_id,@RequestParam Integer page, @RequestParam Integer size) {
	System.out.println("in controller");
	
	List<PostDto> postDtos= userService.getUserPosts(user_id,view_user_id,page,size);
	
	
	
	return ResponseEntity.ok(postDtos);
	}
	
	@GetMapping("/user/getUserUpvotedPosts")
	public ResponseEntity<List<PostDto>> getUserUpvotedPosts(@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
	System.out.println("in controller");
	
	List<PostDto> postDtos= userService.getUserUpvotedPosts(user_id,page,size);
	
	
	
	return ResponseEntity.ok(postDtos);
	}
	
	@GetMapping("/user/getUserSavedPosts")
	public ResponseEntity<List<PostDto>> getUserSavedPosts(@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
	System.out.println("in controller");
	
	List<PostDto> postDtos= userService.getUserSavedPosts(user_id,page,size);
	
	
	
	return ResponseEntity.ok(postDtos);
	}
	
	@GetMapping("/user/getUserVolunteeredDrives")
	public ResponseEntity<List<DriveDto>> getUserVolunteeredDrives(@RequestParam String user_id, @RequestParam("view_user_id") String view_user_id,@RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
		List<DriveDto> driveDtos= userService.getUserVolunteeredDrives(user_id,view_user_id,page,size);
		return ResponseEntity.ok(driveDtos);
	}


	
	@GetMapping("/user/getUserAcceptedPosts")
	public ResponseEntity<List<PostDto>> getUserAcceptedPosts(@RequestParam String user_id,@RequestParam("view_user_id") String view_user_id, @RequestParam Integer page, @RequestParam Integer size) {
	System.out.println("in controller");
	
	List<PostDto> postDtos= userService.getUserAcceptedPosts(user_id,view_user_id,page,size);
	
	
	
	return ResponseEntity.ok(postDtos);
	}
	
	@GetMapping("/user/getUserSolvedPosts")
	public ResponseEntity<List<PostDto>> getUserSolvedPosts(@RequestParam String user_id,@RequestParam("view_user_id") String view_user_id, @RequestParam Integer page, @RequestParam Integer size) {
	System.out.println("in controller");
	
	List<PostDto> postDtos= userService.getUserSolvedPosts(user_id,view_user_id,page,size);
	
	
	
	return ResponseEntity.ok(postDtos);
	}
	
	
	@GetMapping("/user/getUserDrives")
	public ResponseEntity<List<DriveDto>> getUserDrives(@RequestParam String user_id,@RequestParam("view_user_id") String view_user_id, @RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
		List<DriveDto> driveDtos= userService.getUserDrives(user_id,view_user_id,page,size);
		return ResponseEntity.ok(driveDtos);
	}
	@GetMapping("/user/getUserUpvotedDrives")
	public ResponseEntity<List<DriveDto>> getUserUpvotedDrives(@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
		List<DriveDto> driveDtos= userService.getUserUpvotedDrives(user_id,page,size);
		return ResponseEntity.ok(driveDtos);
	}
	@GetMapping("/user/getUserSavedDrives")
	public ResponseEntity<List<DriveDto>> getUserSavedDrives(@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
		List<DriveDto> driveDtos= userService.getUserSavedDrives(user_id,page,size);
		return ResponseEntity.ok(driveDtos);
	}

	
	@GetMapping("/user/getFollowingOrganizations")
	public ResponseEntity<List<FollowingUserDto>> getFollowingOrganizations(@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
		List<FollowingUserDto> followingUserDto= userService.getFollowingOrganizations(user_id,page,size);
		return ResponseEntity.ok(followingUserDto);
	}
	
	
	@GetMapping("/user/getDrivesForFollowedOrganizations")
	public ResponseEntity<List<DriveDto>> getDrivesForFollowedOrganizations(@RequestParam String user_id, @RequestParam("view_user_id") String view_user_id,@RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
		List<DriveDto> driveDtos= userService.getDrivesForFollowedOrganizations(user_id,view_user_id,page,size);
		return ResponseEntity.ok(driveDtos);
	}
	
	@PostMapping("/constant/censoring")
	public ResponseEntity<List<String>> censoringState(@RequestParam String censoring_state) {
		System.out.println("in controller");
	
		Constant.censoring=censoring_state;
		List<String> result = new ArrayList<String>();
		result.add(Constant.censoring);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		

	}
	
	@GetMapping("/user/getNotifications")
	public ResponseEntity<List<NotificationDisplayDto>> getNotifications(@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
		List<NotificationDisplayDto> notificationDisplayDtos= userService.getNotifications(user_id,page,size);
		return ResponseEntity.ok(notificationDisplayDtos);
	}
	
	@GetMapping("/user/getNotificationsUserHelp")
	public ResponseEntity<List<NotificationDisplayDto>> getNotificationsUserHelp(@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
		List<NotificationDisplayDto> notificationDisplayDtos= userService.getNotificationsUserHelp(user_id,page,size);
		return ResponseEntity.ok(notificationDisplayDtos);
	}
	@GetMapping("/user/getNotificationsUserActivity")
	public ResponseEntity<List<NotificationDisplayDto>> getNotificationsUserActivity(@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
		List<NotificationDisplayDto> notificationDisplayDtos= userService.getNotificationsUserActivity(user_id,page,size);
		return ResponseEntity.ok(notificationDisplayDtos);
	}
	@GetMapping("/user/getNotificationsOrganizationHelp")
	public ResponseEntity<List<NotificationDisplayDto>> getNotificationsOrganizationHelp(@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
		List<NotificationDisplayDto> notificationDisplayDtos= userService.getNotificationsOrganizationHelp(user_id,page,size);
		return ResponseEntity.ok(notificationDisplayDtos);
	}
	
	@GetMapping("/user/getNotificationsOrganizationActivity")
	public ResponseEntity<List<NotificationDisplayDto>> getNotificationsOrganizationActivity(@RequestParam String user_id, @RequestParam Integer page, @RequestParam Integer size) {
		System.out.println("in controller");
		List<NotificationDisplayDto> notificationDisplayDtos= userService.getNotificationsOrganizationActivity(user_id,page,size);
		return ResponseEntity.ok(notificationDisplayDtos);
	}
	
	
//	@PostMapping("/user/toExculdeFromFilter")
//	public ResponseEntity<String> toExculdeFromFilter(@RequestParam String text) {
//	String result = userService.toExculdeFromFilter();
//	return new ResponseEntity<>(result, HttpStatus.OK);
//	}

	@PostMapping("/user/edit")
	public ResponseEntity<UserDto> editUser(@RequestBody UserDto userDto) {
		System.out.println("in controller");
		userService.editUser(userDto);
		return ResponseEntity.ok(userDto);
		
	}


	
}
