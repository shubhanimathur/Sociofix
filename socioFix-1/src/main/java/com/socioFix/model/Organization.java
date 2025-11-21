package com.socioFix.model;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.socioFix.model.geoLocations.Area;
import com.socioFix.model.geoLocations.Taluka;

import java.io.Serializable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**  @PrimaryKeyJoinColumn(name="organization_id", referencedColumnName = "user_id",foreignKey=@ForeignKey(name = "fk_organization_id"))
 * The persistent class for the organization database table.
 * 
 */
@Entity
@DiscriminatorValue("Organization")
@PrimaryKeyJoinColumn(name="organization_id", referencedColumnName = "user_id",foreignKey=@ForeignKey(name = "fk_organization_id"))
public class Organization extends User {
	
	


	@Column(name="is_verified")
	private Boolean isVerified;

	@Column(name="unique_id")
	private Integer uniqueId;



	public Set<Taluka> getServingTalukas() {
		return servingTalukas;
	}

	public void setServingTalukas(Set<Taluka> servingTalukas) {
		this.servingTalukas = servingTalukas;
	}

	public Set<Area> getServingAreas() {
		return servingAreas;
	}

	public void setServingAreas(Set<Area> servingAreas) {
		this.servingAreas = servingAreas;
	}

	//bi-directional many-to-many association to Sector
//	@ManyToMany(mappedBy="organizations",fetch=FetchType.EAGER,cascade= { CascadeType.PERSIST, CascadeType.MERGE,
//		    CascadeType.DETACH, CascadeType.REFRESH,CascadeType.ALL})
//	private List<Sector> sectors ;
//	
//	public void addSector(Sector sector) {
//        if (sectors == null) {
//            sectors = new ArrayList<>();
//        }
//        sectors.add(sector);
//        sector.getOrganizations().add(this);
//    }

	
	@ManyToMany(fetch=FetchType.LAZY,cascade= { CascadeType.PERSIST, CascadeType.MERGE,
	CascadeType.DETACH, CascadeType.REFRESH,CascadeType.ALL})
	@JoinTable(
	name="organization_serving_sector"
	, joinColumns={
	@JoinColumn(name="organization_id",foreignKey = @ForeignKey(name = "fk_organization_id"))
	}
	, inverseJoinColumns={
			@JoinColumn(name="sector_id",foreignKey = @ForeignKey(name = "fk_sector_id"))
	}
	)
	private Set<Sector > sectors;
	
	
	
	

//
	//bi-directional many-to-one association to Drive
	@OneToMany(mappedBy="organization")
	private Set<Drive> drives;

//	//bi-directional many-to-one association to OpinionsDrive
//	@OneToMany(mappedBy="organization")
//	private List<OpinionsDrive> opinionsDrives;

	//bi-directional many-to-one association to Location
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="base_location_id",foreignKey = @ForeignKey(name = "fk_base_location_id"),nullable=true)
	private Location base_location;
//
//	//bi-directional many-to-many association to Location
//	@ManyToMany(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
//	@JoinTable(
//		name="organization_serving_location"
//		, joinColumns={
//			@JoinColumn(name="organization_id",foreignKey = @ForeignKey(name = "fk_organization_id"))
//			}
//		, inverseJoinColumns={
//			@JoinColumn(name="location_id",foreignKey = @ForeignKey(name = "fk_location_id"))
//			}
//		)
//	private List<Location> servingLocations; //locations
	
	
	//bi-directional many-to-many association to Taluka
	@ManyToMany(fetch=FetchType.LAZY,cascade ={ CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH,CascadeType.ALL})
	@JoinTable(
	name="organization_serving_taluka"
	, joinColumns={
	@JoinColumn(name="organization_id",foreignKey = @ForeignKey(name = "fk_organization_id"))
	}
	, inverseJoinColumns={
	@JoinColumn(name="taluka_id",foreignKey = @ForeignKey(name = "fk_taluka_id"))
	}
	)
	private Set<Taluka> servingTalukas; //talukas



	 @ManyToMany(fetch=FetchType.LAZY,cascade = { CascadeType.PERSIST, CascadeType.MERGE,
				CascadeType.DETACH, CascadeType.REFRESH,CascadeType.ALL})
	 @JoinTable(
	 name="organization_serving_area"
	 , joinColumns={
	 @JoinColumn(name="organization_id",foreignKey = @ForeignKey(name = "fk_organization_id"))
	 }
	 , inverseJoinColumns={
	 @JoinColumn(name="area_id",foreignKey = @ForeignKey(name = "fk_area_id"))
	 }
	 )
	 private Set<Area> servingAreas; //areas


	 public String getOrganizationId() {
	        return getUserId();
	    }

	    public void setOrganizationId(String organizationId) {
	        setUserId(organizationId);
	    }
	
	
	


	//bi-directional many-to-one association to Post
	@OneToMany(mappedBy="organization")
	private Set<Post> posts;

	//bi-directional many-to-many association to User
	@ManyToMany(mappedBy="followingOrganizations")
	private Set<User> followingUsers;
//
	//bi-directional many-to-one association to UserFollowingOrganization
	@OneToMany(mappedBy="organization")
	private Set<UserFollowingOrganization> userFollowingOrganizations;
	
	
	

	


	public Organization() {
	}



	public Boolean getIsVerified() {
		return this.isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Integer getUniqueId() {
		return this.uniqueId;
	}

	public void setUniqueId(Integer uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Set<Drive> getDrives() {
		return this.drives;
	}

	public void setDrives(Set<Drive> drives) {
		this.drives = drives;
	}

	public Drive addDrive(Drive drive) {
		getDrives().add(drive);
		drive.setOrganization(this);

		return drive;
	}

	public Drive removeDrive(Drive drive) {
		getDrives().remove(drive);
		drive.setOrganization(null);

		return drive;
	}

//	public List<OpinionsDrive> getOpinionsDrives() {
//		return this.opinionsDrives;
//	}
//
//	public void setOpinionsDrives(List<OpinionsDrive> opinionsDrives) {
//		this.opinionsDrives = opinionsDrives;
//	}
//
//	public OpinionsDrive addOpinionsDrive(OpinionsDrive opinionsDrive) {
//		getOpinionsDrives().add(opinionsDrive);
//		opinionsDrive.setOrganization(this);
//
//		return opinionsDrive;
//	}
//
//	public OpinionsDrive removeOpinionsDrive(OpinionsDrive opinionsDrive) {
//		getOpinionsDrives().remove(opinionsDrive);
//		opinionsDrive.setOrganization(null);
//
//		return opinionsDrive;
//	}
//
	public Location getBase_location() {
		return this.base_location;
	}

	public void setBase_location(Location location) {
		this.base_location = location;
	}




	
//
//	public User getUser() {
//		return this.user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
	public Set<Post> getPosts() {
		return this.posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public Post addPost(Post post) {
		getPosts().add(post);
		post.setOrganization(this);

		return post;
	}

	public Post removePost(Post post) {
		getPosts().remove(post);
		post.setOrganization(null);

		return post;
	}
//
	public Set<Sector> getSectors() {
		return this.sectors;
	}

	public void setSectors(Set<Sector> sectors) {
		this.sectors = sectors;
	}
//
	public Set<User> getFollowingUsers() {
		return this.followingUsers;
	}

	public void setFollowingUsers(Set<User> followingUsers) {
		this.followingUsers = followingUsers;
	}

	public Set<UserFollowingOrganization> getUserFollowingOrganizations() {
		return this.userFollowingOrganizations;
	}
//
	public void setUserFollowingOrganizations(Set<UserFollowingOrganization> userFollowingOrganizations) {
		this.userFollowingOrganizations = userFollowingOrganizations;
	}

	public UserFollowingOrganization addUserFollowingOrganization(UserFollowingOrganization userFollowingOrganization) {
		getUserFollowingOrganizations().add(userFollowingOrganization);
		userFollowingOrganization.setOrganization(this);

		return userFollowingOrganization;
	}

	public UserFollowingOrganization removeUserFollowingOrganization(UserFollowingOrganization userFollowingOrganization) {
		getUserFollowingOrganizations().remove(userFollowingOrganization);
		userFollowingOrganization.setOrganization(null);

		return userFollowingOrganization;
	}

	
	



	@Override
	public String toString() {
		
		String secs ="";
		for (Sector s : this.getSectors()) {
			secs+=s.getSectorId();
		}
		
	
		return "Organization [isVerified=" + isVerified + ", uniqueId=" + uniqueId + ", sectors=" + secs
				+ ", base_location=" + base_location + ", servingTalukas=" + servingTalukas + ", servingAreas="
				+ servingAreas + "]";
	}

	public Sector addSector(Sector sector) {
		getSectors().add(sector);
		sector.getOrganizations().add(this);

		return sector;
	}
	
	@Override
    public int hashCode() {
        return getOrganizationId().hashCode();
    }
	
	
}