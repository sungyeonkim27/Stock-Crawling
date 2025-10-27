package com.example.crawling.service;

import com.example.crawling.crawler.NaverStockCrawler;
import com.example.crawling.dto.CrawledPriceResponseDto;
import com.example.crawling.dto.StockResponseDto;
import com.example.crawling.model.CrawledPrice;
import com.example.crawling.model.Stock;
import com.example.crawling.repository.CrawledPriceRepository;
import com.example.crawling.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // 크롤링한 내용들 전부 조회
    public List<CrawledPrice> getAllCrawledPrice() {
        return crawledPriceRepository.findAllByOrderByIdDesc();
    }

    // 이름 혹은 코드로 조회
    public List<CrawledPriceResponseDto> searchCrawledPriceByKeyword(String keyword) {
        // 키워드로 Stock 데이터 가져오기
        List<Stock> matchingStocks = stockRepository.findByNameContaining(keyword);

        // 코드
        List<String> codes = matchingStocks
                .stream()
                .map(Stock::getCode)
                .collect(Collectors.toList());

        // 키워드가 숫자면 코드 직접 추가
        if (keyword.matches("\\d+")) {
            codes.add(keyword);
        }

        // CrawledPrice 데이터 가져오기
        List<CrawledPrice> prices = crawledPriceRepository.findByCodeIn(codes);

        // 코드 -> 이름 매핑
        Map<String, String> codeToName = matchingStocks
                .stream()
                .collect(Collectors.toMap(Stock::getCode, Stock::getName));

        return prices.stream().map(price -> new CrawledPriceResponseDto(
                price.getCode(),
                codeToName.getOrDefault(price.getCode(), "(이름없음)"),
                price.getPrice(),
                price.getTime()
        )).toList();
    }

}



