// 管理者用

package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.model.Beer;
import com.example.craft_beer_app.model.SalesRecord;
import com.example.craft_beer_app.service.BeerService;
import com.example.craft_beer_app.service.SalesRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;

@Controller
@RequestMapping("/admin/sales-records")
public class SalesRecordController {
    private final SalesRecordService salesRecordService;
    private final BeerService beerService;

    @Autowired
    public SalesRecordController(SalesRecordService salesRecordService, BeerService beerService) {
        this.salesRecordService = salesRecordService;
        this.beerService = beerService;
    }

    // カレンダー表示
    @GetMapping
    public String showCalendar() {
        return "sales-records/list";
    }

    @GetMapping("/date/{date}")
    public String showDateDetail(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        List<SalesRecord> records = salesRecordService.getSalesRecordsByDate(date);
        model.addAttribute("date", date);
        model.addAttribute("salesRecords", records);
        model.addAttribute("beers", beerService.getAllBeers());
        return "sales-records/date-detail";
    }

    @GetMapping("/api/admin/sales-records")
    @ResponseBody
    public List<SalesRecord> getAllSalesRecords() {
        return salesRecordService.getAllSalesRecords();
    }

    @PostMapping("/api/admin/sales-records")
    @ResponseBody
    public SalesRecord createSalesRecord(@RequestBody Map<String, Object> payload) {
        SalesRecord salesRecord = new SalesRecord();
        salesRecord.setBeer(beerService.getBeerById(Long.parseLong(payload.get("beerId").toString())).orElseThrow());
        salesRecord.setQuantity(Integer.parseInt(payload.get("quantity").toString()));
        salesRecord.setCreatedBy(Long.parseLong(payload.get("createdBy").toString()));
        salesRecord.setDate(LocalDate.parse(payload.get("date").toString()));
        return salesRecordService.saveSalesRecord(salesRecord);
    }

    @DeleteMapping("/api/admin/sales-records/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteSalesRecord(@PathVariable Long id) {
        salesRecordService.deleteSalesRecord(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/admin/sales-records/{id}")
    @ResponseBody
    public SalesRecord updateSalesRecord(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Optional<SalesRecord> existingRecord = salesRecordService.getSalesRecordById(id);
        if (existingRecord.isPresent()) {
            SalesRecord salesRecord = existingRecord.get();
            salesRecord.setBeer(beerService.getBeerById(Long.parseLong(payload.get("beerId").toString())).orElseThrow());
            salesRecord.setQuantity(Integer.parseInt(payload.get("quantity").toString()));
            salesRecord.setCreatedBy(Long.parseLong(payload.get("createdBy").toString()));
            salesRecord.setDate(LocalDate.parse(payload.get("date").toString()));
            return salesRecordService.saveSalesRecord(salesRecord);
        } else {
            throw new RuntimeException("Sales record not found");
        }
    }
}
