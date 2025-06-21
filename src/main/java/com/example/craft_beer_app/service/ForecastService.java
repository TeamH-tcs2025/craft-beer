package com.example.craft_beer_app.service;

import com.example.craft_beer_app.model.DailySales;
import com.example.craft_beer_app.model.ForecastResponse;
import com.example.craft_beer_app.model.WeatherData;
import com.example.craft_beer_app.util.BeerTypeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ForecastService {

    private static final Logger logger = LoggerFactory.getLogger(ForecastService.class);
    private static final String FORECAST_API_URL = "https://salesfor1.azurewebsites.net/api/sales";
    
    private final RestTemplate restTemplate;
    private final SalesRecordService salesRecordService;
    private final WeatherService weatherService;
    
    @Autowired
    public ForecastService(RestTemplate restTemplate, SalesRecordService salesRecordService, WeatherService weatherService) {
        this.restTemplate = restTemplate;
        this.salesRecordService = salesRecordService;
        this.weatherService = weatherService;
    }

    /**
     * 明日の需要予測データを取得するためのメソッド
     * @return 予測結果のマップ
     */
    public Map<String, Integer> getRecommendedOrder() {
        try {
            // 明日の日付
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            
            // 今日の天気データを取得
            WeatherData weatherData = getTodayWeather();
            logger.info("Today's weather data: {}", weatherData);
            
            // 過去14日間の販売履歴をデータベースから取得
            List<DailySales> salesHistory = salesRecordService.getSalesHistoryForLastDays(14);
            logger.info("Retrieved {} days of sales history", salesHistory.size());
            
            // 日曜日を除外し、null値を0に置き換える
            List<DailySales> cleanedSalesHistory = cleanSalesData(salesHistory);
            logger.info("After cleaning, using {} days of sales history", cleanedSalesHistory.size());
            
            // 予測APIを呼び出し
            ForecastResponse response = getForecast(tomorrow, weatherData, cleanedSalesHistory);
            
            if (response == null) {
                logger.warn("Received null response from forecast API, using demo data");
                response = createDemoForecastResponse();
            }
            
            // 結果をIntegerのマップに変換
            Map<String, String> stringMap = response.toRecommendationMap();
            Map<String, Integer> result = new HashMap<>();
            
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                try {
                    // 数値部分だけを抽出して整数に変換
                    String value = entry.getValue();
                    if (value != null && value.contains("本")) {
                        value = value.substring(0, value.indexOf("本"));
                    }
                    result.put(entry.getKey(), Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    logger.warn("Could not parse value '{}' for beer type '{}', using 0", entry.getValue(), entry.getKey());
                    result.put(entry.getKey(), 0);
                }
            }
            
            // 合計本数の計算と確認
            int total = 0;
            // ビールの種類だけの合計を計算（「合計」キーは除く）
            for (Map.Entry<String, Integer> entry : result.entrySet()) {
                if (!entry.getKey().equals("合計")) {
                    total += entry.getValue();
                }
            }
            
            // 合計の項目が既にあれば、その値と計算した合計を比較して違いがあれば警告
            if (result.containsKey("合計") && result.get("合計") != total) {
                logger.warn("Total bottles from API ({}) doesn't match calculated total ({})", result.get("合計"), total);
            }
            // 合計項目がなければ追加
            if (!result.containsKey("合計")) {
                result.put("合計", total);
            }
            
            return result;
        } catch (Exception e) {
            logger.error("Error in getRecommendedOrder", e);
            // エラー時はデモデータを返す
            return getDemoRecommendation();
        }
    }

    /**
     * 需要予測APIを呼び出し、結果を取得する
     */
    public ForecastResponse getForecast(LocalDate forecastDate, WeatherData weatherData, List<DailySales> recentSales) {
        try {
            // APIの仕様に合わせたリクエストを構築
            Map<String, Object> requestMap = new HashMap<>();
            
            // 日付をISO形式で設定
            requestMap.put("date", forecastDate.format(DateTimeFormatter.ISO_DATE));
            
            // 天気データを仕様通りの形式で設定
            Map<String, Object> weatherMap = new HashMap<>();
            weatherMap.put("最高気温(℃)", weatherData.getTemp());
            weatherMap.put("平均湿度(％)", weatherData.getHumidity());
            weatherMap.put("降水量の合計(mm)", weatherData.getPrecipitation());
            weatherMap.put("最大風速(m/s)", weatherData.getWind_speed());
            requestMap.put("weather", weatherMap);
            
            // 過去の販売データを仕様通りの形式に変換
            List<Map<String, Object>> salesDataList = new ArrayList<>();
            for (DailySales sales : recentSales) {
                Map<String, Object> salesEntry = new HashMap<>();
                salesEntry.put("date", sales.getDate());
                salesEntry.put("ペールエール(本)", sales.getPaleAle());
                salesEntry.put("ラガー(本)", sales.getLager());
                salesEntry.put("IPA(本)", sales.getIpa());
                salesEntry.put("ホワイトビール(本)", sales.getWhiteBeer());
                salesEntry.put("黒ビール(本)", sales.getDarkBeer());
                salesEntry.put("フルーツビール(本)", sales.getFruitBeer());
                salesDataList.add(salesEntry);
            }
            requestMap.put("recent_sales", salesDataList);
            
            // APIリクエストヘッダー設定
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // リクエストをログに出力してデバッグ
            ObjectMapper mapper = new ObjectMapper();
            logger.info("Sending request to forecast API: {}", mapper.writeValueAsString(requestMap));
            
            // リクエストを送信し、文字列としてレスポンスを受け取る
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    FORECAST_API_URL, entity, String.class);
            
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                logger.error("API returned unsuccessful status: {} or null body", response.getStatusCode());
                return createDemoForecastResponse();
            }
            
            // レスポンスの中身を確認
            String responseBody = response.getBody();
            logger.info("API Raw Response: {}", responseBody);
            
            // デフォルトメッセージのチェック
            if (responseBody.contains("HTTP triggered function executed successfully")) {
                logger.error("Received default Azure Function response, API implementation might be incorrect");
                return createDemoForecastResponse();
            }
            
            // JSONレスポンスをパース
            if (responseBody.contains("{") && responseBody.contains("}")) {
                try {
                    // 完全に一致するようにキーを設定してマッピングする
                    Map<String, String> responseMap = mapper.readValue(responseBody, Map.class);
                    
                    ForecastResponse forecastResponse = new ForecastResponse();
                    forecastResponse.setPaleAle(responseMap.get("ペールエール(本)"));
                    forecastResponse.setLager(responseMap.get("ラガー(本)"));
                    forecastResponse.setIpa(responseMap.get("IPA(本)"));
                    forecastResponse.setWhiteBeer(responseMap.get("ホワイトビール(本)"));
                    forecastResponse.setDarkBeer(responseMap.get("黒ビール(本)"));
                    forecastResponse.setFruitBeer(responseMap.get("フルーツビール(本)"));
                    forecastResponse.setTotalBottles(responseMap.get("総予測杯数"));
                    
                    return forecastResponse;
                } catch (Exception e) {
                    logger.error("Error parsing API response as JSON: {}", e.getMessage(), e);
                }
            }
            
            // JSON解析に失敗した場合はデモデータを返す
            return createDemoForecastResponse();
            
        } catch (Exception e) {
            logger.error("Error calling forecast API: {}", e.getMessage(), e);
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
     * デモ用の推奨発注数量を取得
     */
    private Map<String, Integer> getDemoRecommendation() {
        Map<String, Integer> demo = new HashMap<>();
        demo.put("ペールエール", 12);
        demo.put("ラガー", 9);
        demo.put("IPA", 6);
        demo.put("ホワイトビール", 4);
        demo.put("黒ビール", 7);
        demo.put("フルーツビール", 5);
        demo.put("合計", 43);
        return demo;
    }

    /**
     * 今日の天気情報を取得
     */
    public WeatherData getTodayWeather() {
        try {
            // WeatherServiceから今日の天気データを取得
            com.example.craft_beer_app.model.Weather weather = weatherService.getTodayWeatherData();
            
            if (weather != null) {
                // APIに必要な形式に変換
                double temperature = (weather.getTemperatureHigh() != null) ? weather.getTemperatureHigh() : 22.5;
                double humidity = (weather.getHumidity() != null) ? weather.getHumidity() : 65.0;
                double precipitation = (weather.getPrecipitation() != null) ? weather.getPrecipitation() : 0.0;
                double windSpeed = (weather.getWindSpeed() != null) ? weather.getWindSpeed() : 3.2;
                
                return new WeatherData(temperature, humidity, precipitation, windSpeed);
            }
        } catch (Exception e) {
            logger.error("Error getting today's weather: {}", e.getMessage(), e);
        }
        
        // エラー時またはデータがない時はデフォルト値を返す
        logger.info("Using default weather data");
        return new WeatherData(
            22.5,  // 最高気温(℃)
            65.0,  // 平均湿度(％)
            0.0,   // 降水量の合計(mm)
            3.2    // 最大風速(m/s)
        );
    }

    /**
     * 翌3日間の需要予測に基づく推奨発注数量を計算
     * @return 推奨発注数量のマップ
     */
    public Map<String, Integer> getRecommendedOrderQuantity() {
        Map<String, Integer> result = new HashMap<>();
        
        try {
            // 今日の日付を取得
            LocalDate today = LocalDate.now();
            
            // 翌3日間の予測結果合計を計算
            int paleAleTotal = 0;
            int lagerTotal = 0;
            int ipaTotal = 0;
            int whiteBeerTotal = 0;
            int darkBeerTotal = 0;
            int fruitBeerTotal = 0;
            
            // 天気データを一度だけ取得（現時点では当日の天気のみ利用可能）
            WeatherData weatherData = getTodayWeather();
            
            // 過去14日間の販売履歴を一度だけ取得（3日分の予測でも同じデータを使用）
            List<DailySales> salesHistory = salesRecordService.getSalesHistoryForLastDays(14);
            
            // 日曜日を除外し、null値を0に置き換える
            List<DailySales> cleanedSalesHistory = cleanSalesData(salesHistory);
            
            logger.info("Forecasting demand for the next 3 days using {} days of sales history", cleanedSalesHistory.size());
            
            // 翌日から3日分の予測を取得し合計
            for (int i = 1; i <= 3; i++) {
                LocalDate targetDate = today.plusDays(i);
                
                // 日曜日は除外（店舗が休業のため）
                if (targetDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    logger.info("Skipping forecast for {} as it's Sunday (closed day)", targetDate);
                    continue;
                }
                
                // 各日の予測を同じ履歴データと天気データを使って取得
                ForecastResponse forecast = getForecast(targetDate, weatherData, cleanedSalesHistory);
                
                if (forecast == null) {
                    logger.warn("Null forecast for {}, using demo data", targetDate);
                    forecast = createDemoForecastResponse();
                }
                
                // 予測本数を抽出して合計に追加 (文字列から数値を抽出)
                paleAleTotal += extractQuantity(forecast.getPaleAle());
                lagerTotal += extractQuantity(forecast.getLager());
                ipaTotal += extractQuantity(forecast.getIpa());
                whiteBeerTotal += extractQuantity(forecast.getWhiteBeer());
                darkBeerTotal += extractQuantity(forecast.getDarkBeer());
                fruitBeerTotal += extractQuantity(forecast.getFruitBeer());
                
                logger.info("Day {} ({}): Pale Ale={}, Lager={}, IPA={}, White={}, Dark={}, Fruit={}", 
                        i, targetDate, 
                        extractQuantity(forecast.getPaleAle()),
                        extractQuantity(forecast.getLager()),
                        extractQuantity(forecast.getIpa()),
                        extractQuantity(forecast.getWhiteBeer()),
                        extractQuantity(forecast.getDarkBeer()),
                        extractQuantity(forecast.getFruitBeer()));
            }
            
            // 安全在庫を少し足す (売り切れ防止のための10%増し)
            paleAleTotal = (int) Math.ceil(paleAleTotal * 1.1);
            lagerTotal = (int) Math.ceil(lagerTotal * 1.1);
            ipaTotal = (int) Math.ceil(ipaTotal * 1.1);
            whiteBeerTotal = (int) Math.ceil(whiteBeerTotal * 1.1);
            darkBeerTotal = (int) Math.ceil(darkBeerTotal * 1.1);
            fruitBeerTotal = (int) Math.ceil(fruitBeerTotal * 1.1);
            
            // 結果をマップに格納
            result.put("ペールエール", paleAleTotal);
            result.put("ラガー", lagerTotal);
            result.put("IPA", ipaTotal);
            result.put("ホワイトビール", whiteBeerTotal);
            result.put("黒ビール", darkBeerTotal);
            result.put("フルーツビール", fruitBeerTotal);
            
            // 合計計算
            int totalOrder = paleAleTotal + lagerTotal + ipaTotal + whiteBeerTotal + darkBeerTotal + fruitBeerTotal;
            result.put("合計", totalOrder);
            
            logger.info("Calculated recommended order quantity: {}", result);
            
        } catch (Exception e) {
            logger.error("Error calculating recommended order quantity", e);
            // エラー時はデモデータを返す
            result = getDemoRecommendation();
        }
        
        return result;
    }

    /**
     * 予測値の文字列から数量を抽出
     * 例: "12本（予測値: 11.83）" から 12 を抽出
     */
    private int extractQuantity(String forecastValue) {
        if (forecastValue == null || forecastValue.isEmpty()) {
            return 0;
        }
        
        try {
            // 数字を抽出
            Pattern pattern = Pattern.compile("^(\\d+)本");
            Matcher matcher = pattern.matcher(forecastValue);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
        } catch (Exception e) {
            logger.warn("Failed to extract quantity from: {}", forecastValue);
        }
        
        return 0;
    }
    
    /**
     * 販売データを整理する：
     * 1. 日曜日を除外する（休業日のため）
     * 2. nullの値を0に置き換える
     */
    private List<DailySales> cleanSalesData(List<DailySales> salesHistory) {
        return salesHistory.stream()
            .filter(sales -> {
                // 日付をLocalDateに変換
                try {
                    LocalDate date = LocalDate.parse(sales.getDate());
                    // 日曜日を除外
                    return date.getDayOfWeek() != DayOfWeek.SUNDAY;
                } catch (Exception e) {
                    logger.error("Error parsing date: {}", sales.getDate(), e);
                    return false; // 日付が不正な場合も除外
                }
            })
            .map(sales -> {
                // nullの値を0に置換
                if (sales.getPaleAle() == null) sales.setPaleAle(0);
                if (sales.getLager() == null) sales.setLager(0);
                if (sales.getIpa() == null) sales.setIpa(0);
                if (sales.getWhiteBeer() == null) sales.setWhiteBeer(0);
                if (sales.getDarkBeer() == null) sales.setDarkBeer(0);
                if (sales.getFruitBeer() == null) sales.setFruitBeer(0);
                return sales;
            })
            .collect(Collectors.toList());
    }
}