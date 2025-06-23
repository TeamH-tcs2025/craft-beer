// package com.example.craft_beer_app.controller;
// //一般
// import com.example.craft_beer_app.service.WeatherService;
// import com.example.craft_beer_app.model.User;
// import com.example.craft_beer_app.repository.UserRepository;
// import com.example.craft_beer_app.service.SalesService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import jakarta.servlet.http.HttpSession;
// import org.springframework.web.bind.annotation.GetMapping;
// import com.example.craft_beer_app.model.Weather;
// import com.example.craft_beer_app.model.SalesRecord;
// import com.example.craft_beer_app.repository.SalesRecordRepository;

// import java.time.LocalDate;
// import java.util.Map;

// @Controller
// public class UserHomeController {
//     @Autowired
//     private WeatherService weatherService;

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private SalesService salesService;

//     @GetMapping("/user/home")
//     public String userHome(HttpSession session, Model model) {

//         String email = (String) session.getAttribute("email");
//         if (email == null)
//             return "redirect:/login";

//         User user = userRepository.findByEmail(email);
//         if (user == null) {
//             return "redirect:/login"; // ユーザーが見つからない場合はログインページへリダイレクト
//         }

//         model.addAttribute("username", user.getUsername());
        
//         // 昨日の販売実績データを取得
//         LocalDate yesterday = LocalDate.now().minusDays(1);
//         Map<String, Integer> yesterdaySales = salesService.getYesterdaySales();
        
//         // モデルに昨日の日付と販売実績を追加
//         model.addAttribute("yesterday", yesterday);
//         model.addAttribute("yesterdaySales", yesterdaySales);
        
//         // 天気データ
//         Weather todayWeather = weatherService.getTodayWeatherData();
//         model.addAttribute("todayWeather", todayWeather);
        
//         return "adhome";
//     }
// }
