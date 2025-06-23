package com.example.craft_beer_app.service;

import com.example.craft_beer_app.model.Beer;
import com.example.craft_beer_app.repository.BeerRepository; // 正しいインポートパス
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeerService {

    private final BeerRepository beerRepository;

    @Autowired
    public BeerService(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    public List<Beer> getAllBeers() {
        return beerRepository.findAll();
    }
    
    public List<Beer> getAllActiveBeers() {
        return beerRepository.findByIsActiveTrue();
    }

    public Optional<Beer> getBeerById(Long id) {
        return beerRepository.findById(id);
    }

    public Beer saveBeer(Beer beer) {
        return beerRepository.save(beer);
    }

    public void deleteBeer(Long id) {
        Optional<Beer> beerOpt = beerRepository.findById(id);
        if (beerOpt.isPresent()) {
            Beer beer = beerOpt.get();
            beer.setIsActive(false);
            beerRepository.save(beer);
        }
    }

    public void deleteBeerById(Long id) {
        beerRepository.deleteById(id);
    }
}