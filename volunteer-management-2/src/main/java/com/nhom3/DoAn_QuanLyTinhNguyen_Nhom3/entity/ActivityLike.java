package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_likes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "activity_id"}))
public class ActivityLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public ActivityLike() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public Activity getActivity() { return activity; }
    public void setActivity(Activity activity) { this.activity = activity; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    // ===== Static Builder =====
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final ActivityLike like = new ActivityLike();

        public Builder student(User val) { like.student = val; return this; }
        public Builder activity(Activity val) { like.activity = val; return this; }

        public ActivityLike build() { return like; }
    }
}
