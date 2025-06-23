package com.example.craft_beer_app.repository;

import com.example.craft_beer_app.model.SalesRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long> {
    
    /**
     * 特定の日付の販売記録を取得するメソッド
     * @param date 日付
     * @return その日付の販売記録リスト
     */
    List<SalesRecord> findByDate(LocalDate date);
    
    /**
     * 特定の期間の販売記録を取得するメソッド
     * @param startDate 開始日
     * @param endDate 終了日
     * @return 期間内の販売記録リスト
     */
    List<SalesRecord> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * ビールIDで販売記録を検索するメソッド
     * @param beerId ビールID
     * @return 該当ビールの販売記録リスト
     */
    List<SalesRecord> findByBeerId(Long beerId);
}