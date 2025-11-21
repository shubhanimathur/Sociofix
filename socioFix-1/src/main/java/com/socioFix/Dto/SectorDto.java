package com.socioFix.Dto;



public class SectorDto {
	public SectorDto() {
		
	}
	
	private String sectorId;
	
	
    public  String getSectorId() {
		return sectorId;
	}

	public void setSectorId( String  sectorId) {
		this.sectorId = sectorId;
	}

	public SectorDto(String sectorId) {
        this.sectorId =  sectorId;
    }

  


}
