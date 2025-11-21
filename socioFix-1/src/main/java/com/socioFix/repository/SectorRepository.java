package com.socioFix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.socioFix.model.Organization;
import com.socioFix.model.Sector;

public interface SectorRepository extends JpaRepository<Sector,String> {
	

	
	   @Query("SELECT s.sectorId FROM Sector s")
	   List<String> findAllNames();

}
