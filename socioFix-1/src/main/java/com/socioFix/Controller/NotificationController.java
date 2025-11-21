package com.socioFix.Controller;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.socioFix.Dto.NotificationDto;
import com.socioFix.Service.NotificationService;

@RestController
public class NotificationController {
	
	@Autowired
	NotificationService notificationService;
	

	
	
		
	 @PostMapping("/notification/save")
	 public ResponseEntity<String> saveNotification(@RequestBody NotificationDto notificationDto) {
			System.out.println("In notiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii controller");
	 	return notificationService.saveNotification(notificationDto);
	 	
			
		}
	
	
}



