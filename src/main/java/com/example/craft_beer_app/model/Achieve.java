package com.example.craft_beer_app.model;

import java.util.Map;

public class Achieve {
    private String weather;
    private Map<String, Integer> salesresult;

    public Achieve() {
    }

    public Achieve(String weather, Map<String, Integer> salesresult) {
        this.weather = weather;
        this.salesresult = salesresult;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Map<String, Integer> getsalesresult() {
        return salesresult;
    }

    public void setsalesresult(Map<String, Integer> salesresult) {
        this.salesresult = salesresult;
    }
}
