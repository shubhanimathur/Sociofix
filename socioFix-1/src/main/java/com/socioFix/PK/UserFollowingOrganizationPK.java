package com.socioFix.PK;


import java.io.Serializable;
import jakarta.persistence.*;

/**
 * The primary key class for the user_following_organization database table.
 * 
 */
@Embeddable
public class UserFollowingOrganizationPK {


	@Column(name="user_id", insertable=false, updatable=false)
	private String userId;

	@Column(name="organization_id", insertable=false, updatable=false)
	private String organizationId;

	public UserFollowingOrganizationPK() {
	}
	public String getUserId() {
		return this.userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrganizationId() {
		return this.organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserFollowingOrganizationPK)) {
			return false;
		}
		UserFollowingOrganizationPK castOther = (UserFollowingOrganizationPK)other;
		return 
			this.userId.equals(castOther.userId)
			&& this.organizationId.equals(castOther.organizationId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.organizationId.hashCode();
		
		return hash;
	}
}
