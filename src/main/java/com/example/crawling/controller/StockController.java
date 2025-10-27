package com.example.crawling.controller;

import com.example.crawling.dto.CrawledPriceResponseDto;
import com.example.crawling.dto.StockResponseDto;
import com.example.crawling.model.CrawledPrice;
import com.example.crawling.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    @GetMapping
    public Map<String, StockResponseDto> getStocks() {
        return stockService.getStockData();
    }

    // 키워드 검색 조회
    @GetMapping("/search")
    public ResponseEntity<List<CrawledPriceResponseDto>> searchCrawledPrice(@RequestParam String keyword) {
        List<CrawledPriceResponseDto> result = stockService.searchCrawledPriceByKeyword(keyword);
        return ResponseEntity.ok(result);
    }
    // 전체 조회
    @GetMapping("/allSearch")
    public ResponseEntity<List<CrawledPrice>> getAllCrawledPrice() {
        List<CrawledPrice> crawledPrices = stockService.getAllCrawledPrice();
        return ResponseEntity.ok(crawledPrices);
    }
}
