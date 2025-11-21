package com.socioFix.model;


import java.io.Serializable;

import com.socioFix.PK.UserFollowingOrganizationPK;

import jakarta.persistence.*;


/**
 * The persistent class for the user_following_organization database table.
 * 
 */
@Entity
@Table(name="user_following_organization")
public class UserFollowingOrganization implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserFollowingOrganizationPK id;

	//bi-directional many-to-one association to Organization
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="organization_id",insertable =  false, updatable = false)
	private Organization organization;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="user_id",insertable =  false, updatable = false)
	private User user;

	public UserFollowingOrganization() {
	}

	public UserFollowingOrganizationPK getId() {
		return this.id;
	}

	public void setId(UserFollowingOrganizationPK id) {
		this.id = id;
	}

	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}