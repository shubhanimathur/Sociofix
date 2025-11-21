package com.socioFix.Dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socioFix.Service.LocalDateTimeDeserializer;
import com.socioFix.Service.LocalDateTimeSerializer;

public class NotificationDisplayDto {

	private Integer notificationId;

		private String toUserId;
	
	private String byUserId;
	
		private Integer postId;
	
		public NotificationDisplayDto() {};
		
		public NotificationDisplayDto(Integer notificationId, String toUserId, String description,
				LocalDateTime createdAt, String notificationType) {
			super();
			this.notificationId = notificationId;
			this.toUserId = toUserId;
			this.description = description;
			this.createdAt = createdAt;
			this.notificationType = notificationType;
		}



		private Integer driveId;
	
	public NotificationDisplayDto(Integer notificationId, String toUserId, String byUserId, Integer postId,
				Integer driveId, String description, LocalDateTime createdAt, String notificationType) {
			super();
			this.notificationId = notificationId;
			this.toUserId = toUserId;
			this.byUserId = byUserId;
			this.postId = postId;
			this.driveId = driveId;
			this.description = description;
			this.createdAt = createdAt;
			this.notificationType = notificationType;
		}



	private String description;
	
	
	
	
	
	
	
	@JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime createdAt;
	
	
	

	private String notificationType;
	
	
	
	public Integer getNotificationId() {
		return notificationId;
	}



	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}



	public LocalDateTime getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}



	public String getToUserId() {
		return toUserId;
	}



	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}



	public String getByUserId() {
		return byUserId;
	}



	public void setByUserId(String byUserId) {
		this.byUserId = byUserId;
	}



	public Integer getPostId() {
		return postId;
	}



	public void setPostId(Integer postId) {
		this.postId = postId;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public Integer getDriveId() {
		return driveId;
	}



	public void setDriveId(Integer driveid) {
		this.driveId = driveid;
	}



	public String getNotificationType() {
		return notificationType;
	}



	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}




	
}
