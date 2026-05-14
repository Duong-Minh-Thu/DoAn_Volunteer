package com.nhom3.DoAn_QuanLyTinhNguyen_Nhom3.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Hệ thống Quản lý Hoạt động Tình nguyện - API",
        version = "1.0",
        description = "API cho hệ thống quản lý hoạt động tình nguyện dành cho sinh viên. " +
                      "Hỗ trợ đăng ký hoạt động, quản lý điểm danh, bảng xếp hạng và thống kê.",
        contact = @Contact(name = "Nhóm 3", email = "nhom3@example.com")
    )
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
