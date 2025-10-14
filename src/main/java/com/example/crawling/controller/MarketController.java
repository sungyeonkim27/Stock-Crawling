package com.example.crawling.controller;

import com.example.crawling.crawler.MarketCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {

    private final MarketCrawler marketCrawler;

    @GetMapping("/summary")
    public Map<String, Object> getMarketSummary() {
        Map<String, Object> result = new HashMap<>();
        result.put("KOSPI", marketCrawler.getKospiInfo());
        result.put("TopNews", marketCrawler.getTopNews());
        return result;
    }
}
