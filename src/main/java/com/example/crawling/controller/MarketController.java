package com.example.crawling.controller;

import com.example.crawling.model.MarketNews;
import com.example.crawling.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/market")
public class MarketController {

    private final MarketDataService marketDataService;

    @GetMapping("/summary")
    public Map<String, Object> getMarketSummary() {
        Map<String, Object> result = new HashMap<>();
        result.put("KOSPI", marketDataService.getMarketIndex());
        result.put("TopNews", marketDataService.getMarketNews());
        return result;
    }

    //키워드로 뉴스 조회
    @GetMapping("/search")
    public ResponseEntity<List<MarketNews>> searchMarketNewsByCode(String keyword) {
        List<MarketNews> result = marketDataService.searchMarketNewsByCode(keyword);
        return ResponseEntity.ok(result);
    }

    // 뉴스 및 코스피 주가 전체 조회
    @GetMapping("/allSearch")
    public Map<String, Object> getAllMarketData() {
        Map<String, Object> result = new HashMap<>();
        result.put("KOSPI", marketDataService.getAllMarketIndex());
        result.put("TopNews", marketDataService.getAllMarketNews());
        return result;
    }

    // 저장된 코스피 지수 & 증권 뉴스 모두 삭제
    @DeleteMapping("/allDelete")
    public ResponseEntity<Void> deleteAllMarketData() {
        marketDataService.deleteAllMarketIndex();
        marketDataService.deleteAllMarketNews();
        return ResponseEntity.noContent().build();
    }
}
