package com.example.craft_beer_app.controller;
//一般ユーザー
import com.example.craft_beer_app.service.AchieveService;
import com.example.craft_beer_app.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.craft_beer_app.model.Weather;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
public class UserAchieveController {

    @Autowired
    private AchieveService achieveService;

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/user/achieve")
    public String showAchieve(Model model) {
        Map<String, Integer> salesresult = achieveService.getsalesresult();

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        model.addAttribute("today", today);
        model.addAttribute("salesresult", salesresult);
        
        // 天気データ
        Weather todayWeather = weatherService.getTodayWeatherData();
        model.addAttribute("todayWeather", todayWeather);
        
        return "userachieve";
    }
}
