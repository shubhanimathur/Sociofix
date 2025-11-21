package com.socioFix.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.socioFix.model.Notification.Notification;


/**
 * The persistent class for the post database table.
 * 
 */
@Entity
public class Post implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="post_id")
	private Integer postId;

	@Column(name="created_at",columnDefinition = "timestamp")
	private LocalDateTime createdAt;

	private String description;

	@Column(name="image_path")
	private String imagePath;

	private String status;

	private Integer upvotes;

	@Column(name="video_path")
	private String videoPath;


	@Column(name="when_at")
	private LocalDateTime whenAt;
	

	public LocalDateTime getWhenAt() {
		return whenAt;
	}

	public void setWhenAt(LocalDateTime whenAt) {
		this.whenAt = whenAt;
	}
	

	//bi-directional many-to-one association to OpinionsPost
	@OneToMany(mappedBy="post")
	private List<OpinionsPost> opinionsPosts;

	//bi-directional many-to-one association to Location
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="location_id",foreignKey = @ForeignKey(name = "fk_location_id"))
	private Location location;

	//bi-directional many-to-one association to Organization
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="organization_id",foreignKey = @ForeignKey(name = "fk_organization_id"))
	private Organization organization;
//
//	//bi-directional many-to-one association to Sector
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="sector_id",foreignKey = @ForeignKey(name = "fk_sector_id"))
    private Sector sector;
//
	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="user_id",foreignKey = @ForeignKey(name = "fk_user_id"))
	private User user;

	//bi-directional many-to-many association to User
	@ManyToMany(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinTable(
		name="saved_post"
		, joinColumns={
			@JoinColumn(name="post_id",foreignKey = @ForeignKey(name = "fk_post_id"))
			}
		, inverseJoinColumns={
			@JoinColumn(name="user_id",foreignKey = @ForeignKey(name = "fk_user_id"))
			}
		)
	private Set<User> savedPostUsers ;

	//bi-directional many-to-many association to User
	
	
	@ManyToMany(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinTable(
		name="upvotes_post"
		, joinColumns={
			@JoinColumn(name="post_id",foreignKey = @ForeignKey(name = "fk_post_id"))
			}
		, inverseJoinColumns={
			@JoinColumn(name="user_id",foreignKey = @ForeignKey(name = "fk_user_id"))
			}
		)
	private Set<User> upvotedPostUsers;
	
	
	
	
	
	
	
	
	
	
//
//	//bi-directional many-to-one association to AcceptedNotification
//	@OneToMany(mappedBy="post")
//	private List<AcceptedNotification> acceptedNotifications;
//
//	//bi-directional many-to-one association to ContributeNotification
//	@OneToMany(mappedBy="post")
//	private List<ContributeNotification> contributeNotifications;
//
//	//bi-directional many-to-one association to StatusNotification
//	@OneToMany(mappedBy="post")
//	private List<StatusNotification> statusNotifications;
//
	//bi-directional many-to-one association to ToAcceptNotification
	@OneToMany(mappedBy="post")
	private Set<Notification> Notifications;
//
//	//bi-directional many-to-one association to UpvotedNotification
//	@OneToMany(mappedBy="post")
//	private List<UpvotedNotification> upvotedNotifications;

	public Post() {
	}

	public Integer getPostId() {
		return this.postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}
	

	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImagePath() {
		return this.imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getUpvotes() {
		return this.upvotes;
	}

	public void setUpvotes(Integer upvotes) {
		this.upvotes = upvotes;
	}

	public String getVideoPath() {
		return this.videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public List<OpinionsPost> getOpinionsPosts() {
		return this.opinionsPosts;
	}

	public void setOpinionsPosts(List<OpinionsPost> opinionsPosts) {
		this.opinionsPosts = opinionsPosts;
	}

	public OpinionsPost addOpinionsPost(OpinionsPost opinionsPost) {
		getOpinionsPosts().add(opinionsPost);
		opinionsPost.setPost(this);

		return opinionsPost;
	}

	public OpinionsPost removeOpinionsPost(OpinionsPost opinionsPost) {
		getOpinionsPosts().remove(opinionsPost);
		opinionsPost.setPost(null);

		return opinionsPost;
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

	public Sector getSector() {
		return this.sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}
//
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<User> getSavedPostUsers() {
		return this.savedPostUsers;
	}

	public void setSavedPostUsers(Set<User> savedPostUsers) {
		this.savedPostUsers = savedPostUsers;
	}

	public Set<User> getUpvotedPostUsers() {
		return this.upvotedPostUsers;
	}

	public void setUpvotedPostUsers(Set<User> upvotedPostUsers) {
		this.upvotedPostUsers = upvotedPostUsers;
	}
//
//	public List<AcceptedNotification> getAcceptedNotifications() {
//		return this.acceptedNotifications;
//	}
//
//	public void setAcceptedNotifications(List<AcceptedNotification> acceptedNotifications) {
//		this.acceptedNotifications = acceptedNotifications;
//	}
//
//	public AcceptedNotification addAcceptedNotification(AcceptedNotification acceptedNotification) {
//		getAcceptedNotifications().add(acceptedNotification);
//		acceptedNotification.setPost(this);
//
//		return acceptedNotification;
//	}
//
//	public AcceptedNotification removeAcceptedNotification(AcceptedNotification acceptedNotification) {
//		getAcceptedNotifications().remove(acceptedNotification);
//		acceptedNotification.setPost(null);
//
//		return acceptedNotification;
//	}
//
//	public List<ContributeNotification> getContributeNotifications() {
//		return this.contributeNotifications;
//	}
//
//	public void setContributeNotifications(List<ContributeNotification> contributeNotifications) {
//		this.contributeNotifications = contributeNotifications;
//	}
//
//	public ContributeNotification addContributeNotification(ContributeNotification contributeNotification) {
//		getContributeNotifications().add(contributeNotification);
//		contributeNotification.setPost(this);
//
//		return contributeNotification;
//	}
//
//	public ContributeNotification removeContributeNotification(ContributeNotification contributeNotification) {
//		getContributeNotifications().remove(contributeNotification);
//		contributeNotification.setPost(null);
//
//		return contributeNotification;
//	}
//
//	public List<StatusNotification> getStatusNotifications() {
//		return this.statusNotifications;
//	}
//
//	public void setStatusNotifications(List<StatusNotification> statusNotifications) {
//		this.statusNotifications = statusNotifications;
//	}
//
//	public StatusNotification addStatusNotification(StatusNotification statusNotification) {
//		getStatusNotifications().add(statusNotification);
//		statusNotification.setPost(this);
//
//		return statusNotification;
//	}
//
//	public StatusNotification removeStatusNotification(StatusNotification statusNotification) {
//		getStatusNotifications().remove(statusNotification);
//		statusNotification.setPost(null);
//
//		return statusNotification;
//	}
//

	//To Accept Notification
	
	public Set<Notification> getNotifications() {
		return this.Notifications;
	}

	public void setNotifications(Set<Notification> Notifications) {
		this.Notifications = Notifications;
	}

	public Notification addNotification(Notification Notification) {
		getNotifications().add(Notification);
		Notification.setPost(this);

		return Notification;
	}

	public Notification removeNotification(Notification Notification) {
		getNotifications().remove(Notification);
		Notification.setPost(null);

		return Notification;
	}
//
//	public List<UpvotedNotification> getUpvotedNotifications() {
//		return this.upvotedNotifications;
//	}
//
//	public void setUpvotedNotifications(List<UpvotedNotification> upvotedNotifications) {
//		this.upvotedNotifications = upvotedNotifications;
//	}
//
//	public UpvotedNotification addUpvotedNotification(UpvotedNotification upvotedNotification) {
//		getUpvotedNotifications().add(upvotedNotification);
//		upvotedNotification.setPost(this);
//
//		return upvotedNotification;
//	}
//
//	public UpvotedNotification removeUpvotedNotification(UpvotedNotification upvotedNotification) {
//		getUpvotedNotifications().remove(upvotedNotification);
//		upvotedNotification.setPost(null);
//
//		return upvotedNotification;
//	}
	
	
	@Override
    public int hashCode() {
     
        return this.postId;
    }

}