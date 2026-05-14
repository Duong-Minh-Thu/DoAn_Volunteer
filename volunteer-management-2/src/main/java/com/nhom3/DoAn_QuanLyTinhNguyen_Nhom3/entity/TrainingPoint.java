package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "training_points",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "semester"}))
public class TrainingPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(nullable = false, length = 20)
    private String semester;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints = 0;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public TrainingPoint() {}

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }

    // ===== Static Builder =====
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final TrainingPoint tp = new TrainingPoint();

        public Builder student(User val) { tp.student = val; return this; }
        public Builder semester(String val) { tp.semester = val; return this; }
        public Builder totalPoints(Integer val) { tp.totalPoints = val; return this; }

        public TrainingPoint build() { return tp; }
    }
}
