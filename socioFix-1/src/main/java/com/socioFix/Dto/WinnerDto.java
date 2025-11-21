package com.socioFix.Dto;

public class WinnerDto {
    private String id;
    private String name;
    private Long numberOfPosts;

    public WinnerDto(String organizationId, String name, Long numberOfPosts) {
        this.id = organizationId;
        this.name = name;
        this.numberOfPosts = numberOfPosts;
    }

    // getters and setters
    public String getId() {
        return id;
    }

    public void setId(String organizationId) {
        this.id = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(Long numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }
}