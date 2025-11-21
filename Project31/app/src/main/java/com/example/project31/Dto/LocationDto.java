package com.example.project31.Dto;

import java.io.Serializable;

public class LocationDto implements Serializable {

    private Double latitude;
    private Double longitude;

    private String landmark;

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public Double getLatitude() {
        return latitude;
    }

    public LocationDto(Double latitude, Double longtitude) {
        this.latitude = latitude;
        this.longitude = longtitude;
    }
    public LocationDto() {

    }
    private String areaName;


    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public LocationDto(Double latitude, Double longitude, String state, String taluka, String areaId, String areaName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
        this.taluka = taluka;
        this.areaId = areaId;
        this.areaName= areaName;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longtitude) {
        this.longitude = longtitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    private String state;
    private String taluka;
    private String areaId;



}