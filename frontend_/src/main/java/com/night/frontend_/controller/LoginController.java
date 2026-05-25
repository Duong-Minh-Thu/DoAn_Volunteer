package com.night.frontend_.controller;

import com.night.frontend_.model.ApiResponse;
import com.night.frontend_.model.AuthResponse;
import com.night.frontend_.model.LoginRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class LoginController {

    private final RestTemplate restTemplate;

    @Value("${backend.base.url:http://localhost:8082}/api/auth")
    private String authApiUrl;

    public LoginController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpSession session,
                               Model model) {
        try {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(username);
            loginRequest.setPassword(password);

            HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest);

            ResponseEntity<ApiResponse<AuthResponse>> response = restTemplate.exchange(
                    authApiUrl + "/login",
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<ApiResponse<AuthResponse>>() {}
            );

            if (response.getBody() != null && response.getBody().getData() != null) {
                AuthResponse authResponse = response.getBody().getData();
                session.setAttribute("JWT_TOKEN", authResponse.getAccessToken());
                session.setAttribute("USERNAME", authResponse.getUsername());
                session.setAttribute("USER_ROLE", authResponse.getRole());
                return "redirect:/";
            } else {
                model.addAttribute("error", "Tài khoản hoặc mật khẩu không đúng.");
                return "login";
            }
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không chính xác!");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi kết nối tới máy chủ.");
            return "login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
