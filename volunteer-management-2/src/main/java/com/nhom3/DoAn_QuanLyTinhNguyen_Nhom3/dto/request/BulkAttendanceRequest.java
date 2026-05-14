package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class BulkAttendanceRequest {

    @NotEmpty(message = "Danh sách registration IDs không được trống")
    private List<Long> registrationIds;

    public List<Long> getRegistrationIds() { return registrationIds; }
    public void setRegistrationIds(List<Long> registrationIds) { this.registrationIds = registrationIds; }
}
