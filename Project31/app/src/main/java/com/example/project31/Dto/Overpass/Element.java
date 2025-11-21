package com.example.project31.Dto.Overpass;

import com.google.gson.annotations.SerializedName;

public class Element {
    @SerializedName("tags")
    private Tags tags;



    public Tags getTags() {
        return tags;
    }


    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    @SerializedName("lat")
    private String lat;

    @SerializedName("lon")
    private String lon;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }


}
