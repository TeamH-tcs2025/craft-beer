package com.example.craft_beer_app;

import org.springframework.web.bind.annotation.*;

@RestController
public class test {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }
}