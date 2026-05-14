package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.controller;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.CommentRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.ApiResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.CommentResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.LikeResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service.InteractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities")
@Tag(name = "Likes & Comments", description = "Thả tim và bình luận hoạt động")
public class InteractionController {

    private final InteractionService interactionService;

    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    // ==================== LIKES ====================

    @Operation(summary = "Toggle thả tim / bỏ tim hoạt động (chỉ STUDENT)")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{id}/likes")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<LikeResponse>> toggleLike(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        LikeResponse response = interactionService.toggleLike(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Xem số lượt thích của một hoạt động (công khai)")
    @GetMapping("/{id}/likes")
    public ResponseEntity<ApiResponse<LikeResponse>> getLikes(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        LikeResponse response = interactionService.getLikeStatus(id, username);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // ==================== COMMENTS ====================

    @Operation(summary = "Sinh viên đăng bình luận vào hoạt động")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{id}/comments")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CommentRequest request) {
        CommentResponse response = interactionService.createComment(id, userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Bình luận thành công", response));
    }

    @Operation(summary = "Lấy danh sách bình luận của hoạt động (công khai)")
    @GetMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getComments(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CommentResponse> comments = interactionService.getComments(id, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    @Operation(summary = "Sinh viên trả lời một bình luận")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{id}/comments/{commentId}/replies")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<CommentResponse>> createReply(
            @PathVariable Long id,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CommentRequest request) {
        CommentResponse response = interactionService.createReply(id, commentId, userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Trả lời thành công", response));
    }

    @Operation(summary = "Lấy danh sách trả lời của một bình luận (công khai)")
    @GetMapping("/{id}/comments/{commentId}/replies")
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getReplies(
            @PathVariable Long id,
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<CommentResponse> replies = interactionService.getReplies(id, commentId, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(replies));
    }

    @Operation(summary = "Xóa bình luận hoặc trả lời (chủ sở hữu hoặc ADMIN)")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}/comments/{commentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long id,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        interactionService.deleteComment(id, commentId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
