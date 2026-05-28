package com.night.frontend_.service;

import com.night.frontend_.model.User;
import com.night.frontend_.model.ApiResponse;
import com.night.frontend_.model.PageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {

    private final RestTemplate restTemplate;

    @Value("${backend.base.url:http://localhost:8082}/api/users")
    private String apiUrl;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<User> getAllUsers() {
        try {
            ResponseEntity<ApiResponse<PageResponse<User>>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<PageResponse<User>>>() {}
            );
            if (response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData().getContent();
            }
        } catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
        }
        return List.of();
    }

    public User getUserById(Long id) {
        try {
            ResponseEntity<ApiResponse<User>> response = restTemplate.exchange(
                    apiUrl + "/" + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<User>>() {}
            );
            if (response.getBody() != null) {
                return response.getBody().getData();
            }
        } catch (Exception e) {
            System.err.println("Error fetching user " + id + ": " + e.getMessage());
        }
        return null;
    }

    public User getProfile() {
        try {
            ResponseEntity<ApiResponse<User>> response = restTemplate.exchange(
                    apiUrl + "/profile",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<User>>() {}
            );
            if (response.getBody() != null) {
                return response.getBody().getData();
            }
        } catch (Exception e) {
            System.err.println("Error fetching profile: " + e.getMessage());
        }
        return null;
    }

    public boolean updateProfile(String fullName, String email) {
        try {
            java.util.Map<String, String> request = java.util.Map.of("fullName", fullName, "email", email);
            ResponseEntity<ApiResponse<User>> response = restTemplate.exchange(
                    apiUrl + "/profile",
                    HttpMethod.PUT,
                    new org.springframework.http.HttpEntity<>(request),
                    new ParameterizedTypeReference<ApiResponse<User>>() {}
            );
            return response.getBody() != null && response.getBody().isSuccess();
        } catch (Exception e) {
            System.err.println("Error updating profile: " + e.getMessage());
        }
        return false;
    }

    public boolean updateAvatar(String avatarBase64) {
        try {
            ResponseEntity<ApiResponse<User>> response = restTemplate.exchange(
                    apiUrl + "/profile/avatar",
                    HttpMethod.PUT,
                    new org.springframework.http.HttpEntity<>(avatarBase64),
                    new ParameterizedTypeReference<ApiResponse<User>>() {}
            );
            return response.getBody() != null && response.getBody().isSuccess();
        } catch (Exception e) {
            System.err.println("Error updating avatar: " + e.getMessage());
        }
        return false;
    }

    public boolean updateUser(Long id, String fullName, String email, String role) {
        try {
            java.util.Map<String, String> request = java.util.Map.of("fullName", fullName, "email", email, "role", role);
            ResponseEntity<ApiResponse<User>> response = restTemplate.exchange(
                    apiUrl + "/" + id,
                    HttpMethod.PUT,
                    new org.springframework.http.HttpEntity<>(request),
                    new ParameterizedTypeReference<ApiResponse<User>>() {}
            );
            return response.getBody() != null && response.getBody().isSuccess();
        } catch (Exception e) {
            System.err.println("Error updating user " + id + ": " + e.getMessage());
        }
        return false;
    }
}
