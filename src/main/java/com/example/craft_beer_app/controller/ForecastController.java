package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.service.ForecastService;
import com.example.craft_beer_app.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.craft_beer_app.model.Weather;

import java.util.Map;

@Controller
public class ForecastController {

    @Autowired
    private ForecastService forecastService;

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/forecast")
    public String showForecast(Model model) {
        Weather weather = weatherService.getTodayWeatherData();
        Map<String, Integer> recommendation = forecastService.getRecommendedOrder();
        // 天気情報とビールの推奨発注数量をモデルに追加
        model.addAttribute("weather", weather);
        model.addAttribute("recommendation", recommendation);

        return "forecast";
    }
}
// このクラスは、天気予報とビールの推奨発注数量を表示するコントローラーです。
// `ForecastService` と `WeatherService` を使用して、天気情報とビ