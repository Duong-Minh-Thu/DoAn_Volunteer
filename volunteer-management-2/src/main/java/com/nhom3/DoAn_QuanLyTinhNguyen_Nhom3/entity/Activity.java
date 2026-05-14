package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.ActivityStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private User organization;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(length = 255)
    private String location;

    @Column(nullable = false)
    private Integer points = 0;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityStatus status = ActivityStatus.UPCOMING;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Registration> registrations = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Feedback> feedbacks = new ArrayList<>();

    public Activity() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getOrganization() { return organization; }
    public void setOrganization(User organization) { this.organization = organization; }

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

    public List<Registration> getRegistrations() { return registrations; }
    public List<Feedback> getFeedbacks() { return feedbacks; }

    // ===== Static Builder =====
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Activity a = new Activity();

        public Builder organization(User val) { a.organization = val; return this; }
        public Builder title(String val) { a.title = val; return this; }
        public Builder description(String val) { a.description = val; return this; }
        public Builder startDate(LocalDateTime val) { a.startDate = val; return this; }
        public Builder endDate(LocalDateTime val) { a.endDate = val; return this; }
        public Builder location(String val) { a.location = val; return this; }
        public Builder points(Integer val) { a.points = val; return this; }
        public Builder maxParticipants(Integer val) { a.maxParticipants = val; return this; }
        public Builder status(ActivityStatus val) { a.status = val; return this; }

        public Activity build() { return a; }
    }
}
