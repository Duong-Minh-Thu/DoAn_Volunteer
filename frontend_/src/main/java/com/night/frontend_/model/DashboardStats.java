package com.night.frontend_.model;

public class DashboardStats {
    private long totalActivities;
    private long totalStudents;
    private double completionRate;
    private long totalRewardPoints;

    public long getTotalActivities() { return totalActivities; }
    public void setTotalActivities(long totalActivities) { this.totalActivities = totalActivities; }

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public double getCompletionRate() { return completionRate; }
    public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }

    public long getTotalRewardPoints() { return totalRewardPoints; }
    public void setTotalRewardPoints(long totalRewardPoints) { this.totalRewardPoints = totalRewardPoints; }
}
