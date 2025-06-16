package com.example.craft_beer_app.repository;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
 
@Repository
public class UserRepository {
 
    @Autowired
    private JdbcTemplate jdbc;
 
    public boolean exists(String username, String hash) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password_hash = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, username, hash);
        return count != null && count > 0;
    }
}