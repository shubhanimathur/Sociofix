package com.socioFix.model.Notification;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

import com.socioFix.model.Drive;
import com.socioFix.model.Post;
import com.socioFix.model.User;


/**
 * The persistent class for the notification database table.
 * 
 */
@Entity
public class Notification  {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="notification_id")
	private Integer notificationId;

	@Column(name="created_at")
	private LocalDateTime createdAt;



	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="user_id")
	private User toUser;
	
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="by_user_id",foreignKey=@ForeignKey(name = "fk_by_user_id"))
	private User byUser;

	

	@ManyToOne(fetch=FetchType.LAZY,cascade= { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH,CascadeType.ALL})
	@JoinColumn(name="post_id",foreignKey=@ForeignKey(name = "fk_post_id"))
	private Post post;
	
	
	
	private String description;
	
	
	
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="drive_id",foreignKey=@ForeignKey(name = "fk_drive_id"))
	private Drive drive;
	
	
	@Column(name="notification_type")
	private String notificationType;



	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public Notification() {
	}

	public Drive getDrive() {
		return this.drive;
	}

	public void setDrive(Drive drive) {
		this.drive = drive;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getByUser() {
		return byUser;
	}



	public void setByUser(User byUser) {
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
	public User getToUser() {
		return this.toUser;
	}

	public void setToUser(User user) {
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
	public Post getPost() {
		return this.post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (!(o instanceof Notification)) return false;
	    Notification that = (Notification) o;
	    return Objects.equals(toUser.getUserId(), that.toUser.getUserId());
	}

	@Override
	public int hashCode() {
	    return Objects.hash(toUser.getUserId());
	}

}