package com.example.craft_beer_app.service;
import org.springframework.stereotype.Service;

@Service
public class SalesService {
    public String getYesterdaySales() {
        return "昨日の販売数：50杯（仮）";
    }
}

