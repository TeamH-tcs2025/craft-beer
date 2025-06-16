package com.example.craft_beer_app.service;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    public String getTodayWeather() {
        return "☀️ 晴れ（仮）";
    }
}
