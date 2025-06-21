package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.model.User;
import com.example.craft_beer_app.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/ad-user-management")
    public String listUsers(Model model, HttpSession session) {
        // セッションからログイン情報を取得
        String email = (String) session.getAttribute("email");
        if (email == null) {
            // ログインしていない場合はログイン画面にリダイレクト
            return "redirect:/login";
        }

        try {
            // ユーザー一覧を取得してモデルに追加
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
            return "ad-user-management";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "ユーザー情報の取得中にエラーが発生しました: " + e.getMessage());
            return "redirect:/admin/home";
        }
    }

    // ユーザー削除用エンドポイント
    @DeleteMapping("/ad-user-management/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, HttpSession session) {
        // セッションからログイン情報を取得
        String email = (String) session.getAttribute("email");
        if (email == null) {
            // ログインしていない場合は401エラーを返す
            return ResponseEntity.status(401).build();
        }

        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}