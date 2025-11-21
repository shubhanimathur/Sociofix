package com.socioFix.Dto;

public class FollowingUserDto {


	private String userId;
	
	private String name;


	private String profileImg;
	
	private Integer followingStatus;
	
	private String userOrOrganization;

	public String getUserOrOrganization() {
		return userOrOrganization;
	}

	public void setUserOrOrganization(String userOrOrganization) {
		this.userOrOrganization = userOrOrganization;
	}

	public String getUserId() {
		return userId;
	}

	public FollowingUserDto(String userId, String name, String profileImg, Integer followingStatus,String userOrOrganization) {
		super();
		this.userId = userId;
		this.name = name;
		this.profileImg = profileImg;
		this.followingStatus = followingStatus;
		this.userOrOrganization=userOrOrganization;
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

	public String getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

	public Integer getFollowingStatus() {
		return followingStatus;
	}

	public void setFollowingStatus(Integer followingStatus) {
		this.followingStatus = followingStatus;
	}
	

}
