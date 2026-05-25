package com.night.frontend_.service;

import com.night.frontend_.model.Activity;
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
public class ActivityService {

    private final RestTemplate restTemplate;

    @Value("${backend.base.url:http://localhost:8082}/api/activities")
    private String apiUrl;

    public ActivityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Activity> getAllActivities() {
        try {
            ResponseEntity<ApiResponse<PageResponse<Activity>>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<PageResponse<Activity>>>() {}
            );
            if (response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData().getContent();
            }
        } catch (Exception e) {
            System.err.println("Error fetching activities: " + e.getMessage());
        }
        return List.of();
    }

    public Activity getActivityById(Long id) {
        try {
            ResponseEntity<ApiResponse<Activity>> response = restTemplate.exchange(
                    apiUrl + "/" + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<Activity>>() {}
            );
            if (response.getBody() != null) {
                return response.getBody().getData();
            }
        } catch (Exception e) {
            System.err.println("Error fetching activity " + id + ": " + e.getMessage());
        }
        return null;
    }

    public boolean createActivity(com.night.frontend_.model.ActivityRequest request) {
        try {
            ResponseEntity<ApiResponse<Activity>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    new org.springframework.http.HttpEntity<>(request),
                    new ParameterizedTypeReference<ApiResponse<Activity>>() {}
            );
            return response.getBody() != null && response.getBody().isSuccess();
        } catch (Exception e) {
            System.err.println("Error creating activity: " + e.getMessage());
            return false;
        }
    }

    public boolean updateActivity(Long id, com.night.frontend_.model.ActivityRequest request) {
        try {
            ResponseEntity<ApiResponse<Activity>> response = restTemplate.exchange(
                    apiUrl + "/" + id,
                    HttpMethod.PUT,
                    new org.springframework.http.HttpEntity<>(request),
                    new ParameterizedTypeReference<ApiResponse<Activity>>() {}
            );
            return response.getBody() != null && response.getBody().isSuccess();
        } catch (Exception e) {
            System.err.println("Error updating activity: " + e.getMessage());
            return false;
        }
    }
}
