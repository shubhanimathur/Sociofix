package com.socioFix.Dto;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socioFix.Service.LocalDateTimeDeserializer;
import com.socioFix.Service.LocalDateTimeSerializer;


public class DriveDto implements Serializable {

    private LocationDto location;

    private String description;

    private SectorDto sector;

    private String imagePath;

    private Integer driveId;
    
    private String censoredDescription;

    public String getCensoredDescription() {
        return censoredDescription;
    }

    public void setCensoredDescription(String censoredDescription) {
        this.censoredDescription = censoredDescription;
    }

    private String profileImg;
    
    public String getProfileImg() {
        return profileImg;
    }


    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    
 byte[] byteImage;
    
    public byte[] getByteImage() {
        return byteImage;
    }

    public void setByteImage(byte[] byteImage) {
        this.byteImage = byteImage;
    }

    private String stringOfImage;
    public String getStringOfImage() {
        return stringOfImage;
    }

    public void setStringOfImage(String stringOfImage) {
        this.stringOfImage = stringOfImage;
    }
    

    public Integer getDriveId() {

        return this.driveId;
    }

    public void setDriveId(Integer driveId) {

        this.driveId = driveId;
    }

    public String getImagePath() {

        return imagePath;
    }
    public void setImagePath(String imagePath) {

        this.imagePath = imagePath;
    }
    private String userEmail;


    private String name;



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private Integer volunteers;
    // private Integer myVolunteerStatus;
    private Integer myVolunteerStatus;

    public void setMyVolunteerStatus(Integer myVolunteerStatus) {
        this.myVolunteerStatus = myVolunteerStatus;
    }
    public Integer getMyVolunteerStatus() {
        return myVolunteerStatus;
    }
    public Integer getVolunteers() {
        return this.volunteers;
    }
    public void setVolunteers(Integer volunteers) {
        this.volunteers = volunteers;
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
    // public void setMyUpvote(Integer myUpvote) {
    // this.myUpvote = myUpvote;
    // }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

	@JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime whenAt;

    public Integer getUpvotes() {
        return this.upvotes;
    }
    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }
    public DriveDto() {
    }
    public LocalDateTime getWhenAt() {
        return whenAt;
    }
    public void setWhenAt(LocalDateTime timestamp) {
        this.whenAt = timestamp;
    }
    public DriveDto(LocationDto singleLocationDto, String description, SectorDto sectorDto,LocalDateTime timestamp,String userEmail) {
        this.location = singleLocationDto;
        this.description = description;
        this.sector = sectorDto;
        this.whenAt= timestamp;
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


