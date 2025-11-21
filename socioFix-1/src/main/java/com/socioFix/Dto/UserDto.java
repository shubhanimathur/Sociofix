package com.socioFix.Dto;

import java.sql.Timestamp;

import jakarta.persistence.Column;

public class UserDto {

	private String userId;
	
	private String name;
	
	private String bio;

	private String contactNo;

	private String profileImg;
	
	byte[] byteImage;
	    
	private String stringOfImage;
	   public String getStringOfImage() {
	       return stringOfImage;
	   }

	   public void setStringOfImage(String stringOfImage) {
	       this.stringOfImage = stringOfImage;
	   }
		
	
	private Integer followingOrgNo;
	
	private Integer postsNo;
	
	private Integer volunteerNo;
	
	private String userOrOrganization;
	
	 public byte[] getByteImage() {
	        return byteImage;
	    }

	    public void setByteImage(byte[] byteImage) {
	        this.byteImage = byteImage;
	    }

	    
	
	public String getUserOrOrganization() {
		return userOrOrganization;
	}


	public void setUserOrOrganization(String userOrOrganization) {
		this.userOrOrganization = userOrOrganization;
	}


	public Integer getVolunteerNo() {
		return volunteerNo;
	}


	public void setVolunteerNo(Integer volunteerNo) {
		this.volunteerNo = volunteerNo;
	}


	public Integer getFollowingOrgNo() {
		return followingOrgNo;
	}


	public void setFollowingOrgNo(Integer followingOrgNo) {
		this.followingOrgNo = followingOrgNo;
	}


	public Integer getPostsNo() {
		return postsNo;
	}


	public void setPostsNo(Integer postsNo) {
		this.postsNo = postsNo;
	}


	UserDto(){
		
	}


	public UserDto(String userId, String name,  String contactNo) {
	
		this.userId = userId;
		this.name = name;
		this.contactNo = contactNo;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getBio() {
		return bio;
	}


	public void setBio(String bio) {
		this.bio = bio;
	}


	public String getContactNo() {
		return contactNo;
	}


	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}


	public String getProfileImg() {
		return profileImg;
	}


	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

}
