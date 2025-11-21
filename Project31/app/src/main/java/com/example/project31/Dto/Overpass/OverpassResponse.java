package com.example.project31.Dto.Overpass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OverpassResponse {
    @SerializedName("elements")
    private List<Element> elements;

    public List<Element> getElements() {
        return elements;
    }
}


