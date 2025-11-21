package com.socioFix.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.socioFix.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization,String> {

	@Query("SELECT DISTINCT o FROM Organization o " +
	        "JOIN o.sectors s " +
	        "LEFT JOIN o.servingTalukas t " +
	        "LEFT JOIN o.servingAreas a " +
	        "WHERE s.id IN :sectorIds AND " +
	        "(t.id IN :talukaIds OR a.id IN :areaIds)")
	Page<Organization> findDistinctBySectorsAndTalukasAndAreas(
	        @Param("sectorIds") List<String> sectorIds,
	        @Param("talukaIds") List<String> talukaIds,
	        @Param("areaIds") List<String> areaIds,Pageable pageable);
	
	 @Query("SELECT DISTINCT o FROM Organization o " +
			  "LEFT JOIN o.servingTalukas t " +
		        "LEFT JOIN o.servingAreas a " +
	            "WHERE " +
	            "t.id IN :talukaIds " +
	            "OR a.id IN :areaIds")
	 Page<Organization> findDistinctByLocation(@Param("talukaIds") List<String> talukaIds,
			 @Param("areaIds")  List<String> areaIds,Pageable pageable);
	 
	 
	 @Query("SELECT DISTINCT o FROM Organization o " +
	            "JOIN o.sectors s " +
	            "WHERE s.id IN :sectorIds " 
	            )
	 Page<Organization> findDistinctBySectors(@Param("sectorIds") List<String> sectorIds,Pageable pageable);
	 
	 
	 Page<Organization> findByUserIdOrNameContainingIgnoreCase(String userId, String name,Pageable pageable);
	 
	  Page<Organization> findAll(Pageable pageable);
	 
}
