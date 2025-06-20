package com.example.craft_beer_app.service;

import com.example.craft_beer_app.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {
    
    private static final String WEATHER_API_URL = "https://beer-forecast-api.azurewebsites.net/api/getweather";
    
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 東京の今後5日間の天気予報を取得する
     * @return 天気予報のリスト
     */
    public List<Weather> getForecast() {
        ResponseEntity<List<Weather>> response = restTemplate.exchange(
                WEATHER_API_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Weather>>() {}
        );
        return response.getBody();
    }
   
    /**
     * 今日の天気データを取得する
     * @return 今日の天気情報を含む Weather オブジェクト
     */
    public Weather getTodayWeatherData() {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE); // YYYY-MM-DD形式
        return getWeatherByDate(today);
    }
    
    /**
     * 特定の日の天気予報データを取得する
     * @param date 日付 (YYYY-MM-DD形式)
     * @return その日の天気予報、見つからない場合はnull
     */
    public Weather getWeatherByDate(String date) {
        List<Weather> forecast = getForecast();
        for (Weather weather : forecast) {
            if (weather.getDate().equals(date)) {
                return weather;
            }
        }
        return null;
    }
}