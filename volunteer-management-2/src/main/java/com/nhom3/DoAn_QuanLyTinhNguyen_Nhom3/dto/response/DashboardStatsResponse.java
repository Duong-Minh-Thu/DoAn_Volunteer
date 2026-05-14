package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response;

public class DashboardStatsResponse {
    private long totalActivities;
    private long totalStudents;
    private long totalOrganizations;
    private long totalRegistrations;
    private long totalAttended;

    public DashboardStatsResponse() {}

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

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final DashboardStatsResponse r = new DashboardStatsResponse();

        public Builder totalActivities(long val) { r.totalActivities = val; return this; }
        public Builder totalStudents(long val) { r.totalStudents = val; return this; }
        public Builder totalOrganizations(long val) { r.totalOrganizations = val; return this; }
        public Builder totalRegistrations(long val) { r.totalRegistrations = val; return this; }
        public Builder totalAttended(long val) { r.totalAttended = val; return this; }

        public DashboardStatsResponse build() { return r; }
    }
}
