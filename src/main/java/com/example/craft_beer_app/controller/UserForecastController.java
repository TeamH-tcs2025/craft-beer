package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.model.DailySales;
import com.example.craft_beer_app.model.Weather;
import com.example.craft_beer_app.service.ForecastService;
import com.example.craft_beer_app.service.WeatherService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Collectors;

@Controller
public class UserForecastController {

    private static final Logger logger = LoggerFactory.getLogger(UserForecastController.class);

    @Autowired
    private ForecastService forecastService;
    
    @Autowired
    private WeatherService weatherService;

    @GetMapping("/user/forecast")
    public String userForecast(Model model) {
        logger.info("User forecast page requested");
        
        try {
            // 今日の日付と曜日を取得
            LocalDate today = LocalDate.now();
            DayOfWeek dayOfWeek = today.getDayOfWeek();
            
            // 予測期間を計算（今日から3日間、日曜日を除く）
            List<LocalDate> forecastDates = new ArrayList<>();
            LocalDate currentDate = today.plusDays(1); // 明日から
            
            // 最大3日分（ただし日曜日を除く）の予測日を取得
            while (forecastDates.size() < 3) {
                if (currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    forecastDates.add(currentDate);
                }
                currentDate = currentDate.plusDays(1);
            }
            
            // 開始日と終了日を設定
            LocalDate startDate = forecastDates.get(0);
            LocalDate endDate = forecastDates.get(forecastDates.size() - 1);
            
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
            
            // 表示用に日付を整形
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("M/d(E)");
            List<String> formattedDates = forecastDates.stream()
                .map(date -> date.format(displayFormatter))
                .collect(Collectors.toList());
            
            // 明日の予測結果（詳細表示用）
            Map<String, Integer> tomorrowForecast = forecastService.getRecommendedOrder();
            
            // 翌3日間の予測結果に基づく推奨発注数量
            Map<String, Integer> orderQuantity = forecastService.getRecommendedOrderQuantity();
            
            // 発注日かどうか
            boolean isOrderDay = (dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.THURSDAY);
            
            // モデルに値を設定
            model.addAttribute("today", today.format(DateTimeFormatter.ofPattern("yyyy年M月d日(E)")));
            model.addAttribute("startDate", startDate.format(DateTimeFormatter.ofPattern("M/d")));
            model.addAttribute("endDate", endDate.format(DateTimeFormatter.ofPattern("M/d")));
            model.addAttribute("forecastDates", formattedDates);
            model.addAttribute("weather", weatherInfo);
            model.addAttribute("dailyForecast", tomorrowForecast);
            model.addAttribute("orderQuantity", orderQuantity);
            model.addAttribute("isOrderDay", isOrderDay);
            
            logger.info("User forecast page rendered successfully with {} forecast dates", forecastDates.size());
            
        } catch (Exception e) {
            logger.error("Error preparing user forecast page", e);
            // エラーが発生しても最低限の情報でページを表示
            model.addAttribute("error", "データの取得中にエラーが発生しました。");
        }
        
        return "userforecast";
    }
}