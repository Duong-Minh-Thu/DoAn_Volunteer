package com.night.frontend_.model;

public class DashboardStats {
    private long totalActivities;
    private long totalStudents;
    private long totalOrganizations;
    private long totalRegistrations;
    private long totalAttended;

    public long getTotalActivities() { return totalActivities; }
    public void setTotalActivities(long totalActivities) { this.totalActivities = totalActivities; }

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public long getTotalOrganizations() { return totalOrganizations; }
    public void setTotalOrganizations(long totalOrganizations) { this.totalOrganizations = totalOrganizations; }

    public long getTotalRegistrations() { return totalRegistrations; }
    public void setTotalRegistrations(long totalRegistrations) { this.totalRegistrations = totalRegistrations; }

    public long getTotalAttended() { return totalAttended; }
    public void setTotalAttended(long totalAttended) { this.totalAttended = totalAttended; }

    public double getCompletionRate() {
        if (totalRegistrations == 0) return 0.0;
        return ((double) totalAttended / totalRegistrations) * 100.0;
    }
}
