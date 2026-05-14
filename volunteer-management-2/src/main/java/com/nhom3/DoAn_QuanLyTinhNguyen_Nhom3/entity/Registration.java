package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.RegistrationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "activity_id"}))
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status = RegistrationStatus.PENDING;

    @Column(name = "registered_at", updatable = false)
    private LocalDateTime registeredAt;

    public Registration() {}

    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public Activity getActivity() { return activity; }
    public void setActivity(Activity activity) { this.activity = activity; }

    public RegistrationStatus getStatus() { return status; }
    public void setStatus(RegistrationStatus status) { this.status = status; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }

    // ===== Static Builder =====
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Registration r = new Registration();

        public Builder student(User val) { r.student = val; return this; }
        public Builder activity(Activity val) { r.activity = val; return this; }
        public Builder status(RegistrationStatus val) { r.status = val; return this; }

        public Registration build() { return r; }
    }
}
