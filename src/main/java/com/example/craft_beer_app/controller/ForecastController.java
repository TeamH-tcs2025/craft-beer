package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.model.DailySales;
import com.example.craft_beer_app.model.Weather;
import com.example.craft_beer_app.model.WeatherData;
import com.example.craft_beer_app.service.ForecastService;
import com.example.craft_beer_app.service.SalesRecordService;
import com.example.craft_beer_app.service.WeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ForecastController {

    @Autowired
    private ForecastService forecastService;
    
    @Autowired
    private SalesRecordService salesRecordService;
    
    @Autowired
    private WeatherService weatherService;

    /**
     * ルートパスの /forecast へのアクセスを /admin/forecast にリダイレクト
     */
    @GetMapping("/forecast")
    public String redirectToAdminForecast() {
        return "redirect:/admin/forecast";
    }

    /**
     * 管理者向け予測画面
     */
    @GetMapping("/admin/forecast")
    public String adminForecast(Model model) {
        // 今日の日付と曜日を取得
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        
        // 明日から3日間の予測期間を計算
        LocalDate startDate = today.plusDays(1);
        LocalDate endDate = today.plusDays(3);
        
        // 今日の天気データを取得
        Weather todayWeather = weatherService.getTodayWeatherData();
        String weatherInfo;

        if (todayWeather != null) {
            // 天候表現を表示せず、気温・湿度・風速のみを表示
            double temp = todayWeather.getTemperature() != null ? todayWeather.getTemperature() : 0.0;
            double humidity = todayWeather.getHumidity() != null ? todayWeather.getHumidity() : 0.0;
            double windSpeed = todayWeather.getWindSpeed() != null ? todayWeather.getWindSpeed() : 0.0;
            
            weatherInfo = String.format("気温: %.1f°C 湿度: %.0f%% 風速: %.1fm/s", 
                    temp, humidity, windSpeed);
        } else {
            weatherInfo = "天気情報が取得できませんでした";
        }
        
        // 過去14日間の販売データをDBから取得
        List<DailySales> salesHistory = salesRecordService.getSalesHistoryForLastDays(14);
        
        // 明日の予測結果（グラフ表示用）
        Map<String, Integer> tomorrowForecast = forecastService.getRecommendedOrder();
        
        // 翌3日間の予測結果に基づく推奨発注数量
        Map<String, Integer> orderQuantity = forecastService.getRecommendedOrderQuantity();
        
        // 表示用に日付を整形
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("M/d(E)");
        List<String> forecastDates = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            LocalDate date = today.plusDays(i);
            forecastDates.add(date.format(displayFormatter));
        }
        
        // モデルに値を設定
        model.addAttribute("today", today.format(DateTimeFormatter.ofPattern("yyyy年M月d日(E)")));
        model.addAttribute("startDate", startDate.format(DateTimeFormatter.ofPattern("M/d")));
        model.addAttribute("endDate", endDate.format(DateTimeFormatter.ofPattern("M/d")));
        model.addAttribute("forecastDates", forecastDates);
        model.addAttribute("weather", weatherInfo);
        model.addAttribute("dailyForecast", tomorrowForecast); // 明日の予測
        model.addAttribute("orderQuantity", orderQuantity); // 推奨発注数量
        model.addAttribute("recentSales", salesHistory);
        model.addAttribute("isOrderDay", dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.THURSDAY);
        
        return "forecast";
    }
}