package com.example.crawling.service;

import com.example.crawling.crawler.NaverStockCrawler;
import com.example.crawling.dto.StockResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockService {

    private final NaverStockCrawler crawler;

    // 여러 종목을 한번에 조회
    public Map<String, StockResponseDto> getStockData() {
        Map<String, String> codes = Map.of(
                "삼성전자", "005930",
                "SK하이닉스", "000660",
                "NAVER", "035420",
                "카카오", "035720"
        );

        Map<String, StockResponseDto> result = new HashMap<>();

        codes.forEach((name, code) ->
                crawler.getPrice(code).ifPresent(price ->
                        result.put(name, new StockResponseDto(name, price, code))
                )
        );

        return result;
    }
}

