package com.socioFix.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

import com.socioFix.model.Notification.Notification;



/**
 * The persistent class for the drive database table.
 * 
 */
@Entity
public class Drive {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="drive_id")
	private Integer driveId;

	@Column(name="created_at")
	private LocalDateTime createdAt;
	
	@Column(name="when_at")
	private LocalDateTime whenAt;
	

	public LocalDateTime getWhenAt() {
		return whenAt;
	}

	public void setWhenAt(LocalDateTime whenAt) {
		this.whenAt = whenAt;
	}
	@Temporal(TemporalType.DATE)
	private Date date;

	private String description;

	private Time duration;

	@Column(name="image_path")
	private String imagePath;

	private Time time;

	private Integer upvotes;
	

	
	private Integer completed;
	
	 
	private Integer volunteers;
	 
	 public Integer getVolunteers() {
	        return this.volunteers;
	    }
	    public void setVolunteers(Integer volunteers) {
	        this.volunteers = volunteers;
	    }




	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="sector_id",foreignKey = @ForeignKey(name = "fk_sector_id"))
	private Sector sector;
	
	//bi-directional many-to-one association to Location
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="location_id",foreignKey = @ForeignKey(name = "fk_location_id"))
	private Location location;

	//bi-directional many-to-one association to Organization
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="organization_id",foreignKey = @ForeignKey(name = "fk_organization_id"))
	private Organization organization;

	//bi-directional many-to-one association to OpinionsDrive
	@OneToMany(mappedBy="drive")
	private Set<OpinionsDrive> opinionsDrives;
//
	//bi-directional many-to-many association to User

	@ManyToMany(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinTable(
		name="saved_drive"
		, joinColumns={
			@JoinColumn(name="drive_id",foreignKey = @ForeignKey(name = "fk_drive_id"))
			}
		, inverseJoinColumns={
			@JoinColumn(name="user_id",foreignKey = @ForeignKey(name = "fk_user_id"))
			}
		)
	private Set<User> savedDrivesUsers;

	//bi-directional many-to-many association to User
	
	@ManyToMany(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinTable(
		name="upvotes_drive"
		, joinColumns={
			@JoinColumn(name="drive_id",foreignKey = @ForeignKey(name = "fk_drive_id"))
			}
		, inverseJoinColumns={
			@JoinColumn(name="user_id",foreignKey = @ForeignKey(name = "fk_user_id"))
			}
		)
	private Set<User> upvotedDrivesUsers;
//
	//bi-directional many-to-many association to User

	@ManyToMany(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinTable(
		name="volunteer_drive"
		, joinColumns={
			@JoinColumn(name="drive_id",foreignKey = @ForeignKey(name = "fk_drive_id"))
			}
		, inverseJoinColumns={
			@JoinColumn(name="user_id",foreignKey = @ForeignKey(name = "fk_user_id"))
			}
		)
	private Set<User> volunteeredDrivesUsers;
//
	//bi-directional many-to-one association to ReminderNotification
	@OneToMany(mappedBy="drive")
	private Set<Notification> notifications;
//
//	//bi-directional many-to-one association to UpvotedNotification
//	@OneToMany(mappedBy="drive")
//	private Set<UpvotedNotification> upvotedNotifications;

	public Drive() {
	}

	public Integer getDriveId() {
		return this.driveId;
	}

	public void setDriveId(Integer driveId) {
		this.driveId = driveId;
	}

	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}


	public Integer getCompleted() {
		return completed;
	}

	public void setCompleted(Integer completed) {
		this.completed = completed;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public Sector getSector() {
		return this.sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}
//

	public Time getDuration() {
		return this.duration;
	}

	public void setDuration(Time duration) {
		this.duration = duration;
	}

	public String getImagePath() {
		return this.imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Time getTime() {
		return this.time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Integer getUpvotes() {
		return this.upvotes;
	}

	public void setUpvotes(Integer upvotes) {
		this.upvotes = upvotes;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Set<OpinionsDrive> getOpinionsDrives() {
		return this.opinionsDrives;
	}

	public void setOpinionsDrives(Set<OpinionsDrive> opinionsDrives) {
		this.opinionsDrives = opinionsDrives;
	}

	public OpinionsDrive addOpinionsDrive(OpinionsDrive opinionsDrive) {
		getOpinionsDrives().add(opinionsDrive);
		opinionsDrive.setDrive(this);

		return opinionsDrive;
	}

	public OpinionsDrive removeOpinionsDrive(OpinionsDrive opinionsDrive) {
		getOpinionsDrives().remove(opinionsDrive);
		opinionsDrive.setDrive(null);

		return opinionsDrive;
	}
//
	public Set<User> getSavedDrivesUsers() {
		return this.savedDrivesUsers;
	}

	public void setSavedDrivesUsers(Set<User> users1) {
		this.savedDrivesUsers = users1;
	}

	public Set<User> getUpvotedDrivesUsers() {
		return this.upvotedDrivesUsers;
	}

	public void setUpvotedDrivesUsers(Set<User> users2) {
		this.upvotedDrivesUsers = users2;
	}

	public Set<User> getVolunteeredDrivesUsers() {
		return this.volunteeredDrivesUsers;
	}

	public void setVolunteeredDrivesUsers(Set<User> users3) {
		this.volunteeredDrivesUsers = users3;
	}
//
	public Set<Notification> getNotifications() {
		return this.notifications;
	}
	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}
	public Notification addNotification(Notification notification) {
		getNotifications().add(notification);
		notification.setDrive(this);
		return notification;
	}
	public Notification removeNotification(Notification notification) {
		getNotifications().remove(notification);
		notification.setDrive(null);
		return notification;
	}
//
	@Override
    public int hashCode() {
     
        return this.driveId;
    }


}