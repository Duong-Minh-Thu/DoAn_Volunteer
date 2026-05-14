package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.controller;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.RegistrationRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.UpdateRegistrationStatusRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.ApiResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.RegistrationResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrations")
@Tag(name = "Registrations", description = "Quản lý đăng ký hoạt động")
@SecurityRequirement(name = "Bearer Authentication")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Operation(summary = "Sinh viên đăng ký tham gia hoạt động")
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<RegistrationResponse>> register(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody RegistrationRequest request) {
        RegistrationResponse response = registrationService.register(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đăng ký thành công, chờ duyệt", response));
    }

    @Operation(summary = "Sinh viên xem lịch sử các hoạt động đã đăng ký")
    @GetMapping("/my-activities")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Page<RegistrationResponse>>> getMyActivities(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RegistrationResponse> activities = registrationService
                .getMyActivities(userDetails.getUsername(), PageRequest.of(page, size, Sort.by("registeredAt").descending()));
        return ResponseEntity.ok(ApiResponse.success(activities));
    }

    @Operation(summary = "ORG duyệt/từ chối đơn đăng ký (PENDING -> APPROVED/REJECTED)")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ORG') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistrationResponse>> updateStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateRegistrationStatusRequest request) {
        RegistrationResponse response = registrationService.updateStatus(id, userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái thành công", response));
    }
}
