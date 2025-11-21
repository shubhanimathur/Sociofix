package com.example.project31.Dto.Overpass;

import com.google.gson.annotations.SerializedName;

public class Tags {

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }
}
