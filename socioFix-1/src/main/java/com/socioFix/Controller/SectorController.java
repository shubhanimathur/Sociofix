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
import org.springframework.web.bind.annotation.RestController;

import com.socioFix.Service.SectorService;


@RestController
public class SectorController {

	
	@Autowired
	SectorService sectorService;
	
	
		
	    @GetMapping("/sector/getNames")
	    public ResponseEntity<List<String>> getStrings() {
	       
	    	System.out.println("in controller");
	    	List<String> sectorNames = (List)sectorService.getSectorNames();
	    	return new ResponseEntity<>(sectorNames, HttpStatus.OK);
	    }
	
	
}
