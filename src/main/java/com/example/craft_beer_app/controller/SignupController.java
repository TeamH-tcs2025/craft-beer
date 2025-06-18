package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String email,
                                @RequestParam String role,
                                HttpSession session,
                                Model model) {

        boolean success = userService.register(username, password, email, role);

        if (success) {
            session.setAttribute("username", username);
            session.setAttribute("role", role);

            if ("false".equals(role)) {
                return "redirect:/home";
            } else {
                return "redirect:/home";
            }
        } else {
            model.addAttribute("error", "このユーザー名は既に使われています。");
            return "signup";
        }
    }
}
