package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.controller;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.ApiResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.LeaderboardEntryResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.MyRankResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rankings")
@Tag(name = "Rankings & Leaderboard", description = "Bảng xếp hạng điểm rèn luyện")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @Operation(summary = "Lấy bảng xếp hạng sinh viên (công khai). Lọc theo semester hoặc all-time")
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<LeaderboardEntryResponse>>> getLeaderboard(
            @RequestParam(required = false) String semester,
            @RequestParam(defaultValue = "50") int limit) {
        List<LeaderboardEntryResponse> leaderboard = rankingService.getLeaderboard(semester, Math.min(limit, 100));
        return ResponseEntity.ok(ApiResponse.success(leaderboard));
    }

    @Operation(summary = "Sinh viên xem thứ hạng của bản thân")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/my-rank")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<MyRankResponse>> getMyRank(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String semester) {
        MyRankResponse response = rankingService.getMyRank(userDetails.getUsername(), semester);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
