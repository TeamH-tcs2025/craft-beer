package com.example.craft_beer_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherData {
    @JsonProperty("最高気温(℃)")
    private double maxTemperature;
    
    @JsonProperty("平均湿度(％)")
    private double avgHumidity;
    
    @JsonProperty("降水量の合計(mm)")
    private double totalRainfall;
    
    @JsonProperty("最大風速(m/s)")
    private double maxWindSpeed;

    public WeatherData() {
    }

    public WeatherData(double maxTemperature, double avgHumidity, double totalRainfall, double maxWindSpeed) {
        this.maxTemperature = maxTemperature;
        this.avgHumidity = avgHumidity;
        this.totalRainfall = totalRainfall;
        this.maxWindSpeed = maxWindSpeed;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getAvgHumidity() {
        return avgHumidity;
    }

    public void setAvgHumidity(double avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    public double getTotalRainfall() {
        return totalRainfall;
    }

    public void setTotalRainfall(double totalRainfall) {
        this.totalRainfall = totalRainfall;
    }

    public double getMaxWindSpeed() {
        return maxWindSpeed;
    }

    public void setMaxWindSpeed(double maxWindSpeed) {
        this.maxWindSpeed = maxWindSpeed;
    }

    @Override
    public String toString() {
        return String.format("最高気温: %.1f℃, 平均湿度: %.1f%%, 降水量: %.1fmm, 最大風速: %.1fm/s", 
                maxTemperature, avgHumidity, totalRainfall, maxWindSpeed);
    }
}