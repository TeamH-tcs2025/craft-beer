package com.example.craft_beer_app.repository;
 
import com.example.craft_beer_app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
 
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
        String sql = "INSERT INTO users (username, password_hash, email, role) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, user.getUsername(), user.getPasswordHash(), user.getEmail(), user.getRole());
    }
    
    public User findByEmail(String email) {
        String sql = "SELECT id, username, password_hash, email, role FROM users WHERE email = ?";
        try {
            return jdbc.queryForObject(sql, new UserRowMapper(), email);
        } catch (Exception e) {
            return null; // ユーザーが見つからない場合はnullを返す
        }
    }
    
    public User findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, email, role FROM users WHERE username = ?";
        try {
            return jdbc.queryForObject(sql, new UserRowMapper(), username);
        } catch (Exception e) {
            return null; // 見つからない場合はnull
        }
    }
    
    public List<User> findAll() {
        String sql = "SELECT id, username, password_hash, email, role FROM users";
        return jdbc.query(sql, new UserRowMapper());
    }
    
    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbc.update(sql, id);
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setEmail(rs.getString("email"));
            user.setRole(rs.getString("role"));
            return user;
        }
    }
}