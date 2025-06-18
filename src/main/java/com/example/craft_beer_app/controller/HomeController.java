package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.service.WeatherService;
import com.example.craft_beer_app.model.User;
import com.example.craft_beer_app.repository.UserRepository;
import com.example.craft_beer_app.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private WeatherService weatherService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SalesService salesService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {

        String email = (String) session.getAttribute("email");
        if (email == null)
            return "redirect:/login";

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "redirect:/login"; // ユーザーが見つからない場合はログインページへリダイレクト
        }


        model.addAttribute("username", user.getUsername());
        model.addAttribute("weather", weatherService.getTodayWeather());
        model.addAttribute("sales", salesService.getYesterdaySales());

        return "home";
    }
}
