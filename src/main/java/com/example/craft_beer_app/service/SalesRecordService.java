package com.example.craft_beer_app.service;

import com.example.craft_beer_app.model.SalesRecord;
import com.example.craft_beer_app.repository.SalesRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.craft_beer_app.model.Beer;
import com.example.craft_beer_app.model.DailySales;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.example.craft_beer_app.util.BeerTypeUtil;

@Service
public class SalesRecordService {

    private final SalesRecordRepository salesRecordRepository;

    @Autowired
    public SalesRecordService(SalesRecordRepository salesRecordRepository) {
        this.salesRecordRepository = salesRecordRepository;
    }

    public List<SalesRecord> getAllSalesRecords() {
        return salesRecordRepository.findAll();
    }

    public Optional<SalesRecord> getSalesRecordById(Long id) {
        return salesRecordRepository.findById(id);
    }

    public SalesRecord saveSalesRecord(SalesRecord salesRecord) {
        return salesRecordRepository.save(salesRecord);
    }

    public boolean deleteSalesRecord(Long id) {
        if (salesRecordRepository.existsById(id)) {
            salesRecordRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<SalesRecord> getSalesRecordsByDateRange(LocalDate startDate, LocalDate endDate) {
        return salesRecordRepository.findByDateBetween(startDate, endDate);
    }

    public List<SalesRecord> getSalesRecordsByBeerId(Long beerId) {
        return salesRecordRepository.findByBeerId(beerId);
    }

    public List<SalesRecord> getSalesRecordsByDate(LocalDate date) {
        return salesRecordRepository.findByDate(date);
    }

    /**
     * 指定した日数分の過去の販売実績を日付ごとにまとめて取得する
     * @param days 取得する過去の日数
     * @return 日付ごとにまとめた販売実績のリスト
     */
    public List<DailySales> getSalesHistoryForLastDays(int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
    
    // 指定期間内の全販売記録を取得
    List<SalesRecord> records = salesRecordRepository.findByDateBetween(startDate, endDate);
    
    // 日付ごとのマップを初期化
    Map<LocalDate, DailySales> dailySalesMap = new HashMap<>();
    
    // 期間内の各日付について、空の販売実績オブジェクトで初期化
    for (int i = 0; i <= days; i++) {
        LocalDate date = endDate.minusDays(i);
        dailySalesMap.put(date, new DailySales(date));
    }
    
    // 販売記録を日付ごとにグループ化して集計
    Map<LocalDate, Map<String, Integer>> salesByDateAndType = new HashMap<>();
    
    for (SalesRecord record : records) {
        LocalDate recordDate = record.getDate();
        
        // ビール名からタイプを判定
        String beerType = BeerTypeUtil.getBeerTypeFromName(record.getBeer().getName());
        Integer quantity = record.getQuantity();
        
        // 日付のマップがなければ作成
        salesByDateAndType.putIfAbsent(recordDate, new HashMap<>());
        
        // ビールタイプの販売数を加算
        Map<String, Integer> typeSales = salesByDateAndType.get(recordDate);
        typeSales.put(beerType, typeSales.getOrDefault(beerType, 0) + quantity);
    }
    
    // 集計結果をDailySalesオブジェクトに変換
    for (Map.Entry<LocalDate, Map<String, Integer>> entry : salesByDateAndType.entrySet()) {
        LocalDate date = entry.getKey();
        Map<String, Integer> typeSales = entry.getValue();
        
        if (dailySalesMap.containsKey(date)) {
            DailySales dailySales = dailySalesMap.get(date);
            
            // 各ビールタイプの販売数を設定
            dailySales.setPaleAle(typeSales.getOrDefault("ペールエール", 0));
            dailySales.setLager(typeSales.getOrDefault("ラガー", 0));
            dailySales.setIpa(typeSales.getOrDefault("IPA", 0));
            dailySales.setWhiteBeer(typeSales.getOrDefault("ホワイトビール", 0));
            dailySales.setDarkBeer(typeSales.getOrDefault("黒ビール", 0));
            dailySales.setFruitBeer(typeSales.getOrDefault("フルーツビール", 0));
        }
    }
    
    // 日付順にソートしたリストを返す
    return dailySalesMap.values().stream()
            .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
            .collect(Collectors.toList());
}
}