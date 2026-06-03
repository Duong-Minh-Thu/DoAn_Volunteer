package com.night.frontend_.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationPageController {

    @GetMapping("/notifications")
    public String notificationsPage(HttpSession session, Model model) {
        String token = (String) session.getAttribute("JWT_TOKEN");
        if (token == null) {
            return "redirect:/login";
        }
        
        String username = (String) session.getAttribute("USERNAME");
        String role = (String) session.getAttribute("USER_ROLE");
        
        model.addAttribute("username", username != null ? username : "Người dùng");
        model.addAttribute("role", role != null ? role : "USER");
        
        return "notifications/list";
    }
}
