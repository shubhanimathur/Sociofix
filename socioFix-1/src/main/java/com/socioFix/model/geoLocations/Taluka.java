package com.socioFix.model.geoLocations;


import java.util.List;
import java.util.Set;

import org.postgresql.geometric.PGpolygon;

import com.socioFix.model.Organization;
import com.socioFix.model.Post;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Taluka {


//	@Column(name="taluka_polygon")
//	private PGpolygon talukaPolygon;
	
	@Id
	@Column(name="taluka_id")
	private String talukaId;
	
	
	private String talukaPolygon;
//	@OneToMany(mappedBy="taluka")
//	private List<Area> areas;
	
	


//	public List<Area> getAreas() {
//		return areas;
//	}
//
//	public void setAreas(List<Area> areas) {
//		this.areas = areas;
//	}


	@ManyToMany(mappedBy="servingTalukas",cascade={ CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH,CascadeType.ALL})
	private Set<Organization> allOrganizationsServingInTalukas;//organizations2;
	
	
	public Set<Organization> getAllOrganizationsServingInTalukas() {
		return allOrganizationsServingInTalukas;
	}

	public void setAllOrganizationsServingInTalukas(Set<Organization> allOrganizationsServingInTalukas) {
		this.allOrganizationsServingInTalukas = allOrganizationsServingInTalukas;
	}


//	public PGpolygon getTalukaPolygon() {
//		return talukaPolygon;
//	}
//
//
//	public void setTalukaPolygon(PGpolygon talukaPolygon) {
//		this.talukaPolygon = talukaPolygon;
//	}


	public String getTalukaId() {
		return talukaId;
	}


	public void setTalukaId(String talukaNameId) {
		this.talukaId = talukaNameId;
	}

	public String getTalukaPolygon() {
		return talukaPolygon;
	}


	public void setTalukaPolygon(String talukaPolygon) {
		this.talukaPolygon = talukaPolygon;
	}


	
}
