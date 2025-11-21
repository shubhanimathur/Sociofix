package com.socioFix.Dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socioFix.Service.LocalDateTimeDeserializer;
import com.socioFix.Service.LocalDateTimeSerializer;

import jakarta.persistence.Column;



public class PostDto implements Serializable {

    //private image;
    //private time;
	
	
    private LocationDto location;
    private String description;
    private SectorDto sector;
    private String imagePath;
    
    private String organizationName;
    
    private String organizationEmail;
    
    private String censoredDescription;
    
    byte[] byteImage;
    
   private String stringOfImage;
   public String getStringOfImage() {
       return stringOfImage;
   }

   public void setStringOfImage(String stringOfImage) {
       this.stringOfImage = stringOfImage;
   }
	
	
    
    public byte[] getByteImage() {
        return byteImage;
    }

    public void setByteImage(byte[] byteImage) {
        this.byteImage = byteImage;
    }


    private String profileImg;
    public String getProfileImg() {
        return profileImg;
    }


    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    
    
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

	private Integer postId;
    public Integer getPostId() {
		return this.postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

    public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	
	private String userEmail;
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
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;
	
	@JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime whenAt;
	

	public LocalDateTime getWhenAt() {
		return whenAt;
	}

	public void setWhenAt(LocalDateTime whenAt) {
		this.whenAt = whenAt;
	}
	
    
	public Integer getUpvotes() {
		return this.upvotes;
	}

	public void setUpvotes(Integer upvotes) {
		this.upvotes = upvotes;
	}

    public PostDto() {
    	
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime timestamp) {
        this.createdAt = timestamp;
    }

   
    public PostDto(LocationDto singleLocationDto, String description, SectorDto sectorDto,LocalDateTime timestamp,String userEmail) {
        this.location = singleLocationDto;
        this.description = description;
        this.sector = sectorDto;
        this.createdAt= timestamp;
        this.userEmail = userEmail;
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



}
