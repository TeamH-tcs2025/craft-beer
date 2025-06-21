package com.example.craft_beer_app.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForecastRequest {
    private String date;
    
    @JsonProperty("weather")
    private Map<String, Double> weather;
    
    @JsonProperty("recent_sales")
    private List<Map<String, Object>> recentSales;
    
    public ForecastRequest() {
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public Map<String, Double> getWeather() {
        return weather;
    }
    
    public void setWeather(Map<String, Double> weather) {
        this.weather = weather;
    }
    
    public List<Map<String, Object>> getRecentSales() {
        return recentSales;
    }
    
    public void setRecentSales(List<Map<String, Object>> recentSales) {
        this.recentSales = recentSales;
    }
}