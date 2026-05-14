package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.Activity;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.ActivityStatus;

import java.time.LocalDateTime;

public class ActivityResponse {
    private Long id;
    private Long orgId;
    private String orgName;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private Integer points;
    private Integer maxParticipants;
    private ActivityStatus status;
    private LocalDateTime createdAt;

    public ActivityResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }

    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }

    public ActivityStatus getStatus() { return status; }
    public void setStatus(ActivityStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static ActivityResponse from(Activity activity) {
        ActivityResponse r = new ActivityResponse();
        r.id = activity.getId();
        r.orgId = activity.getOrganization().getId();
        r.orgName = activity.getOrganization().getFullName();
        r.title = activity.getTitle();
        r.description = activity.getDescription();
        r.startDate = activity.getStartDate();
        r.endDate = activity.getEndDate();
        r.location = activity.getLocation();
        r.points = activity.getPoints();
        r.maxParticipants = activity.getMaxParticipants();
        r.status = activity.getStatus();
        r.createdAt = activity.getCreatedAt();
        return r;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final ActivityResponse r = new ActivityResponse();

        public Builder id(Long val) { r.id = val; return this; }
        public Builder orgId(Long val) { r.orgId = val; return this; }
        public Builder orgName(String val) { r.orgName = val; return this; }
        public Builder title(String val) { r.title = val; return this; }
        public Builder description(String val) { r.description = val; return this; }
        public Builder startDate(LocalDateTime val) { r.startDate = val; return this; }
        public Builder endDate(LocalDateTime val) { r.endDate = val; return this; }
        public Builder location(String val) { r.location = val; return this; }
        public Builder points(Integer val) { r.points = val; return this; }
        public Builder maxParticipants(Integer val) { r.maxParticipants = val; return this; }
        public Builder status(ActivityStatus val) { r.status = val; return this; }
        public Builder createdAt(LocalDateTime val) { r.createdAt = val; return this; }

        public ActivityResponse build() { return r; }
    }
}
