package com.example.craft_beer_app.util;

/**
 * ビール名からタイプを判別するユーティリティクラス
 */
public class BeerTypeUtil {
    
    /**
     * ビール名からタイプを判別する
     * @param beerName ビール名
     * @return ビールタイプ
     */
    public static String getBeerTypeFromName(String beerName) {
        if (beerName == null) {
            return "未分類";
        }
        
        String lowerName = beerName.toLowerCase();
        
        if (lowerName.contains("ペールエール") || lowerName.contains("pale ale") || 
            (lowerName.contains("エール") && !lowerName.contains("ダーク"))) {
            return "ペールエール";
        } else if (lowerName.contains("ラガー") || lowerName.contains("lager")) {
            return "ラガー";
        } else if (lowerName.contains("ipa") || lowerName.contains("インディア")) {
            return "IPA";
        } else if (lowerName.contains("ホワイト") || lowerName.contains("white") || 
                  lowerName.contains("ヴァイス") || lowerName.contains("weiss")) {
            return "ホワイトビール";
        } else if (lowerName.contains("黒") || lowerName.contains("スタウト") || 
                  lowerName.contains("stout") || lowerName.contains("ポーター") || 
                  lowerName.contains("porter") || lowerName.contains("ダーク") || 
                  lowerName.contains("dark")) {
            return "黒ビール";
        } else if (lowerName.contains("フルーツ") || lowerName.contains("fruit") || 
                  lowerName.contains("桃") || lowerName.contains("苺") || 
                  lowerName.contains("柑橘") || lowerName.contains("オレンジ") || 
                  lowerName.contains("レモン")) {
            return "フルーツビール";
        }
        
        return "未分類";
    }
}