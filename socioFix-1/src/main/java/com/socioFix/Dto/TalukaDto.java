package com.socioFix.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class TalukaDto {

	
	private String  talukaId;
	private String talukaPolygon;
	

	public String getTalukaId() {
		return talukaId;
	}

	public void setTalukaId(String talukaId) {
		this.talukaId = talukaId;
	}
	
	public TalukaDto(){
		
	}
	
	public String getTalukaPolygon() {
		return talukaPolygon;
	}


	public void setTalukaPolygon(String talukaPolygon) {
		this.talukaPolygon = talukaPolygon;
	}
	
	
	
	
	
	
}
