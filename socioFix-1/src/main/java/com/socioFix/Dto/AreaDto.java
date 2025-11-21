package com.socioFix.Dto;

import jakarta.persistence.Column;

public class AreaDto {

	private String areaId;
	


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



	private String talukaName;
	
	public AreaDto(){
		
	}
}
