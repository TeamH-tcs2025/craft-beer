package com.example.craft_beer_app.repository;

import com.example.craft_beer_app.model.SalesRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long> {
    List<SalesRecord> findByDate(LocalDate date);
    List<SalesRecord> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<SalesRecord> findByBeerId(Long beerId);
}