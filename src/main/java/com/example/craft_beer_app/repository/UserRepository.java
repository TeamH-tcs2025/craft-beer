package com.example.craft_beer_app.repository;
 
import com.example.craft_beer_app.model.User;
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
    public boolean exists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }
    public void save(User user) {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        jdbc.update(sql, user.getUsername(), user.getPasswordHash());
    }
}