package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.entity.User;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.Role;

import java.time.LocalDateTime;

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private String studentCode;
    private LocalDateTime createdAt;
    private Integer totalTrainingPoints; // tổng điểm rèn luyện tất cả học kỳ

    public UserResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getTotalTrainingPoints() { return totalTrainingPoints; }
    public void setTotalTrainingPoints(Integer totalTrainingPoints) { this.totalTrainingPoints = totalTrainingPoints; }

    // Factory từ User (không có điểm rèn luyện — dùng cho admin list)
    public static UserResponse from(User user) {
        UserResponse r = new UserResponse();
        r.id = user.getId();
        r.username = user.getUsername();
        r.email = user.getEmail();
        r.fullName = user.getFullName();
        r.role = user.getRole();
        r.studentCode = user.getStudentCode();
        r.createdAt = user.getCreatedAt();
        return r;
    }

    // Factory từ User kèm điểm rèn luyện
    public static UserResponse from(User user, Integer totalTrainingPoints) {
        UserResponse r = from(user);
        r.totalTrainingPoints = totalTrainingPoints;
        return r;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final UserResponse r = new UserResponse();

        public Builder id(Long val) { r.id = val; return this; }
        public Builder username(String val) { r.username = val; return this; }
        public Builder email(String val) { r.email = val; return this; }
        public Builder fullName(String val) { r.fullName = val; return this; }
        public Builder role(Role val) { r.role = val; return this; }
        public Builder studentCode(String val) { r.studentCode = val; return this; }
        public Builder createdAt(LocalDateTime val) { r.createdAt = val; return this; }
        public Builder totalTrainingPoints(Integer val) { r.totalTrainingPoints = val; return this; }

        public UserResponse build() { return r; }
    }
}
