package com.example.craft_beer_app.service;

import com.example.craft_beer_app.model.Beer;
import com.example.craft_beer_app.model.SalesRecord;
import com.example.craft_beer_app.repository.SalesRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SalesService {
    
    @Autowired
    private SalesRecordRepository salesRecordRepository;
    
    
    /**
     * 指定した日付の販売実績データを取得するメソッド
     * @param date 取得したい日付
     * @return ビール名と販売本数のマップ
     */
    public Map<String, Integer> getSalesByDate(LocalDate date) {
        // 指定日付の販売記録を取得
        List<SalesRecord> records;
        try {
            records = salesRecordRepository.findByDate(date);
        } catch (Exception e) {
            // データベースエラーなどの場合は空のマップを返す
            System.err.println("販売データ取得中にエラーが発生しました: " + e.getMessage());
            return new HashMap<>();
        }
        
        // 記録がない場合は空のマップを返す
        if (records == null || records.isEmpty()) {
            return new HashMap<>();
        }
        
        // ビール名ごとに販売本数を集計
        Map<String, Integer> beerSales = new HashMap<>();
        for (SalesRecord record : records) {
            try {
                Beer beer = record.getBeer();
                if (beer != null) {
                    String beerName = beer.getName();
                    int quantity = record.getQuantity();
                    
                    // 既存のビールの場合は本数を加算
                    beerSales.put(beerName, beerSales.getOrDefault(beerName, 0) + quantity);
                }
            } catch (Exception e) {
                // 特定のレコード処理中のエラーはスキップして続行
                System.err.println("販売レコード処理中にエラーが発生しました: " + e.getMessage());
            }
        }
        
        return beerSales;
    }
    
    /**
     * 期間指定で販売実績データを取得するメソッド
     * @param startDate 期間開始日
     * @param endDate 期間終了日
     * @return 日付ごとのビール名と販売本数のマップ
     */
    public Map<LocalDate, Map<String, Integer>> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Map<String, Integer>> salesByDate = new HashMap<>();
        
        // 指定された期間の各日付について処理
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            Map<String, Integer> dailySales = getSalesByDate(current);
            salesByDate.put(current, dailySales);
            current = current.plusDays(1);
        }
        
        return salesByDate;
    }
}

