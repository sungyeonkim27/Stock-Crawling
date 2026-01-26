package com.example.crawling.util;

public class ValidationUtils {
    public static boolean isValidStockCode(String stockCode) {
        return stockCode != null && stockCode.matches("^[0-9]{6}$");
    }
}
