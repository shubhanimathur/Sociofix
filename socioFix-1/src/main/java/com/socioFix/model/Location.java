package com.socioFix.model;

import java.io.Serializable;
import jakarta.persistence.*;
//import org.postgresql.geometric.PGpolygon;
import java.util.List;


/**
 * The persistent class for the location database table.
 * 
 */
@Entity
public class Location {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="location_id")
	private Integer locationId;

//	private String area;
	
	private String areaId;

	//@Column(name="area_polygon")
	//private PGpolygon areaPolygon;



	private String taluka;

	private Double latitude;

	private Double longitude;

	private String landmark;

	private String state;
	
	private String areaName;
	

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}



	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
	
	

	//bi-directional many-to-one association to Drive
	@OneToMany(mappedBy="location")
	private List<Drive> drives;
//
//	//bi-directional many-to-one association to Organization
	@OneToMany(mappedBy="base_location")
	private List<Organization> base_locations;
//
	//bi-directional many-to-many association to Organization
//	@ManyToMany(mappedBy="servingLocations")
//	private List<Organization> allOrganizationsServingInLocation;//organizations2;

	//bi-directional many-to-one association to Post
	@OneToMany(mappedBy="location")
	private List<Post> posts;

	public Location() {
	}

	public Integer getLocationId() {
		return this.locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
//
//	public String getArea() {
//		return this.area;
//	}
//
//	public void setArea(String area) {
//		this.area = area;
//	}
//
//	public PGpolygon getAreaPolygon() {
//		return this.areaPolygon;
//	}
//
//	public void setAreaPolygon(PGpolygon areaPolygon) {
//		this.areaPolygon = areaPolygon;
//	}

	public String getTaluka() {
		return this.taluka;
	}

	public void setTaluka(String city) {
		this.taluka = city;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getLandmark() {
		return this.landmark;
	}

	public void setLandmark(String state) {
		this.landmark = state;
	}

	public List<Drive> getDrives() {
		return this.drives;
	}

	public void setDrives(List<Drive> drives) {
		this.drives = drives;
	}

	public Drive addDrive(Drive drive) {
		getDrives().add(drive);
		drive.setLocation(this);

		return drive;
	}

	public Drive removeDrive(Drive drive) {
		getDrives().remove(drive);
		drive.setLocation(null);

		return drive;
	}
//
//	public List<Organization> getOrganizations1() {
//		return this.organizations1;
//	}
//
//	public void setOrganizations1(List<Organization> organizations1) {
//		this.organizations1 = organizations1;
//	}
//
//	public Organization addOrganizations1(Organization organizations1) {
//		getOrganizations1().add(organizations1);
//		organizations1.setLocation(this);
//
//		return organizations1;
//	}
//
//	public Organization removeOrganizations1(Organization organizations1) {
//		getOrganizations1().remove(organizations1);
//		organizations1.setLocation(null);
//
//		return organizations1;
//	}
//
//	public List<Organization> getAllOrganizationsServingInLocation() {
//		return this.allOrganizationsServingInLocation;
//	}
//
//	public void setAllOrganizationsServingInLocation(List<Organization> allOrganizationsServingInLocation) {
//		this.allOrganizationsServingInLocation = allOrganizationsServingInLocation;
//	}

	
	public List<Post> getPosts() {
		return this.posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Post addPost(Post post) {
		getPosts().add(post);
		post.setLocation(this);

		return post;
	}

	public Post removePost(Post post) {
		getPosts().remove(post);
		post.setLocation(null);

		return post;
	}

}