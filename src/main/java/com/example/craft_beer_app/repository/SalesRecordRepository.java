package com.example.craft_beer_app.repository;

import com.example.craft_beer_app.model.SalesRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long> {
    List<SalesRecord> findByDate(LocalDate date);
    
    // 追加: 日付範囲クエリメソッド
    List<SalesRecord> findByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);
    
    // 追加: 日付範囲クエリメソッド (findByDateBetween も使えるようにする)
    List<SalesRecord> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    // 追加: ビールIDによる検索メソッド
    List<SalesRecord> findByBeerId(Long beerId);
}