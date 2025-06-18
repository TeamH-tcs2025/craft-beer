package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.service.AchieveService;
import com.example.craft_beer_app.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
public class AchieveController {

    @Autowired
    private AchieveService achieveService;

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/achieve")
    public String showAchieve(Model model) {
        String weather = weatherService.getTodayWeather();
        Map<String, Integer> salesresult = achieveService.getsalesresult();

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        model.addAttribute("today", today);
       
        model.addAttribute("weather", weather);
        model.addAttribute("salesresult", salesresult);

        return "achieve";
    }
}
