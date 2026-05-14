package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.ActivityStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ActivityRequest {

    @NotBlank(message = "Tiêu đề không được trống")
    private String title;

    private String description;

    @NotNull @Future(message = "Ngày bắt đầu phải trong tương lai")
    private LocalDateTime startDate;

    @NotNull @Future(message = "Ngày kết thúc phải trong tương lai")
    private LocalDateTime endDate;

    private String location;

    @Min(value = 0, message = "Điểm không thể âm")
    private Integer points = 0;

    private Integer maxParticipants;

    private ActivityStatus status;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }

    public ActivityStatus getStatus() { return status; }
    public void setStatus(ActivityStatus status) { this.status = status; }
}
