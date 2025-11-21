package com.socioFix.model;


import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.socioFix.model.Notification.Notification;



/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
public class User {

	@Id
	@Column(name="user_id",columnDefinition="TEXT")
	private String userId;

	private String bio;

	@Column(name="contact_no")
	private String contactNo;

	@Column(name="created_at")
	private Timestamp createdAt;

	private String name;


	
	@Column(name="user_or_organization")
	private String userOrOrganization;
				  

	public String getUserOrOrganization() {
		return userOrOrganization;
	}

	public void setUserOrOrganization(String userOrOrganization) {
		this.userOrOrganization = userOrOrganization;
	}

	@Column(name="profile_img")
	private String profileImg;

	//bi-directional many-to-one association to OpinionsPost
	@OneToMany(mappedBy="user")
	private Set<OpinionsPost> opinionsPosts;
	
	//
	//bi-directional many-to-one association to OpinionsDrive
	@OneToMany(mappedBy="user")
	private Set<OpinionsDrive> opinionsDrives;


//
//	//bi-directional many-to-one association to Post
	@OneToMany(mappedBy="user")
	private Set<Post> posts;
//
	//bi-directional many-to-many association to Drive
	@ManyToMany(mappedBy="savedDrivesUsers")
	private Set<Drive> savedDrives;

//	//bi-directional many-to-many association to Drive
	@ManyToMany(mappedBy="upvotedDrivesUsers")
	private Set<Drive> upvotedDrives;
//
	//bi-directional many-to-many association to Drive
	@ManyToMany(mappedBy="volunteeredDrivesUsers")
	private Set<Drive> volunteeredDrives;
////
//	//bi-directional many-to-many association to Organization
	@ManyToMany(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinTable(
		name="user_following_organization"
		, joinColumns={
			@JoinColumn(name="user_id",foreignKey = @ForeignKey(name = "fk_user_id"))
			}
		, inverseJoinColumns={
			@JoinColumn(name="organization_id",foreignKey = @ForeignKey(name = "fk_organization_id"))
			}
		)
	private Set<Organization> followingOrganizations;
//
	//bi-directional many-to-many association to Post

	@ManyToMany(mappedBy="savedPostUsers")
	private Set<Post> savedPosts;//posts2;

	//bi-directional many-to-many association to Post

	
	
	
	@ManyToMany(mappedBy="upvotedPostUsers")
 	private Set<Post> upvotedPosts;//posts3;
//
	//bi-directional many-to-one association to UserFollowingOrganization
	@OneToMany(mappedBy="user")
	private Set<UserFollowingOrganization> userFollowingOrganizations;
//

//
	//bi-directional many-to-one association to Notification
//	@OneToMany(mappedBy="toUser")
//	private Set<Notification> notifications;
	
	@OneToMany(mappedBy="toUser")
	private Set<Notification> notifications;
	

	
	@OneToMany(mappedBy="byUser")
	private Set<Notification> byNotifications;

	public User() {
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBio() {
		return this.bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getContactNo() {
		return this.contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	

	

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getProfileImg() {
		return this.profileImg;
	}

	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

	public Set<OpinionsPost> getOpinionsPosts() {
		return this.opinionsPosts;
	}

	public void setOpinionsPosts(Set<OpinionsPost> opinionsPosts) {
		this.opinionsPosts = opinionsPosts;
	}

	public OpinionsPost addOpinionsPost(OpinionsPost opinionsPost) {
		getOpinionsPosts().add(opinionsPost);
		opinionsPost.setUser(this);

		return opinionsPost;
	}

	public OpinionsPost removeOpinionsPost(OpinionsPost opinionsPost) {
		getOpinionsPosts().remove(opinionsPost);
		opinionsPost.setUser(null);

		return opinionsPost;
	}
	
	//opinions drives 
	public Set<OpinionsDrive> getOpinionsDrives() {
	return this.opinionsDrives;
}

public void setOpinionsDrives(Set<OpinionsDrive> opinionsDrives) {
	this.opinionsDrives = opinionsDrives;
}

public OpinionsDrive addOpinionsDrive(OpinionsDrive opinionsDrive) {
	getOpinionsDrives().add(opinionsDrive);
	opinionsDrive.setUser(this);

	return opinionsDrive;
}

public OpinionsDrive removeOpinionsDrive(OpinionsDrive opinionsDrive) {
	getOpinionsDrives().remove(opinionsDrive);
	opinionsDrive.setUser(null);

	return opinionsDrive;
}


	public Set<Post> getPosts() {
		return this.posts;
	}
//
	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public Post addPosts(Post posts) {
		getPosts().add(posts);
		posts.setUser(this);

		return posts;
	}

	public Post removePosts(Post posts) {
		getPosts().remove(posts);
		posts.setUser(null);

		return posts;
	}
//
	public Set<Drive> getSavedDrives() {
		return this.savedDrives;
	}

	public void setSavedDrives(Set<Drive> drives1) {
		this.savedDrives = drives1;
	}

	public Set<Drive> getUpvotedDrives() {
		return this.upvotedDrives;
	}

	public void setUpvotedDrives(Set<Drive> drives2) {
		this.upvotedDrives = drives2;
	}

	public Set<Drive> getVolunteeredDrives() {
		return this.volunteeredDrives;
	}

	public void setVolunteeredDrives(Set<Drive> drives3) {
		this.volunteeredDrives = drives3;
	}
//
	public Set<Organization> getFollowingOrganizations() {
		return this.followingOrganizations;
	}

	public void setOrganizations(Set<Organization> followingOrganizations) {
		this.followingOrganizations = followingOrganizations;
	}

	public Set<Post> getSavedPosts() {
		return this.savedPosts;
	}

	public void setSavedPosts(Set<Post> savedPosts) {
		this.savedPosts = savedPosts;
	}

	public Set<Post> getUpvotedPosts() {
		return this.upvotedPosts;
	}

	public void setUpvotedPosts(Set<Post> upvotedPosts) {
		this.upvotedPosts = upvotedPosts;
	}
//
	public Set<UserFollowingOrganization> getUserFollowingOrganizations() {
		return this.userFollowingOrganizations;
	}

	public void setUserFollowingOrganizations(Set<UserFollowingOrganization> userFollowingOrganizations) {
		this.userFollowingOrganizations = userFollowingOrganizations;
	}

	public UserFollowingOrganization addUserFollowingOrganization(UserFollowingOrganization userFollowingOrganization) {
		getUserFollowingOrganizations().add(userFollowingOrganization);
		userFollowingOrganization.setUser(this);

		return userFollowingOrganization;
	}

	public UserFollowingOrganization removeUserFollowingOrganization(UserFollowingOrganization userFollowingOrganization) {
		getUserFollowingOrganizations().remove(userFollowingOrganization);
		userFollowingOrganization.setUser(null);

		return userFollowingOrganization;
	}
//

//
//	public Set<Notification> getNotifications() {
//		return this.notifications;
//	}
//
//	public void setNotifications(Set<Notification> notifications) {
//		this.notifications = notifications;
//	}
//
//	public Notification addNotification(Notification notification) {
//		getNotifications().add(notification);
//		notification.setToUser(this);
//
//		return notification;
//	}
//
//	public Notification removeNotification(Notification notification) {
//		getNotifications().remove(notification);
//		notification.setToUser(null);
//
//		return notification;
//	}
	
	//
	public Set<Notification> getNotifications() {
		return this.notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}

	public Notification addNotification(Notification notification) {
		getNotifications().add(notification);
		notification.setToUser(this);

		return notification;
	}

	public Notification removeNotification(Notification notification) {
		getNotifications().remove(notification);
		notification.setToUser(null);

		return notification;
	}
	

	// Upvoted Notification

	public Set<Notification> getByNotifications() {
		return this.byNotifications;
	}

	public void setByNotifications(Set<Notification> byNotifications) {
		this.byNotifications = byNotifications;
	}

	
	
	
//	@Override
//    public boolean equals(Object obj)
//    {
//          
//    if(this == obj)
//            return true;
//          
//        // it checks if the argument is of the 
//        // type Geek by comparing the classes 
//        // of the passed argument and this object.
//        // if(!(obj instanceof Geek)) return false; ---> avoid.
//        if(obj == null || obj.getClass()!= this.getClass())
//            return false;
//          
//        // type casting of the argument. 
//       User user = (User) obj;
//          
//        // comparing the state of argument with 
//        // the state of 'this' Object.
//        return ( user.userId.hashCode()==this.userId.hashCode());
//    }
	
	 @Override
	    public boolean equals(Object obj) {
		 System.out.println("In equals ");
	        if (obj == this) {
	        	
	            return true;
	        }
	        if (!(obj instanceof User)) {
	            return false;
	        }
	        User other = (User) obj;
	        return this.userId.equals(other.userId);
	    }

	    @Override
	    public int hashCode() {
	        return userId.hashCode();
	    }
	
	
//	 @Override
//	    public int hashCode() {
//	     
//	        return this.userId.hashCode();
//	    }

}