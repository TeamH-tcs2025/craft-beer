package com.example.craft_beer_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForecastResponse {
    
    @JsonProperty("ペールエール(本)")
    private String paleAle;
    
    @JsonProperty("ラガー(本)")
    private String lager;
    
    @JsonProperty("IPA(本)")
    private String ipa;
    
    @JsonProperty("ホワイトビール(本)")
    private String whiteBeer;
    
    @JsonProperty("黒ビール(本)")
    private String darkBeer;
    
    @JsonProperty("フルーツビール(本)")
    private String fruitBeer;
    
    @JsonProperty("総予測杯数")
    private String totalBottles;

    public ForecastResponse() {
    }

    public String getPaleAle() {
        return paleAle;
    }

    public void setPaleAle(String paleAle) {
        this.paleAle = paleAle;
    }

    public String getLager() {
        return lager;
    }

    public void setLager(String lager) {
        this.lager = lager;
    }

    public String getIpa() {
        return ipa;
    }

    public void setIpa(String ipa) {
        this.ipa = ipa;
    }

    public String getWhiteBeer() {
        return whiteBeer;
    }

    public void setWhiteBeer(String whiteBeer) {
        this.whiteBeer = whiteBeer;
    }

    public String getDarkBeer() {
        return darkBeer;
    }

    public void setDarkBeer(String darkBeer) {
        this.darkBeer = darkBeer;
    }

    public String getFruitBeer() {
        return fruitBeer;
    }

    public void setFruitBeer(String fruitBeer) {
        this.fruitBeer = fruitBeer;
    }

    public String getTotalBottles() {
        return totalBottles;
    }

    public void setTotalBottles(String totalBottles) {
        this.totalBottles = totalBottles;
    }

    // Thymeleaf表示用のマップに変換
    public Map<String, String> toRecommendationMap() {
        Map<String, String> map = new HashMap<>();
        map.put("ペールエール", extractQuantity(paleAle));
        map.put("ラガー", extractQuantity(lager));
        map.put("IPA", extractQuantity(ipa));
        map.put("ホワイトビール", extractQuantity(whiteBeer));
        map.put("黒ビール", extractQuantity(darkBeer));
        map.put("フルーツビール", extractQuantity(fruitBeer));
        map.put("合計", extractQuantity(totalBottles));
        return map;
    }

    // 「12本（予測値: 11.83）」から「12」を抽出
    private String extractQuantity(String rawValue) {
        if (rawValue == null || rawValue.isEmpty()) {
            return "0";
        }
        
        Pattern pattern = Pattern.compile("^(\\d+)本");
        Matcher matcher = pattern.matcher(rawValue);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "0";
    }
}