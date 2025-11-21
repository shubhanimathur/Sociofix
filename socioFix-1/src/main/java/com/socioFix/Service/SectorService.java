package com.socioFix.Service;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socioFix.Dto.SectorDto;
import com.socioFix.Dto.UserDto;
import com.socioFix.model.User;
import com.socioFix.repository.SectorRepository;
import com.socioFix.repository.UserRepository;

@Service
public class SectorService {

	
	
	
	@Autowired
	SectorRepository sectorRepository;
	

	public static ModelMapper modelMapper = new ModelMapper();
	
	public ArrayList<String> getSectorNames() {
		
		ArrayList<String> sectorNames = (ArrayList<String>) sectorRepository.findAllNames();
	
		System.out.println(sectorNames);
		
		return  sectorNames ;
		
	}
}
