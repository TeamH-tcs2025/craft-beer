package com.example.craft_beer_app.controller;
 
import com.example.craft_beer_app.model.Beer;
import com.example.craft_beer_app.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
 
@Controller
@RequestMapping("/admin/beers")
public class AdminBeerController {
 
    @Autowired
    private BeerService beerService;
 
    // 一覧表示
    @GetMapping
    public String listBeers(Model model) {
        model.addAttribute("beers", beerService.getAllActiveBeers());
        return "adBeers";
    }
 
    // 追加フォーム表示
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("beer", new Beer());
        return "adBeer-form";
    }
 
    // 追加処理
    @PostMapping("/add")
    public String addBeer(@ModelAttribute Beer beer) {
        beer.setIsActive(true); // 追加時に必ずtrueをセット
        beerService.saveBeer(beer);
        return "redirect:/admin/beers";
    }
 
    // 編集フォーム表示
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Beer beer = beerService.getBeerById(id).orElseThrow();
        model.addAttribute("beer", beer);
        return "adBeer-form";
    }
 
    // 編集処理
    @PostMapping("/edit")
    public String editBeer(@ModelAttribute Beer beer) {
        Beer existing = beerService.getBeerById(beer.getId()).orElseThrow();
        beer.setCreatedAt(existing.getCreatedAt());
        beer.setUpdatedAt(java.time.LocalDateTime.now());
        beerService.saveBeer(beer);
        return "redirect:/admin/beers";
    }
 
    // 削除処理（DBから物理削除）
    @PostMapping("/delete/{id}")
    public String deleteBeer(@PathVariable Long id) {
        beerService.deleteBeerById(id);
        return "redirect:/admin/beers";
    }
}