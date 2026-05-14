package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.Registration;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.RegistrationStatus;

import java.time.LocalDateTime;

public class RegistrationResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private String studentCode;
    private Long activityId;
    private String activityTitle;
    private RegistrationStatus status;
    private LocalDateTime registeredAt;

    public RegistrationResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }

    public String getActivityTitle() { return activityTitle; }
    public void setActivityTitle(String activityTitle) { this.activityTitle = activityTitle; }

    public RegistrationStatus getStatus() { return status; }
    public void setStatus(RegistrationStatus status) { this.status = status; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }

    public static RegistrationResponse from(Registration reg) {
        RegistrationResponse r = new RegistrationResponse();
        r.id = reg.getId();
        r.studentId = reg.getStudent().getId();
        r.studentName = reg.getStudent().getFullName();
        r.studentCode = reg.getStudent().getStudentCode();
        r.activityId = reg.getActivity().getId();
        r.activityTitle = reg.getActivity().getTitle();
        r.status = reg.getStatus();
        r.registeredAt = reg.getRegisteredAt();
        return r;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final RegistrationResponse r = new RegistrationResponse();

        public Builder id(Long val) { r.id = val; return this; }
        public Builder studentId(Long val) { r.studentId = val; return this; }
        public Builder studentName(String val) { r.studentName = val; return this; }
        public Builder studentCode(String val) { r.studentCode = val; return this; }
        public Builder activityId(Long val) { r.activityId = val; return this; }
        public Builder activityTitle(String val) { r.activityTitle = val; return this; }
        public Builder status(RegistrationStatus val) { r.status = val; return this; }
        public Builder registeredAt(LocalDateTime val) { r.registeredAt = val; return this; }

        public RegistrationResponse build() { return r; }
    }
}
