package com.night.frontend_.controller;

import com.night.frontend_.service.RegistrationService;
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
    public String listRegistrations(Model model) {
        model.addAttribute("registrations", registrationService.getAllRegistrations());
        return "registrations/list";
    }

    @GetMapping("/{id}")
    public String registrationDetail(@PathVariable Long id, Model model) {
        model.addAttribute("registration", registrationService.getRegistrationById(id));
        return "registrations/detail";
    }
}
