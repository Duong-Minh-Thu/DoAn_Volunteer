package com.night.frontend_.service;

import com.night.frontend_.model.Registration;
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
public class RegistrationService {

    private final RestTemplate restTemplate;

    @Value("${backend.base.url:http://localhost:8082}/api/registrations")
    private String apiUrl;

    public RegistrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Registration> getAllRegistrations() {
        return List.of();
    }

    public List<Registration> getMyRegistrations() {
        try {
            ResponseEntity<ApiResponse<PageResponse<Registration>>> response = restTemplate.exchange(
                    apiUrl + "/my-activities",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<PageResponse<Registration>>>() {}
            );
            if (response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData().getContent();
            }
        } catch (Exception e) {
            System.err.println("Error fetching my registrations: " + e.getMessage());
        }
        return List.of();
    }

    public Registration getRegistrationById(Long id) {
        try {
            ResponseEntity<ApiResponse<Registration>> response = restTemplate.exchange(
                    apiUrl + "/" + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<Registration>>() {}
            );
            if (response.getBody() != null) {
                return response.getBody().getData();
            }
        } catch (Exception e) {
            System.err.println("Error fetching registration " + id + ": " + e.getMessage());
        }
        return null;
    }
}
