package com.night.frontend_.controller;

import com.night.frontend_.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";
    }

    @GetMapping("/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "users/detail";
    }

    @org.springframework.web.bind.annotation.PostMapping("/{id}/update")
    public String updateUser(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestParam("fullName") String fullName,
            @org.springframework.web.bind.annotation.RequestParam("email") String email,
            @org.springframework.web.bind.annotation.RequestParam("role") String role) {
        userService.updateUser(id, fullName, email, role);
        return "redirect:/users/" + id;
    }
}
