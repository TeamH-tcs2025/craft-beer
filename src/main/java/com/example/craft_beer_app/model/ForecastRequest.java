package com.example.craft_beer_app.model;
import java.util.List;

public class ForecastRequest {
    private String date;
    private WeatherData weather;
    private List<DailySales> recent_sales;

    public ForecastRequest() {
    }

    public ForecastRequest(String date, WeatherData weather, List<DailySales> recent_sales) {
        this.date = date;
        this.weather = weather;
        this.recent_sales = recent_sales;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public WeatherData getWeather() {
        return weather;
    }

    public void setWeather(WeatherData weather) {
        this.weather = weather;
    }

    public List<DailySales> getRecent_sales() {
        return recent_sales;
    }

    public void setRecent_sales(List<DailySales> recent_sales) {
        this.recent_sales = recent_sales;
    }
}