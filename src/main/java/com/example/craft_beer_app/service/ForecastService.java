package com.example.craft_beer_app.service;

import com.example.craft_beer_app.model.DailySales;
import com.example.craft_beer_app.model.ForecastRequest;
import com.example.craft_beer_app.model.ForecastResponse;
import com.example.craft_beer_app.model.WeatherData;
import com.example.craft_beer_app.model.Forecast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ForecastService {

    private static final Logger logger = LoggerFactory.getLogger(ForecastService.class);
    private static final String FORECAST_API_URL = "https://salesfor.azurewebsites.net/api/salesforecast";
    private final RestTemplate restTemplate;
 
    @Autowired
    public ForecastService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * 需要予測APIを呼び出し、結果を取得する
     */
    public ForecastResponse getForecast(LocalDate forecastDate, WeatherData weatherData, List<DailySales> recentSales) {
        try {
            // リクエスト用のオブジェクト作成
            ForecastRequest request = new ForecastRequest();
            request.setDate(forecastDate.format(DateTimeFormatter.ISO_DATE));
            request.setWeather(weatherData);
            request.setRecent_sales(recentSales);

            // APIリクエストヘッダー設定
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // APIリクエスト実行
            HttpEntity<ForecastRequest> entity = new HttpEntity<>(request, headers);
            logger.info("Sending request to forecast API: {}", request);
            
            ResponseEntity<ForecastResponse> response = restTemplate.postForEntity(
                    FORECAST_API_URL, entity, ForecastResponse.class);
            
            logger.info("Received response from forecast API: {}", response.getBody());
            return response.getBody();
            
        } catch (Exception e) {
            logger.error("Error calling forecast API", e);
            // エラー時はデモデータを返す
            return createDemoForecastResponse();
        }
    }

    /**
     * デモ用のフォーキャストレスポンスを作成
     */
    private ForecastResponse createDemoForecastResponse() {
        ForecastResponse response = new ForecastResponse();
        response.setPaleAle("12本（予測値: 11.83）");
        response.setLager("9本（予測値: 8.62）");
        response.setIpa("6本（予測値: 5.79）");
        response.setWhiteBeer("4本（予測値: 3.61）");
        response.setDarkBeer("7本（予測値: 6.85）");
        response.setFruitBeer("5本（予測値: 4.72）");
        response.setTotalBottles("43本（予測値: 41.42）");
        return response;
    }

    /**
     * デモ用の過去販売データを生成
     */
    public List<DailySales> generateSalesHistory(int days) {
        List<DailySales> salesHistory = new ArrayList<>();
        LocalDate today = LocalDate.now();
        Random random = new Random();
        
        // 過去14日間のデータを生成
        for (int i = days; i > 0; i--) {
            LocalDate date = today.minusDays(i);
            DailySales sales = new DailySales(
                    date,
                    5 + random.nextInt(8),  // ペールエール
                    4 + random.nextInt(7),  // ラガー
                    3 + random.nextInt(5),  // IPA
                    2 + random.nextInt(4),  // ホワイトビール
                    3 + random.nextInt(5),  // 黒ビール
                    2 + random.nextInt(4)   // フルーツビール
            );
            salesHistory.add(sales);
        }
        
        return salesHistory;
    }

    /**
     * 今日の天気情報を取得（実際にはAPI連携すべき箇所）
     */
    public WeatherData getTodayWeather() {
        // 実装では実際に気象APIを呼ぶべきだが、デモとしてハードコード値を返す
        return new WeatherData(
                22.5,  // 最高気温(℃)
                65.0,  // 平均湿度(％)
                0.0,   // 降水量の合計(mm)
                3.2    // 最大風速(m/s)
        );
    }
}