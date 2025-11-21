package com.socioFix.Dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socioFix.Service.LocalDateTimeDeserializer;
import com.socioFix.Service.LocalDateTimeSerializer;
import com.socioFix.model.Post;
import com.socioFix.model.User;

public class NotificationDto implements Serializable{


    private Integer notificationId;

    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    private UserDto toUser;

    private UserDto byUser;

    private PostDto post;

    private String description;

    private DriveDto drive;

    private String notificationType;
    
    public NotificationDto(Integer notificationId, LocalDateTime createdAt, UserDto toUser, UserDto byUser, PostDto post, String description, DriveDto drive, String notificationType) {
        this.notificationId = notificationId;
        this.createdAt = createdAt;
        this.toUser = toUser;
        this.byUser = byUser;
        this.post = post;
        this.description = description;
        this.drive = drive;
        this.notificationType = notificationType;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }


    public DriveDto getDrive() {
        return this.drive;
    }

    public void setDrive(DriveDto drive) {
        this.drive = drive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserDto getByUser() {
        return byUser;
    }



    public void setByUser(UserDto byUser) {
        this.byUser = byUser;
    }



    public Integer getNotificationId() {
        return this.notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    //	public AcceptedNotification getAcceptedNotification() {
//		return this.acceptedNotification;
//	}
//
//	public void setAcceptedNotification(AcceptedNotification acceptedNotification) {
//		this.acceptedNotification = acceptedNotification;
//	}
//
//	public ContributeNotification getContributeNotification() {
//		return this.contributeNotification;
//	}
//
//	public void setContributeNotification(ContributeNotification contributeNotification) {
//		this.contributeNotification = contributeNotification;
//	}
//
    public UserDto getToUser() {
        return this.toUser;
    }

    public void setToUser(UserDto user) {
        this.toUser = user;
    }

    //	public ReminderNotification getReminderNotification() {
//		return this.reminderNotification;
//	}
//
//	public void setReminderNotification(ReminderNotification reminderNotification) {
//		this.reminderNotification = reminderNotification;
//	}
//
//	public StatusNotification getStatusNotification() {
//		return this.statusNotification;
//	}
//
//	public void setStatusNotification(StatusNotification statusNotification) {
//		this.statusNotification = statusNotification;
//	}
//
//	public ToAcceptNotification getToAcceptNotification() {
//		return this.toAcceptNotification;
//	}
//
//	public void setToAcceptNotification(ToAcceptNotification toAcceptNotification) {
//		this.toAcceptNotification = toAcceptNotification;
//	}
//
//	public UpvotedNotification getUpvotedNotification() {
//		return this.upvotedNotification;
//	}
//
//	public void setUpvotedNotification(UpvotedNotification upvotedNotification) {
//		this.upvotedNotification = upvotedNotification;
//	}
    public PostDto getPost() {
        return this.post;
    }

    public void setPost(PostDto post) {
        this.post = post;
    }

    public NotificationDto() {
    }


    public NotificationDto(UserDto toUser, PostDto post) {
        //this.createdAt = createdAt;
        this.toUser = toUser;
        this.post = post;
    }



}