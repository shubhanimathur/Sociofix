package com.example.project31.Dto;

import java.time.LocalDateTime;

public class NotificationDisplayDto {

    private Integer notificationId;


    private LocalDateTime createdAt;

    private String toUserId;

    private String byUserId;

    private Integer postId;



    private String description;




    private Integer driveId;


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



    public void setDriveId(Integer driveId) {
        this.driveId = driveId;
    }



    public String getNotificationType() {
        return notificationType;
    }



    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public NotificationDisplayDto() {};



}
