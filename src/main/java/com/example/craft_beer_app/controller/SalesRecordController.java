package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.dto.SalesRecordDto;
import com.example.craft_beer_app.model.Beer;
import com.example.craft_beer_app.model.SalesRecord;
import com.example.craft_beer_app.service.BeerService;
import com.example.craft_beer_app.service.SalesRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @GetMapping
    public String getAllSalesRecords(Model model) {
        model.addAttribute("salesRecords", salesRecordService.getAllSalesRecords());
        return "sales-records/list";
    }

    @GetMapping("/new")
    public String showNewSalesRecordForm(Model model) {
        model.addAttribute("salesRecord", new SalesRecordDto());
        model.addAttribute("beers", beerService.getAllActiveBeers());
        return "sales-records/form";
    }

    @PostMapping
    public String saveSalesRecord(@ModelAttribute SalesRecordDto salesRecordDto) {
        SalesRecord salesRecord = new SalesRecord();
        salesRecord.setId(salesRecordDto.getId());
        salesRecord.setDate(salesRecordDto.getDate());
        salesRecord.setQuantity(salesRecordDto.getQuantity());
        salesRecord.setCreatedBy(salesRecordDto.getCreatedBy());
        
        Optional<Beer> beer = beerService.getBeerById(salesRecordDto.getBeerId());
        beer.ifPresent(salesRecord::setBeer);
        
        salesRecordService.saveSalesRecord(salesRecord);
        return "redirect:/sales-records";
    }

    @GetMapping("/{id}/edit")
    public String showEditSalesRecordForm(@PathVariable Long id, Model model) {
        Optional<SalesRecord> salesRecordOpt = salesRecordService.getSalesRecordById(id);
        if (salesRecordOpt.isPresent()) {
            SalesRecord salesRecord = salesRecordOpt.get();
            SalesRecordDto dto = new SalesRecordDto();
            dto.setId(salesRecord.getId());
            dto.setDate(salesRecord.getDate());
            dto.setQuantity(salesRecord.getQuantity());
            dto.setBeerId(salesRecord.getBeer().getId());
            dto.setCreatedBy(salesRecord.getCreatedBy());
            
            model.addAttribute("salesRecord", dto);
            model.addAttribute("beers", beerService.getAllActiveBeers());
            return "sales-records/form";
        }
        return "redirect:/sales-records";
    }

    @GetMapping("/{id}/delete")
    public String deleteSalesRecord(@PathVariable Long id) {
        salesRecordService.deleteSalesRecord(id);
        return "redirect:/sales-records";
    }
}
