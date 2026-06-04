package com.example.crawling.service;

import com.example.crawling.crawler.NaverStockCrawler;
import com.example.crawling.crawler.NaverStockHistoryCrawler;
import com.example.crawling.dto.CrawledPriceResponseDto;
import com.example.crawling.dto.StockResponseDto;
import com.example.crawling.model.*;
import com.example.crawling.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final NaverStockCrawler crawler;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final UserStockRepository userStockRepository;
    private final CrawledPriceRepository crawledPriceRepository;
    private final NaverStockHistoryCrawler historyСrawler;
    private final StockHistoryRepository stockHistoryRepository;

    // 여러 종목을 한번에 크롤링
    public Map<String, StockResponseDto> getStockData(Principal principal) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("해당 사용자는 존재하지 않습니다."));
        Map<String, StockResponseDto> result = new HashMap<>();
        List<UserStock> userStocks = userStockRepository.findByUser(user);

        Map<String, String> codes = userStocks.stream()
                .collect(Collectors.toMap(
                us -> us.getStock().getName(),
                us -> us.getStock().getCode()
        ));

        codes.forEach((name, code) ->
                crawler.getStockInfo(code).ifPresent(stockInfo -> {

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
                        cp.setStockName(name);
                        cp.setPrice(stockInfo.price());
                        cp.setUsername(username);
                        cp.setTime(LocalDateTime.now());
                        crawledPriceRepository.save(cp);

                        // 결과 반환
                        result.put(name, new StockResponseDto(name, stockInfo.price(), code, username));
                })
        );

        return result;
    }

    // 크롤링한 내용들 전부 조회
    public List<CrawledPriceResponseDto> getAllCrawledPrice(String username) {
        List<CrawledPrice> crawledPrices = crawledPriceRepository.findByUsernameOrderByIdDesc(username);
        return crawledPrices
                .stream()
                .map(CrawledPriceResponseDto::new)
                .toList();
    }

    // 이름 혹은 코드로 조회
    public List<CrawledPriceResponseDto> searchCrawledPriceByKeyword(String keyword, String username) {
        // 키워드로 Stock 데이터 가져오기
        List<Stock> matchingStocks = stockRepository.findByNameContaining(keyword);

        // 종목 코드
        List<String> codes = matchingStocks
                .stream()
                .map(Stock::getCode)
                .collect(Collectors.toList());

        // 키워드가 숫자면 코드 직접 추가
        if (keyword.matches("\\d+")) {
            codes.add(keyword);
        }

        // CrawledPrice 데이터 가져오기
        List<CrawledPrice> prices = crawledPriceRepository.findByCodeInAndUsername(codes, username);

        return prices.stream()
                .map(CrawledPriceResponseDto::new)
                .toList();
    }

    // 해당 크롤링된 주식 삭제
    @Transactional
    public void deletePricesByStockCode(String stockCode) {
        Optional<Stock> stockOptional = stockRepository.findByCode(stockCode);
        if (stockOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 코드의 종목이 존재하지 않습니다: " + stockCode);
        }
        crawledPriceRepository.deleteByCode(stockCode);

    }

    // 종목 히스토리 조회 (DB에 없으면 크롤링 후 저장)
    // 최초 90일치 조회
    public List<StockHistory> getStockHistory(String code, String stockName) {
        List<StockHistory> existing = stockHistoryRepository.findHistoryByCode(code);

        if (existing.isEmpty()) {
            List<StockHistory> crawled = historyСrawler.crawlHistory(code, stockName, 90);
            stockHistoryRepository.saveAll(crawled);
            return crawled;
        }
        return existing;
    }

    // 오늘 종가만 추가
    public void updateTodayHistory(String code, String stockName) {
        List<StockHistory> crawled = historyСrawler.crawlHistory(code, stockName, 1);

        if (crawled.isEmpty()) return;

        StockHistory today = crawled.get(0);

        if (!stockHistoryRepository.existsByCodeAndDate(today.getCode(), today.getTradeDate())) {
            stockHistoryRepository.save(today);
        }
    }
}



