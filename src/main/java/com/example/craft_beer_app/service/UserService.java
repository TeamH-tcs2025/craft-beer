package com.example.craft_beer_app.service;
 
import com.example.craft_beer_app.repository.UserRepository;
import com.example.craft_beer_app.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service
public class UserService {
    @Autowired
    private UserRepository repo;
 
    public boolean authenticate(String username, String password) {
        String hash = PasswordUtil.hash(password);
        return repo.exists(username, hash);
    }
}