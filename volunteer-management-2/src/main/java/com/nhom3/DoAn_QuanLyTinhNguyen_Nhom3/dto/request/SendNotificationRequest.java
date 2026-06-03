package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request;

public class SendNotificationRequest {
    private String title;
    private String message;
    private String targetType; // "ALL", "STUDENTS", "ORGS"

    public SendNotificationRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}
