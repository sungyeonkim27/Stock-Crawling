package com.example.crawling.service;

import com.example.crawling.crawler.MarketCrawler;
import com.example.crawling.model.MarketIndex;
import com.example.crawling.model.MarketNews;
import com.example.crawling.repository.MarketIndexRepository;
import com.example.crawling.repository.MarketNewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MarketDataService {
    private final MarketCrawler marketCrawler;
    private final MarketNewsRepository marketNewsRepository;
    private final MarketIndexRepository marketIndexRepository;

    public Map<String, Object> getMarketIndex() {
        Map<String, Object> data = marketCrawler.getKospiInfo();
        if (data.containsKey("current") && data.containsKey("change")) {
            MarketIndex marketIndex = new MarketIndex();
            marketIndex.setCurrent((double) data.get("current"));
            marketIndex.setChange((String) data.get("change"));
            marketIndex.setDirection((String) data.getOrDefault("direction", "알 수 없음"));
            marketIndex.setCrawledAt(LocalDateTime.now());
            marketIndexRepository.save(marketIndex);
        }
        return data;
    }

    public List<String> getMarketNews() {
        List<String> newsList = marketCrawler.getTopNews();
        LocalDateTime now = LocalDateTime.now();

        newsList.forEach(title -> {
            MarketNews marketNews = new MarketNews();
            marketNews.setTitle(title);
            marketNews.setCrawledAt(now);
            marketNewsRepository.save(marketNews);
        });
        return newsList;
    }
}
