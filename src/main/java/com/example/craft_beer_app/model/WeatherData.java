package com.example.craft_beer_app.model;

/**
 * 予測API用の天気データクラス
 */
public class WeatherData {
    private double temp; // 気温（℃）
    private double humidity; // 湿度（％）
    private double precipitation; // 降水量（mm）
    private double wind_speed; // 風速（m/s）
    
    public WeatherData() {
    }
    
    public WeatherData(double temp, double humidity, double precipitation, double windSpeed) {
        this.temp = temp;
        this.humidity = humidity;
        this.precipitation = precipitation;
        this.wind_speed = windSpeed;
    }
    
    // 追加：ゲッターメソッド
    public double getTemp() {
        return temp;
    }
    
    public void setTemp(double temp) {
        this.temp = temp;
    }
    
    public double getHumidity() {
        return humidity;
    }
    
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
    
    public double getPrecipitation() {
        return precipitation;
    }
    
    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }
    
    public double getWind_speed() {
        return wind_speed;
    }
    
    public void setWind_speed(double windSpeed) {
        this.wind_speed = windSpeed;
    }
    
    @Override
    public String toString() {
        return "WeatherData{" +
                "temp=" + temp +
                ", humidity=" + humidity +
                ", precipitation=" + precipitation +
                ", wind_speed=" + wind_speed +
                '}';
    }
}