package com.night.frontend_.controller;

import com.night.frontend_.model.User;
import com.night.frontend_.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        String token = (String) session.getAttribute("JWT_TOKEN");
        if (token == null) {
            return "redirect:/login";
        }
        
        String username = (String) session.getAttribute("USERNAME");
        String role = (String) session.getAttribute("USER_ROLE");
        
        model.addAttribute("username", username != null ? username : "Người dùng");
        model.addAttribute("role", role != null ? role : "USER");
        
        User profile = userService.getProfile();
        model.addAttribute("profile", profile);
        if (profile != null) {
            session.setAttribute("USER_AVATAR", profile.getAvatar());
        }
        
        return "home";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            HttpSession session) {
        String token = (String) session.getAttribute("JWT_TOKEN");
        if (token == null) {
            return "redirect:/login";
        }
        
        boolean success = userService.updateProfile(fullName, email);
        if (success) {
            session.setAttribute("USERNAME", fullName); // update the header display name
        }
        return "redirect:/";
    }

    @PostMapping("/profile/avatar")
    public String updateAvatar(
            @RequestParam("avatar") String avatarBase64,
            HttpSession session) {
        String token = (String) session.getAttribute("JWT_TOKEN");
        if (token == null) {
            return "redirect:/login";
        }
        boolean success = userService.updateAvatar(avatarBase64);
        if (success) {
            session.setAttribute("USER_AVATAR", avatarBase64);
        }
        return "redirect:/";
    }
}
