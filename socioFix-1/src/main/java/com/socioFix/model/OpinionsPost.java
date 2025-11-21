package com.socioFix.model;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the opinions_post database table.
 * 
 */
@Entity
@Table(name="opinions_post")
public class OpinionsPost  {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="opinions_post_id")
	private Integer opinionsPostId;

	private String opnion;

	//bi-directional many-to-one association to Post
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="post_id",foreignKey = @ForeignKey(name = "fk_post_id"))
	private Post post;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="user_id",foreignKey = @ForeignKey(name = "fk_user_id"))
	private User user;

	public OpinionsPost() {
	}

	public Integer getOpinionsPostId() {
		return this.opinionsPostId;
	}

	public void setOpinionsPostId(Integer opinionsPostId) {
		this.opinionsPostId = opinionsPostId;
	}

	public String getOpnion() {
		return this.opnion;
	}

	public void setOpnion(String opnion) {
		this.opnion = opnion;
	}

	public Post getPost() {
		return this.post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}