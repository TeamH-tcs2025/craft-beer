package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.model.DailySales;
import com.example.craft_beer_app.model.Weather;
import com.example.craft_beer_app.service.ForecastService;
import com.example.craft_beer_app.service.SalesRecordService;
import com.example.craft_beer_app.service.WeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class UserForecastController {

    @Autowired
    private ForecastService forecastService;
    
    @Autowired
    private WeatherService weatherService;
    
    @Autowired
    private SalesRecordService salesRecordService;

    @GetMapping("/user/forecast")
    public String showForecast(Model model) {
        // 明日の日付（予測対象日）
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        
        // 今日の天気
        Weather weather = weatherService.getTodayWeatherData();
        String weatherInfo = weather != null ? 
                String.format("%s 気温: %.1f°C 湿度: %.0f%% 風速: %.1fm/s", 
                        weather.getWeather(), 
                        weather.getTemperature(), 
                        weather.getHumidity(), 
                        weather.getWindSpeed()) : 
                "天気情報が取得できませんでした";
        
        // 過去14日間の販売履歴
        List<DailySales> salesHistory = salesRecordService.getSalesHistoryForLastDays(14);
        
        // 予測結果
        Map<String, Integer> recommendation = forecastService.getRecommendedOrder();
        
        // 表示用フォーマット
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
        String formattedForecastDate = tomorrow.format(displayFormatter);
        
        // モデルにデータをセット
        model.addAttribute("forecastDate", formattedForecastDate);
        model.addAttribute("weather", weatherInfo);
        model.addAttribute("recommendation", recommendation);
        model.addAttribute("remainingDays", 3);
        model.addAttribute("recentSales", salesHistory);
        
        return "forecast";
    }
}