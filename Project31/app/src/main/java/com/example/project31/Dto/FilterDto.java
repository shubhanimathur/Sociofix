package com.example.project31.Dto;

import com.google.gson.annotations.SerializedName;

public class FilterDto {

        @SerializedName("content")
        private String content;

    @SerializedName("censor-character")
    private String censor_character;

    @SerializedName("catalog")
    private String catalog;




    public String getCensor_character() {
        return censor_character;
    }

    public void setCensor_character(String censor_character) {
        this.censor_character = censor_character;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public FilterDto(String content, String censor_character, String catalog) {
        this.content = content;
        this.censor_character = censor_character;
        this.catalog = catalog;
    }
}

