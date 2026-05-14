package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.controller;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.ActivityRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.BulkAttendanceRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.FeedbackRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.ActivityResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.ApiResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.FeedbackResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.RegistrationResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.enums.ActivityStatus;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service.ActivityService;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service.FeedbackService;
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
@RequestMapping("/api/activities")
@Tag(name = "Activities", description = "Quản lý hoạt động tình nguyện")
public class ActivityController {

    private final ActivityService activityService;
    private final RegistrationService registrationService;
    private final FeedbackService feedbackService;

    public ActivityController(ActivityService activityService,
                              RegistrationService registrationService,
                              FeedbackService feedbackService) {
        this.activityService = activityService;
        this.registrationService = registrationService;
        this.feedbackService = feedbackService;
    }

    @Operation(summary = "Lấy/Tìm kiếm danh sách hoạt động (công khai). Lọc theo status, tên tổ chức, địa điểm")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ActivityResponse>>> getAllActivities(
            @RequestParam(required = false) ActivityStatus status,
            @RequestParam(required = false) String orgName,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Page<ActivityResponse> activities = activityService.getAllActivities(status, orgName, location, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(ApiResponse.success(activities));
    }

    @Operation(summary = "Xem chi tiết một hoạt động (công khai)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityResponse>> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(activityService.getActivityById(id)));
    }

    @Operation(summary = "Tạo hoạt động mới (ORG hoặc ADMIN)")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    @PreAuthorize("hasRole('ORG') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ActivityResponse>> createActivity(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ActivityRequest request) {
        ActivityResponse response = activityService.createActivity(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo hoạt động thành công", response));
    }

    @Operation(summary = "Cập nhật hoạt động (ORG chủ sở hữu hoặc ADMIN)")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORG') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ActivityResponse>> updateActivity(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ActivityRequest request) {
        ActivityResponse response = activityService.updateActivity(id, userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", response));
    }

    @Operation(summary = "Xóa/Hủy hoạt động (ORG chủ sở hữu hoặc ADMIN)")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORG') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteActivity(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        activityService.deleteActivity(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Xóa hoạt động thành công", null));
    }

    @Operation(summary = "Lấy danh sách sinh viên đăng ký hoạt động này (ORG/ADMIN)")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}/students")
    @PreAuthorize("hasRole('ORG') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<RegistrationResponse>>> getStudentsByActivity(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<RegistrationResponse> students = registrationService
                .getStudentsByActivity(id, userDetails.getUsername(), PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(students));
    }

    @Operation(summary = "Chốt điểm danh hàng loạt và cộng điểm rèn luyện (ORG/ADMIN)")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{id}/attendance")
    @PreAuthorize("hasRole('ORG') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> bulkAttendance(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BulkAttendanceRequest request) {
        int updated = registrationService.bulkAttendance(id, userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Chốt điểm danh thành công cho " + updated + " sinh viên", null));
    }

    @Operation(summary = "Sinh viên đánh giá hoạt động sau khi hoàn thành")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{id}/feedback")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<FeedbackResponse>> createFeedback(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody FeedbackRequest request) {
        FeedbackResponse response = feedbackService.createFeedback(id, userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Đánh giá thành công", response));
    }

    @Operation(summary = "Lấy danh sách đánh giá của một hoạt động (công khai)")
    @GetMapping("/{id}/feedback")
    public ResponseEntity<ApiResponse<Page<FeedbackResponse>>> getFeedbacks(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<FeedbackResponse> feedbacks = feedbackService.getFeedbacksByActivity(id, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(feedbacks));
    }

    @Operation(summary = "Xóa đánh giá (chủ đánh giá hoặc ADMIN)")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}/feedback/{feedbackId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteFeedback(
            @PathVariable Long id,
            @PathVariable Long feedbackId,
            @AuthenticationPrincipal UserDetails userDetails) {
        feedbackService.deleteFeedback(id, feedbackId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Xóa đánh giá thành công", null));
    }
}

