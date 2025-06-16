package com.example.craft_beer_app.controller;

import com.example.craft_beer_app.dto.SalesRecordDto;
import com.example.craft_beer_app.model.Beer;
import com.example.craft_beer_app.model.SalesRecord;
import com.example.craft_beer_app.service.BeerService;
import com.example.craft_beer_app.service.SalesRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // DBの接続情報　（finalをつけると定数になる）
        final String URL = "jdbc:postgresql://localhost:5432/login_sample";
        final String USER = "postgres";
        final String PASSWORD = "postgres";

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // ハッシュ化
        String hashedPassword = PasswordUtil.hash(password);

        boolean loginSuccess = false;
        
        // なぜかドライバロードの一文を入れないと動かなかったので以下のように記述
        try {
            // JDBCドライバを明示的にロード
            Class.forName("org.postgresql.Driver");

            // 接続処理
            try (
                    Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                    PreparedStatement stmt = conn.prepareStatement(
                            "SELECT * FROM users WHERE username = ? AND password_hash = ?")) {
                // SQLインジェクション対策の「?」
                stmt.setString(1, username);
                stmt.setString(2, hashedPassword);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    loginSuccess = true;
                }
            }

        } catch (ClassNotFoundException e) {
            throw new ServletException("PostgreSQL JDBC ドライバが見つかりません", e);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        if (loginSuccess) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            response.sendRedirect("welcome");
        } else {
            response.sendRedirect("login.jsp");
        }
    }
}
