package com.example.craft_beer_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Weather {
    private String date;
    
    @JsonProperty("temperature_high")
    private Double temperatureHigh;
    
    @JsonProperty("temperature_low")
    private Double temperatureLow;
    
    @JsonProperty("feels_like")
    private Double feelsLike;
    
    private Double precipitation;
    
    @JsonProperty("wind_speed")
    private Double windSpeed;
    
    private Double humidity;

    // デフォルトコンストラクタ
    public Weather() {
    }
    
    // getters and setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTemperatureHigh() {
        return temperatureHigh;
    }

    public void setTemperatureHigh(Double temperatureHigh) {
        this.temperatureHigh = temperatureHigh;
    }

    public Double getTemperatureLow() {
        return temperatureLow;
    }

    public void setTemperatureLow(Double temperatureLow) {
        this.temperatureLow = temperatureLow;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "date='" + date + '\'' +
                ", temperatureHigh=" + temperatureHigh +
                ", temperatureLow=" + temperatureLow +
                ", feelsLike=" + feelsLike +
                ", precipitation=" + precipitation +
                ", windSpeed=" + windSpeed +
                ", humidity=" + humidity +
                '}';
    }
}