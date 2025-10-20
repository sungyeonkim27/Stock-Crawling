package com.example.crawling.service;

import com.example.crawling.crawler.NaverStockCrawler;
import com.example.crawling.dto.StockResponseDto;
import com.example.crawling.model.CrawledPrice;
import com.example.crawling.model.Stock;
import com.example.crawling.repository.CrawledPriceRepository;
import com.example.crawling.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockService {

    private final NaverStockCrawler crawler;
    private final StockRepository stockRepository;
    private final CrawledPriceRepository crawledPriceRepository;

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
                crawler.getPrice(code).ifPresent(price -> {

                        // 종목 저장
                        stockRepository.findByCode(code).orElseGet(() -> {
                            Stock stock = new Stock();
                            stock.setCode(code);
                            stock.setName(name);
                            return stockRepository.save(stock);
                        });

                        // 가격 저장
                        CrawledPrice cp = new CrawledPrice();
                        cp.setCode(code);
                        cp.setPrice(price);
                        cp.setTime(LocalDateTime.now());
                        crawledPriceRepository.save(cp);

                        // 결과 반환
                        result.put(name, new StockResponseDto(name, price, code));
                })
        );

        return result;
    }
}

