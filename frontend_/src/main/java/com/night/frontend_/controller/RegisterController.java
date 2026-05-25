package com.night.frontend_.controller;

import com.night.frontend_.model.ApiResponse;
import com.night.frontend_.model.AuthResponse;
import com.night.frontend_.model.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class RegisterController {

    @Value("${backend.base.url:http://localhost:8082}/api/auth")
    private String authApiUrl;

    private final RestTemplate restTemplate;

    public RegisterController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model, HttpSession session) {
        if (session.getAttribute("JWT_TOKEN") != null) {
            return "redirect:/"; // Already logged in
        }
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("registerRequest") RegisterRequest request, Model model, HttpSession session) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            model.addAttribute("registerRequest", request);
            return "register";
        }

        try {
            ResponseEntity<ApiResponse<AuthResponse>> response = restTemplate.exchange(
                    authApiUrl + "/register",
                    HttpMethod.POST,
                    new org.springframework.http.HttpEntity<>(request),
                    new ParameterizedTypeReference<ApiResponse<AuthResponse>>() {}
            );

            ApiResponse<AuthResponse> apiResponse = response.getBody();
            if (apiResponse != null && apiResponse.isSuccess() && apiResponse.getData() != null) {
                // Đăng ký thành công và tự động đăng nhập
                AuthResponse authResponse = apiResponse.getData();
                session.setAttribute("JWT_TOKEN", authResponse.getAccessToken());
                session.setAttribute("USERNAME", authResponse.getUsername());
                session.setAttribute("USER_ROLE", authResponse.getRole());
                return "redirect:/";
            } else {
                model.addAttribute("error", apiResponse != null ? apiResponse.getMessage() : "Đăng ký thất bại");
                model.addAttribute("registerRequest", request);
                return "register";
            }
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            String errorMsg = "Lỗi hệ thống hoặc dữ liệu không hợp lệ. Vui lòng kiểm tra lại.";
            try {
                String responseBody = e.getResponseBodyAsString();
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                ApiResponse<?> apiResponse = mapper.readValue(responseBody, ApiResponse.class);
                if (apiResponse != null && apiResponse.getMessage() != null) {
                    errorMsg = apiResponse.getMessage();
                }
            } catch (Exception ex) {
                try {
                    String responseBody = e.getResponseBodyAsString();
                    if (responseBody.contains("message\":\"")) {
                        int start = responseBody.indexOf("message\":\"") + 10;
                        int end = responseBody.indexOf("\"", start);
                        errorMsg = responseBody.substring(start, end);
                    }
                } catch (Exception ex2) {
                    // Ignore
                }
            }
            model.addAttribute("error", errorMsg);
            model.addAttribute("registerRequest", request);
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể kết nối đến máy chủ. Vui lòng thử lại sau.");
            model.addAttribute("registerRequest", request);
            return "register";
        }
    }
}
