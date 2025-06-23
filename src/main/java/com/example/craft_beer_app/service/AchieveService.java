package com.example.craft_beer_app.service;

import com.example.craft_beer_app.model.SalesRecord;
import com.example.craft_beer_app.service.SalesRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AchieveService {

    @Autowired
    private SalesRecordService salesRecordService;

    public Map<String, Integer> getSalesResult() {
        LocalDate today = LocalDate.now();
        List<SalesRecord> todayRecords = salesRecordService.getSalesRecordsByDate(today);

        Map<String, Integer> salesResult = new HashMap<>();
        for (SalesRecord record : todayRecords) {
            String beerName = record.getBeer().getName();
            int quantity = record.getQuantity();
            salesResult.put(beerName, salesResult.getOrDefault(beerName, 0) + quantity);
        }

        return salesResult;
    }
}
