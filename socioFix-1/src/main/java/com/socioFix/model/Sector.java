package com.socioFix.model;

import java.io.Serializable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the sector database table.
 * 
 */
@Entity
public class Sector {

	

	@Id
	@Column(name="sector_id")
	private String sectorId;


	//bi-directional many-to-one association to Post
	@OneToMany(mappedBy="sector")
	private Set<Post> posts;

	@OneToMany(mappedBy="sector")
	private Set<Drive> drives;

	
//	@Override
//	public String toString() {
//		
//		String orgs ="";
//		for (Organization o : this.getOrganizations()) {
//			orgs+=o.getUserId();
//		}
//		return "Sector [sectorId=" + sectorId + ", name=" + name + ", posts=" + posts + ", organizations="
//				+ orgs + "]";
//	}

	//bi-directional many-to-many association to Organization
//	@ManyToMany(fetch=FetchType.EAGER,cascade= { CascadeType.PERSIST, CascadeType.MERGE,
//		    CascadeType.DETACH, CascadeType.REFRESH,CascadeType.ALL})
//	@JoinTable(
//		name="organization_serving_sector"
//		, joinColumns={
//			@JoinColumn(name="sector_id",foreignKey = @ForeignKey(name = "fk_sector_id"))
//			}
//		, inverseJoinColumns={
//			@JoinColumn(name="organization_id",foreignKey = @ForeignKey(name = "fk_organization_id"))
//			}
//		)
	
	
	@ManyToMany(mappedBy="sectors",fetch=FetchType.LAZY,cascade= { CascadeType.PERSIST, CascadeType.MERGE,
		    CascadeType.DETACH, CascadeType.REFRESH,CascadeType.ALL})
	private Set<Organization> organizations  ;

	
	


	
	
	
	public Sector() {
	}

	public String getSectorId() {
		return this.sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	

	public Set<Post> getPosts() {
		return this.posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public Post addPost(Post post) {
		getPosts().add(post);
		post.setSector(this);

		return post;
	}

	public Post removePost(Post post) {
		getPosts().remove(post);
		post.setSector(null);

		return post;
	}

	

	public Set<Drive> getDrives() {
		return this.drives;
	}
	public void setDrives(Set<Drive> drives) {
		this.drives = drives;
	}
	public Drive addDrive(Drive drive) {
		getDrives().add(drive);
		drive.setSector(this);
		return drive;
	}
	public Drive removeDrive(Drive drive) {
		getDrives().remove(drive);
		drive.setSector(null);
		return drive;
	}


	public Set<Organization> getOrganizations() {
		return this.organizations;
	}

	public void setOrganizations(Set<Organization> organizations) {
		this.organizations = organizations;
	}
	
	public Organization addOrganization(Organization organization) {
		getOrganizations().add(organization);
		organization.getSectors().add(this);

		return organization;
	}
	
	

}