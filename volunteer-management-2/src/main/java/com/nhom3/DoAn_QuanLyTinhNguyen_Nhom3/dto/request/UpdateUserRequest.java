package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.Role;

public class UpdateUserRequest {
    private String fullName;
    private String email;
    private Role role;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
