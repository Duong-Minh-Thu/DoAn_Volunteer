package com.night.frontend_.controller;

import com.night.frontend_.service.RegistrationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public String listRegistrations(HttpSession session, Model model) {
        if (session.getAttribute("JWT_TOKEN") == null) {
            return "redirect:/login";
        }
        
        String role = (String) session.getAttribute("USER_ROLE");
        if ("STUDENT".equals(role)) {
            model.addAttribute("registrations", registrationService.getMyRegistrations());
        } else {
            model.addAttribute("registrations", java.util.List.of());
        }
        return "registrations/list";
    }

    @GetMapping("/{id}")
    public String registrationDetail(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("JWT_TOKEN") == null) {
            return "redirect:/login";
        }
        model.addAttribute("registration", registrationService.getRegistrationById(id));
        return "registrations/detail";
    }
}
