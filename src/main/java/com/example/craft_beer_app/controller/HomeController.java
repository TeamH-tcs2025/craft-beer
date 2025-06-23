package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.model.User;
import com.example.craft_beer_app.repository.UserRepository;
import com.example.craft_beer_app.service.SalesService;
import com.example.craft_beer_app.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private WeatherService weatherService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SalesService salesService;
    
    @GetMapping({"/", "/home"})
    public String redirectHome() {
        return "redirect:/admin/home";
    }

    @GetMapping("/admin/home")
    public String adminHome(HttpSession session, Model model) {
        // ユーザー情報の取得と検証
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", user.getUsername());
               
        // 天気データの追加（WeatherServiceがあれば）
        try {
            model.addAttribute("todayWeather", weatherService.getTodayWeatherData());
        } catch (Exception e) {
            // 天気データ取得に失敗しても続行
            System.err.println("天気データ取得中にエラーが発生しました: " + e.getMessage());
        }
        
        return "home";
    }

    @GetMapping("/user/home")
    public String userHome(HttpSession session, Model model) {
        // ユーザー情報の取得と検証
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", user.getUsername());
        
        // 天気データの追加（WeatherServiceがあれば）
        try {
            model.addAttribute("todayWeather", weatherService.getTodayWeatherData());
        } catch (Exception e) {
            // 天気データ取得に失敗しても続行
            System.err.println("天気データ取得中にエラーが発生しました: " + e.getMessage());
        }
        
        return "adhome";
    }
}