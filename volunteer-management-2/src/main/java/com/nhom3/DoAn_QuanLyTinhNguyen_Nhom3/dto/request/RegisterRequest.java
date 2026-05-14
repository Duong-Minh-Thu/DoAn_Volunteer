package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Username không được trống")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Password không được trống")
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Họ tên không được trống")
    private String fullName;

    @NotNull(message = "Role không được trống")
    private Role role;

    private String studentCode;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
}
