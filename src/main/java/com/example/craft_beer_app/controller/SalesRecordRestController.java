package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.model.SalesRecord;
import com.example.craft_beer_app.service.SalesRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales-records")
public class SalesRecordRestController {

    private final SalesRecordService salesRecordService;

    @Autowired
    public SalesRecordRestController(SalesRecordService salesRecordService) {
        this.salesRecordService = salesRecordService;
    }

    @GetMapping
    public ResponseEntity<List<SalesRecord>> getAllSalesRecords() {
        return ResponseEntity.ok(salesRecordService.getAllSalesRecords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesRecord> getSalesRecordById(@PathVariable Long id) {
        Optional<SalesRecord> salesRecord = salesRecordService.getSalesRecordById(id);
        return salesRecord.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SalesRecord> createSalesRecord(@RequestBody SalesRecord salesRecord) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(salesRecordService.saveSalesRecord(salesRecord));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesRecord> updateSalesRecord(@PathVariable Long id, @RequestBody SalesRecord salesRecord) {
        if (!salesRecordService.getSalesRecordById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        salesRecord.setId(id);
        return ResponseEntity.ok(salesRecordService.saveSalesRecord(salesRecord));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesRecord(@PathVariable Long id) {
        if (!salesRecordService.getSalesRecordById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        salesRecordService.deleteSalesRecord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<SalesRecord>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(salesRecordService.getSalesRecordsByDateRange(startDate, endDate));
    }
}