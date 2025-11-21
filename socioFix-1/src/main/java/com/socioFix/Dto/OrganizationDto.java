package com.socioFix.Dto;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.socioFix.model.Location;
import com.socioFix.model.OpinionsPost;
import com.socioFix.model.Sector;
import com.socioFix.model.geoLocations.Area;
import com.socioFix.model.geoLocations.Taluka;

import jakarta.persistence.Column;

public class OrganizationDto extends UserDto {

	private Boolean isVerified;
	
	private Integer uniqueId;
	
	private LocationDto base_location;
	
	private Integer driveNo;
	
	private Integer followingUserNo;
	
	private Integer solvedPostsNo;
	
	private Integer acceptedPostsNo;
	

	public Integer getAcceptedPostsNo() {
		return acceptedPostsNo;
	}

	public void setAcceptedPostsNo(Integer acceptedPostsNo) {
		this.acceptedPostsNo = acceptedPostsNo;
	}

	public Integer getSolvedPostsNo() {
		return solvedPostsNo;
	}

	public void setSolvedPostsNo(Integer solvedPostsNo) {
		this.solvedPostsNo = solvedPostsNo;
	}

	public Integer getDriveNo() {
		return driveNo;
	}

	public void setDriveNo(Integer driveNo) {
		this.driveNo = driveNo;
	}

	public Integer getFollowingUserNo() {
		return followingUserNo;
	}

	public void setFollowingUserNo(Integer followingUserNo) {
		this.followingUserNo = followingUserNo;
	}

	private Set<TalukaDto> servingTalukas;
	 private Set<AreaDto> servingAreas; 
	 

	 private Set<SectorDto> sectors;
	
	
	 public String getOrganizationId() {
	        return getUserId();
	    }

	    public void setOrganizationId(String organizationId) {
	        setUserId(organizationId);
	    }
	
	

	

	public Set<TalukaDto> getServingTalukas() {
		return servingTalukas;
	}

	public void setServingTalukas(Set<TalukaDto> servingTalukas) {
		this.servingTalukas = servingTalukas;
	}

	public Set<AreaDto> getServingAreas() {
		return servingAreas;
	}

	public void setServingAreas(Set<AreaDto> servingAreas) {
		this.servingAreas = servingAreas;
	}

	public Set<SectorDto> getSectors() {
		return sectors;
	}

	public void setSectors(Set<SectorDto> sectors) {
		this.sectors = sectors;
	}

	public LocationDto getBase_location() {
		return base_location;
	}

	public void setBase_location(LocationDto base_location) {
		this.base_location = base_location;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Integer getUniqueId() {
		return uniqueId;
	}

	

	public OrganizationDto(String userId, String name, String contactNo,Boolean isVerified, Integer uniqueId) {
		super(userId, name, contactNo);
		// TODO Auto-generated constructor stub
		this.isVerified = isVerified;
		this.uniqueId = uniqueId;
	}

	public void setUniqueId(Integer uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	OrganizationDto()
	{
		
	}
	
	 
}
