package com.socioFix.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.socioFix.Service.TalukaService;



@RestController
public class TalukaController {

	
	@Autowired
	TalukaService talukaService;

	
	@GetMapping("/taluka/getNames")
	public ResponseEntity<List<String>> getStrings() {
	List<String> talukaNames = (List)talukaService.getTalukaNames();
	return new ResponseEntity<>(talukaNames, HttpStatus.OK);
	}


	
}
