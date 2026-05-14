package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.RegistrationStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateRegistrationStatusRequest {

    @NotNull(message = "Status không được trống")
    private RegistrationStatus status;

    public RegistrationStatus getStatus() { return status; }
    public void setStatus(RegistrationStatus status) { this.status = status; }
}
