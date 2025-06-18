package com.example.craft_beer_app.service;

import com.example.craft_beer_app.model.User;
import com.example.craft_beer_app.repository.UserRepository;
import com.example.craft_beer_app.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    // ログイン処理
    public boolean authenticate(String email, String password) {
        User user = repo.findByEmail(email);
        if (user == null) {
            return false; // ユーザーが存在しない
        }
        String hash = PasswordUtil.hash(password);
        return user.getPasswordHash().equals(hash);
    }

    // サインアップ処理
    public boolean register(String username, String password, String email, String role) {
        if (repo.exists(username)) {
            return false; // 既に登録済み
        }
        String hash = PasswordUtil.hash(password);
        repo.save(new User(username, hash, email, role));
        return true;
    }
    public User findByEmailAndPassword(String email, String password) {
    User user = repo.findByEmail(email);
    if (user == null) return null;

    String hash = PasswordUtil.hash(password);
    return user.getPasswordHash().equals(hash) ? user : null;
}
}