package com.socioFix.model;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the opinions_drive database table.
 * 
 */
@Entity
@Table(name="opinions_drive")
public class OpinionsDrive  {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="opinions_drive_id")
	private Integer opinionsDriveId;

	private String opinion;

	//bi-directional many-to-one association to Drive
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="drive_id",foreignKey = @ForeignKey(name = "fk_drive_id"))
	private Drive drive;

	//bi-directional many-to-one association to Organization
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="user_id",foreignKey = @ForeignKey(name = "fk_user_id"))
	private User user;

	public OpinionsDrive() {
	}

	public Integer getOpinionsDriveId() {
		return this.opinionsDriveId;
	}

	public void setOpinionsDriveId(Integer opinionsDriveId) {
		this.opinionsDriveId = opinionsDriveId;
	}

	public String getOpinion() {
		return this.opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public Drive getDrive() {
		return this.drive;
	}

	public void setDrive(Drive drive) {
		this.drive = drive;
	}


	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}