package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.controller;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.ApiResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.DashboardStatsResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@Tag(name = "Statistics", description = "Thống kê hệ thống (Admin)")
@SecurityRequirement(name = "Bearer Authentication")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @Operation(summary = "Lấy thống kê tổng quan cho Admin Dashboard")
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success(statsService.getDashboardStats()));
    }
}
