package com.example.craft_beer_app.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ForecastService {

    /**
    //   ビールごとの推奨発注数量を返す（仮データ）
     */
    public Map<String, Integer> getRecommendedOrder() {
        Map<String, Integer> recommendation = new HashMap<>();
        recommendation.put("ビールA", 20);
        recommendation.put("ビールB", 15);
        return recommendation;
    }
}
