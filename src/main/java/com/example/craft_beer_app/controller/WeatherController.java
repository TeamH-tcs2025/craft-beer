package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.model.Weather;
import com.example.craft_beer_app.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * 天気予報表示ページ
     */
    @GetMapping
    public String showWeatherForecast(Model model) {
        List<Weather> forecast = weatherService.getForecast();
        model.addAttribute("forecast", forecast);
        return "weather/forecast";
    }

    /**
     * REST API: 天気予報データを取得
     */
    @GetMapping("/api/forecast")
    @ResponseBody
    public ResponseEntity<List<Weather>> getWeatherForecast() {
        List<Weather> forecast = weatherService.getForecast();
        return ResponseEntity.ok(forecast);
    }

    /**
     * 特定の日の天気データを取得
     */
    @GetMapping("/api/forecast/{date}")
    @ResponseBody
    public ResponseEntity<Weather> getWeatherByDate(@PathVariable String date) {
        Weather weather = weatherService.getWeatherByDate(date);
        if (weather != null) {
            return ResponseEntity.ok(weather);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}