package com.example.craft_beer_app.service;

import com.example.craft_beer_app.model.SalesRecord;
import com.example.craft_beer_app.repository.SalesRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public void deleteSalesRecord(Long id) {
        salesRecordRepository.deleteById(id);
    }

    public List<SalesRecord> getSalesRecordsByDateRange(LocalDate startDate, LocalDate endDate) {
        return salesRecordRepository.findByDateBetween(startDate, endDate);
    }

    public List<SalesRecord> getSalesRecordsByBeerId(Long beerId) {
        return salesRecordRepository.findByBeerId(beerId);
    }
}