package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.service.UserService;
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
        return "signup"; // signup.html を表示
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String email,
                                @RequestParam String role,
                                Model model) {
        boolean success = userService.register(username, password, email, role);
        if (success) {
            return "redirect:/login"; // 登録成功後はログインページへリダイレクト
        } else {
            model.addAttribute("error", "このユーザー名は既に使われています。");
            return "signup";
        }
    }
}
