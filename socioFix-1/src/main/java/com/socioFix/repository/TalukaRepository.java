package com.socioFix.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.socioFix.model.geoLocations.Taluka;


public interface TalukaRepository extends JpaRepository<Taluka, String> {
	
	   
	   @Query("SELECT t.talukaId FROM Taluka t")
	   List<String> findAllTalukaIds();

}
