package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.controller;

import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.LoginRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.request.RegisterRequest;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.ApiResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.dto.response.AuthResponse;
import com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Đăng ký và đăng nhập")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Đăng ký tài khoản mới (STUDENT hoặc ORG)")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công", response));
    }

    @Operation(summary = "Đăng nhập, lấy JWT Access Token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
    }
}
