package com.example.craft_beer_app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DailySales {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String date;

    @JsonProperty("ペールエール(本)")
    private Integer paleAle;

    @JsonProperty("ラガー(本)")
    private Integer lager;

    @JsonProperty("IPA(本)")
    private Integer ipa;

    @JsonProperty("ホワイトビール(本)")
    private Integer whiteBeer;

    @JsonProperty("黒ビール(本)")
    private Integer darkBeer;

    @JsonProperty("フルーツビール(本)")
    private Integer fruitBeer;

    // デフォルトコンストラクタ
    public DailySales() {
    }

    // 日付指定のコンストラクタ
    public DailySales(LocalDate date) {
        this.date = date.toString();
    }

    // 全データ指定のコンストラクタ
    public DailySales(LocalDate date, int paleAle, int lager, int ipa, int whiteBeer, int darkBeer, int fruitBeer) {
        this.date = date.toString();
        this.paleAle = paleAle;
        this.lager = lager;
        this.ipa = ipa;
        this.whiteBeer = whiteBeer;
        this.darkBeer = darkBeer;
        this.fruitBeer = fruitBeer;
    }

    // Getters & Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPaleAle() {
        return paleAle;
    }

    public void setPaleAle(Integer paleAle) {
        this.paleAle = paleAle;
    }

    public Integer getLager() {
        return lager;
    }

    public void setLager(Integer lager) {
        this.lager = lager;
    }

    public Integer getIpa() {
        return ipa;
    }

    public void setIpa(Integer ipa) {
        this.ipa = ipa;
    }

    public Integer getWhiteBeer() {
        return whiteBeer;
    }

    public void setWhiteBeer(Integer whiteBeer) {
        this.whiteBeer = whiteBeer;
    }

    public Integer getDarkBeer() {
        return darkBeer;
    }

    public void setDarkBeer(Integer darkBeer) {
        this.darkBeer = darkBeer;
    }

    public Integer getFruitBeer() {
        return fruitBeer;
    }

    public void setFruitBeer(Integer fruitBeer) {
        this.fruitBeer = fruitBeer;
    }

    // Thymeleaf用にビールタイプをキー、本数を値としたMapを返すメソッド
    public Map<String, Integer> getBeerSales() {
        Map<String, Integer> salesMap = new HashMap<>();
        salesMap.put("ペールエール", paleAle != null ? paleAle : 0);
        salesMap.put("ラガー", lager != null ? lager : 0);
        salesMap.put("IPA", ipa != null ? ipa : 0);
        salesMap.put("ホワイトビール", whiteBeer != null ? whiteBeer : 0);
        salesMap.put("黒ビール", darkBeer != null ? darkBeer : 0);
        salesMap.put("フルーツビール", fruitBeer != null ? fruitBeer : 0);
        return salesMap;
    }
    
    // 日付の表示用フォーマット（yyyy-MM-ddからMM/ddに変換）
    public String getFormattedDate() {
        if (date == null) return "";
        String[] parts = date.split("-");
        if (parts.length >= 3) {
            return parts[1] + "/" + parts[2];
        }
        return date;
    }
}