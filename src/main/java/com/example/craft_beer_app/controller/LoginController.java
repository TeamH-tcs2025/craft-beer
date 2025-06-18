package com.example.craft_beer_app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.craft_beer_app.service.UserService;
import com.example.craft_beer_app.model.User; // または UserEntity




@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; 
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userService.findByEmailAndPassword(email, password);

        if (user == null) {
            model.addAttribute("error", "ユーザー名またはパスワードが間違っています。");
            return "login";
        }

        // セッション保存（ログイン状態）
        session.setAttribute("email", email);
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());

        if ("false".equals(user.getRole())) {
            return "redirect:/home";
        } else {
            return "redirect:/home";
        }
    }
}
