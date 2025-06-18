package com.example.craft_beer_app.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AchieveService {

    public Map<String, Integer> getsalesresult() {
        Map<String, Integer> salesresult = new HashMap<>();
        salesresult.put("ビールA", 20);
        salesresult.put("ビールB", 15);
        return salesresult;
    }
}