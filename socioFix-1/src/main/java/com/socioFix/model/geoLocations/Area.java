package com.socioFix.model.geoLocations;
import java.util.List;
import java.util.Set;

import org.postgresql.geometric.PGpolygon;

import com.socioFix.model.Organization;
import com.socioFix.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Area {

	
	@Id
	@Column(name="area_id")
	private String areaId;
	

	@Column(name="area_name")
	private String areaName;
	

	private Double latitude;

	private Double longitude;


	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longtitiude) {
		this.longitude = longtitiude;
	}

	@Column(name="taluka_name")
	private String talukaName;

//	@ManyToOne(fetch=FetchType.LAZY,cascade =  { CascadeType.PERSIST, CascadeType.MERGE,
//			CascadeType.DETACH, CascadeType.REFRESH,CascadeType.ALL})
//	@JoinColumn(name="taluka_id",foreignKey = @ForeignKey(name = "fk_taluka_id"))
//	private Taluka taluka;
//
//	

	
	@ManyToMany(mappedBy="servingAreas",cascade= { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH,CascadeType.ALL})
	private Set<Organization> allOrganizationsServingInAreas;//organizations2;


	public Set<Organization> getAllOrganizationsServingInAreas() {
		return allOrganizationsServingInAreas;
	}


	public void setAllOrganizationsServingInAreas(Set<Organization> allOrganizationsServingInAreas) {
		this.allOrganizationsServingInAreas = allOrganizationsServingInAreas;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}


	public String getAreaName() {
		return areaName;
	}


	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getTalukaName() {
		return talukaName;
	}

	public void setTalukaName(String talukaName) {
		this.talukaName = talukaName;
	}

//	public Taluka getTaluka() {
//		return taluka;
//	}
//
//	public void setTaluka(Taluka taluka) {
//		this.taluka = taluka;
//	}

}
