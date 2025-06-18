package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.service.WeatherService;
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
    private SalesService salesService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String user = (String) session.getAttribute("email");
        if (user == null) return "redirect:/login";

        model.addAttribute("username", user);
        model.addAttribute("weather", weatherService.getTodayWeather());
        model.addAttribute("sales", salesService.getYesterdaySales());

        return "home";
    }
}
