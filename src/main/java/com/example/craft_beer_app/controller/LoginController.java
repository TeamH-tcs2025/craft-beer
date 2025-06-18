package com.example.craft_beer_app.controller;
 
import com.example.craft_beer_app.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
 
@Controller
public class LoginController {
 
    @Autowired
    private UserService userService;
 
    @GetMapping("/login")
    public String showForm() {
        return "login";
    }
 
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        if (userService.authenticate(email, password)) {
            session.setAttribute("email", email);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "ログイン失敗：ユーザー名またはパスワードが間違っています");
            return "login";
        }
    }

 
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}