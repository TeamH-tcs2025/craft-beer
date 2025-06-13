package com.example.craft_beer_app.service;

import com.example.craft_beer_app.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Long> {
    List<Beer> findByIsActiveTrue();
}