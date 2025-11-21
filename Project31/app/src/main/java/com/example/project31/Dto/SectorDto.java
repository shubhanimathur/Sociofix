package com.example.project31.Dto;

import java.io.Serializable;

public class SectorDto implements Serializable {


    public SectorDto(String name) {
        this.sectorId = name;
        this.select=false;
    }

    public String getSectorId() {
        return sectorId;
    }

    public void setSectorId(String sectorId) {
        this.sectorId = sectorId;
    }

    private String sectorId;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    private boolean select;

    @Override
    public String toString() {
        return getSectorId();
    }
}
