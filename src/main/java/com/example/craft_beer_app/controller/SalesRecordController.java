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
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;

@Controller
@RequestMapping("/sales-records")
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

    // 新規作成フォーム表示
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("beers", beerService.getAllBeers());
        return "sales-records/new";
    }

    // 新規作成処理
    @PostMapping
    public String create(@RequestParam("beerId") Long beerId,
                        @RequestParam("quantity") Integer quantity,
                        @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            SalesRecord salesRecord = new SalesRecord();
            Beer beer = beerService.getBeerById(beerId)
                    .orElseThrow(() -> new RuntimeException("Beer not found: " + beerId));
            
            salesRecord.setBeer(beer);
            salesRecord.setQuantity(quantity);
            salesRecord.setDate(date);
            salesRecord.setCreatedAt(LocalDateTime.now());
            salesRecord.setUpdatedAt(LocalDateTime.now());
            
            // デバッグ用ログ出力
            System.out.println("Saving record: " + salesRecord);
            
            salesRecordService.saveSalesRecord(salesRecord);
            return "redirect:/sales-records";
        } catch (Exception e) {
            // エラーログ出力
            e.printStackTrace();
            return "redirect:/sales-records/new?error=true";
        }
    }

    // 編集フォーム表示
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<SalesRecord> salesRecord = salesRecordService.getSalesRecordById(id);
        if (salesRecord.isPresent()) {
            model.addAttribute("salesRecord", salesRecord.get());
            model.addAttribute("beers", beerService.getAllBeers());
            return "sales-records/form";
        }
        return "redirect:/sales-records";
    }

    // 更新処理
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                        @RequestParam("beerId") Long beerId,
                        @RequestParam("quantity") Integer quantity,
                        @RequestParam("createdBy") Long createdBy) {
        Optional<SalesRecord> existingRecord = salesRecordService.getSalesRecordById(id);
        if (existingRecord.isPresent()) {
            SalesRecord salesRecord = existingRecord.get();
            salesRecord.setBeer(beerService.getBeerById(beerId).orElseThrow());
            salesRecord.setQuantity(quantity);
            salesRecord.setCreatedBy(createdBy);
            
            salesRecordService.saveSalesRecord(salesRecord);
        }
        return "redirect:/sales-records";
    }

    // 詳細表示
    @GetMapping("/{id}/detail")
    public String showDetail(@PathVariable Long id, Model model) {
        Optional<SalesRecord> salesRecord = salesRecordService.getSalesRecordById(id);
        if (salesRecord.isPresent()) {
            model.addAttribute("salesRecord", salesRecord.get());
            return "sales-records/detail";
        }
        return "redirect:/sales-records";
    }

    // APIエンドポイントを/apiパスの下に移動
    @GetMapping("/api/sales-records")
    @ResponseBody
    public List<SalesRecord> getAllSalesRecords() {
        return salesRecordService.getAllSalesRecords();
    }

    @DeleteMapping("/api/sales-records/{id}")
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salesRecordService.deleteSalesRecord(id);
        return ResponseEntity.ok().build();
    }
}
