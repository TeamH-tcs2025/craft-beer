package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.model.DailySales;
import com.example.craft_beer_app.model.ForecastResponse;
import com.example.craft_beer_app.model.WeatherData;
import com.example.craft_beer_app.service.ForecastService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class ForecastController {

    @Autowired
    private ForecastService forecastService;

    @GetMapping("/forecast")
    public String forecast(Model model) {
        // 明日の日付を取得（予測対象日）
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        
        // 今日の天気データを取得（実際は気象APIから取得）
        WeatherData todayWeather = forecastService.getTodayWeather();
        
        // 過去14日間の販売データを取得（実際はDBから取得）
        List<DailySales> salesHistory = forecastService.generateSalesHistory(14);
        
        // 需要予測APIを呼び出し
        ForecastResponse forecastResponse = forecastService.getForecast(tomorrow, todayWeather, salesHistory);
        
        // 表示用にデータを整形
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
        String formattedForecastDate = tomorrow.format(displayFormatter);
        
        Map<String, String> recommendation = forecastResponse.toRecommendationMap();
        
        // モデルに値を設定
        model.addAttribute("forecastDate", formattedForecastDate);
        model.addAttribute("weather", todayWeather.toString());
        model.addAttribute("recommendation", recommendation);
        model.addAttribute("remainingDays", 3);  // 次回発注までの残日数
        model.addAttribute("recentSales", salesHistory);
        
        return "forecast";
    }
}