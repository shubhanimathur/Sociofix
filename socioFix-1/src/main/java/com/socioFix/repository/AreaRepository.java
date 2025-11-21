package com.socioFix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socioFix.model.geoLocations.Area;

public interface AreaRepository extends JpaRepository<Area, String> {

}
