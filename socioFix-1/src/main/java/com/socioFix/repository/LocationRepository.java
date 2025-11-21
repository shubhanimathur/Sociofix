package com.socioFix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socioFix.model.Location;


public interface LocationRepository extends JpaRepository<Location,Integer> {

}
