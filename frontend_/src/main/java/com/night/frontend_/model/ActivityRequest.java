package com.night.frontend_.model;

public class ActivityRequest {
    private String title;
    private String description;
    private String startDate; // Backend expects string format date-time
    private String endDate;
    private String location;
    private int points;
    private int maxParticipants;
    private String status;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    private String avatar;
    private String gallery;

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getGallery() { return gallery; }
    public void setGallery(String gallery) { this.gallery = gallery; }
}
