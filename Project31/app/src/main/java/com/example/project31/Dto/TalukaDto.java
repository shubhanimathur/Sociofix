package com.example.project31.Dto;

import java.io.Serializable;

public class TalukaDto implements Serializable {



//    private String  talukaId;
//
//    public String getTalukaId() {
//        return talukaId;
//    }
//
//    public void setTalukaId(String talukaId) {
//        this.talukaId = talukaId;
//    }
//
//    public TalukaDto(String talukaId) {
//        this.talukaId = talukaId;
//    }
//
//    public TalukaDto(){
//
//    }

    private String  talukaId;
    private String talukaPolygon;
    public String getTalukaId() {
        return talukaId;
    }

    public void setTalukaId(String talukaId) {
        this.talukaId = talukaId;
    }

    public TalukaDto(String talukaId) {
        this.talukaId = talukaId;
    }
    public TalukaDto(String talukaId, String talukaPolygon) {
        this.talukaId = talukaId;
        this.talukaPolygon = talukaPolygon;
    }
    public TalukaDto(){

    }
    public String getTalukaPolygon() {
        return talukaPolygon;
    }


    public void setTalukaPolygon(String talukaPolygon) {
        this.talukaPolygon = talukaPolygon;
    }

    @Override
    public String toString() {
        return getTalukaId();
    }
}
