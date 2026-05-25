package com.night.frontend_.model;

public class LeaderboardEntry {
    private String fullName;
    private String email;
    private int totalActivities;
    private int completedActivities;
    private int totalPoints;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getTotalActivities() { return totalActivities; }
    public void setTotalActivities(int totalActivities) { this.totalActivities = totalActivities; }

    public int getCompletedActivities() { return completedActivities; }
    public void setCompletedActivities(int completedActivities) { this.completedActivities = completedActivities; }

    public int getTotalPoints() { return totalPoints; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
}
