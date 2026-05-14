package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request;

import jakarta.validation.constraints.NotNull;

public class RegistrationRequest {

    @NotNull(message = "activity_id không được trống")
    private Long activityId;

    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
}
