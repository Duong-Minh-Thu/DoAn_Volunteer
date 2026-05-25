package com.night.frontend_.controller;

import com.night.frontend_.model.ApiResponse;
import com.night.frontend_.model.DashboardStats;
import com.night.frontend_.model.LeaderboardEntry;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class StatisticsController {

    private final RestTemplate restTemplate;

    @Value("${backend.base.url:http://localhost:8082}/api")
    private String apiUrl;

    public StatisticsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/statistics")
    public String showStatistics(Model model, HttpSession session) {
        // Require login for stats dashboard
        if (session.getAttribute("JWT_TOKEN") == null) {
            return "redirect:/login";
        }

        // Fetch Dashboard Stats
        try {
            ResponseEntity<ApiResponse<DashboardStats>> statsResponse = restTemplate.exchange(
                    apiUrl + "/stats/dashboard",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<DashboardStats>>() {}
            );
            if (statsResponse.getBody() != null && statsResponse.getBody().getData() != null) {
                model.addAttribute("stats", statsResponse.getBody().getData());
            } else {
                model.addAttribute("stats", new DashboardStats()); // default empty
            }
        } catch (Exception e) {
            model.addAttribute("stats", new DashboardStats());
            System.err.println("Error fetching stats dashboard: " + e.getMessage());
        }

        // Fetch Top Students Rankings
        try {
            ResponseEntity<ApiResponse<List<LeaderboardEntry>>> rankResponse = restTemplate.exchange(
                    apiUrl + "/rankings/students?limit=10",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<List<LeaderboardEntry>>>() {}
            );
            if (rankResponse.getBody() != null && rankResponse.getBody().getData() != null) {
                model.addAttribute("topStudents", rankResponse.getBody().getData());
            } else {
                model.addAttribute("topStudents", List.of());
            }
        } catch (Exception e) {
            model.addAttribute("topStudents", List.of());
            System.err.println("Error fetching rankings: " + e.getMessage());
        }

        return "statistics";
    }
}
