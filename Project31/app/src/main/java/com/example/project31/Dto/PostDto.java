package com.example.project31.Dto;

//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import android.graphics.Bitmap;

import java.io.Serializable;
import java.time.LocalDateTime;



public class PostDto implements Serializable {

    //private image
    private LocationDto location;
    private String description;
    private SectorDto sector;
    private String userEmail;

    public byte[] getByteImage() {
        return byteImage;
    }

    public void setByteImage(byte[] byteImage) {
        this.byteImage = byteImage;
    }

    //    private transient Bitmap bitmap;
    byte[] byteImage;


    private transient Bitmap bitmap;

    private transient String Uri;

    public String getStringOfImage() {
        return stringOfImage;
    }

    public void setStringOfImage(String stringOfImage) {
        this.stringOfImage = stringOfImage;
    }

    private transient String imageFrom;
    private Integer postId;

    private String stringOfImage;

    private String organizationName;

    private String organizationEmail;

    private String censoredDescription;

    public String getCensoredDescription() {
        return censoredDescription;
    }

    public void setCensoredDescription(String censoredDescription) {
        this.censoredDescription = censoredDescription;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationEmail() {
        return organizationEmail;
    }

    public void setOrganizationEmail(String organizationId) {
        this.organizationEmail = organizationId;
    }


    public Integer getPostId() {
        return this.postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }


    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    private LocalDateTime whenAt;

    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public LocalDateTime getWhenAt() {
        return whenAt;
    }

    public void setWhenAt(LocalDateTime whenAt) {
        this.whenAt = whenAt;
    }


    public PostDto(LocationDto singleLocationDto, String description, SectorDto sectorDto,LocalDateTime timestamp,String userEmail,byte[] byteImage) {
        this.location = singleLocationDto;
        this.description = description;
        this.sector = sectorDto;
        this.whenAt = timestamp;
        this.userEmail = userEmail;
        this.byteImage = byteImage;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public PostDto(){


    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto singleLocationDto) {
        this.location = singleLocationDto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SectorDto getSector() {
        return sector;
    }

    public void setSector(SectorDto sector) {
        this.sector = sector;
    }

    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    private String profileImg;
    public String getProfileImg() {
        return profileImg;
    }


    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    private String name;

    private String status;



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }


    private Integer upvotes;
    // private Integer myUpvote;
    private Integer myUpvote;

    public void setMyUpvote(Integer myUpvote) {
        this.myUpvote = myUpvote;
    }
    public Integer getMyUpvote() {
        return myUpvote;
    }
    //	public void setMyUpvote(Integer myUpvote) {
//		this.myUpvote = myUpvote;
//	}


    public Integer getUpvotes() {
        return this.upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }





}
