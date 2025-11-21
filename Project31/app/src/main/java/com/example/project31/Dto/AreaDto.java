package com.example.project31.Dto;

import java.io.Serializable;

public class AreaDto implements Serializable {

    private String areaId;

    public AreaDto(String areaId, String areaName, String talukaName) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.talukaName = talukaName;
        this.select=false;
    }


    private String areaName;

    private Double latitude;

    private Double longitude;


    public Double getLatitude() {
        return latitude;
    }


    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }


    public Double getLongitude() {
        return longitude;
    }


    public void setLongitude(Double longtitiude) {
        this.longitude = longtitiude;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    private boolean select;



    public String getAreaId() {
        return areaId;
    }



    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }



    public String getAreaName() {
        return areaName;
    }



    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }



    public String getTalukaName() {
        return talukaName;
    }



    public void setTalukaName(String talukaName) {
        this.talukaName = talukaName;
    }



    private String talukaName;

    public AreaDto(){

    }

    @Override
    public String toString() {
        return getAreaName();
    }
}
