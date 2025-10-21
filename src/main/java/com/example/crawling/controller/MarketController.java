package com.example.crawling.controller;

import com.example.crawling.crawler.MarketCrawler;
import com.example.crawling.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {

    private final MarketCrawler marketCrawler;
    private final MarketDataService marketDataService;

    @GetMapping("/summary")
    public Map<String, Object> getMarketSummary() {
        Map<String, Object> result = new HashMap<>();
        result.put("KOSPI", marketDataService.getMarketIndex());
        result.put("TopNews", marketDataService.getMarketNews());
        return result;
    }
}
