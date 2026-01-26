package com.example.crawling.controller;

import com.example.crawling.crawler.NaverStockCrawler;
import com.example.crawling.dto.WatchlistResponse;
import com.example.crawling.model.Stock;
import com.example.crawling.model.User;
import com.example.crawling.model.UserStock;
import com.example.crawling.repository.StockRepository;
import com.example.crawling.repository.UserRepository;
import com.example.crawling.repository.UserStockRepository;
import com.example.crawling.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/watchlist")
public class WatchlistController {

    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final UserStockRepository userStockRepository;
    private final NaverStockCrawler naverStockCrawler;

    // 관심 종목 조회
    @GetMapping
    public List<WatchlistResponse> getAllWatchlist(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("없는 사용자입니다."));
        // UserStock을 가져와서 맵핑
        List<UserStock> userStocks = userStockRepository.findByUser(user);
        List<WatchlistResponse> watchlistResponses = userStocks
                .stream()
                .map(WatchlistResponse::from).toList();
        return watchlistResponses;
    }

    // 관심 종목 추가
    @PostMapping
    public ResponseEntity<?> addWatchlist(Principal principal, @RequestParam String stockCode) {
        if (!ValidationUtils.isValidStockCode(stockCode)) {
            return ResponseEntity.badRequest().body("유효하지 않은 종목코드입니다.");
        }

        // 1. 사용자 검증
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("없는 사용자입니다."));
        // 2. 주식 검증(이미 크롤링 했던 주식이면 저장된 정보를 사용하고 아니면 새로 크롤링해서 정보를 가져옴)
        Stock stock = stockRepository.findByCode(stockCode).orElseGet(() -> {
            // 새로 크롤링해서 가져오기
            return naverStockCrawler.getStockInfo(stockCode)
                    .map(info -> {
                        Stock newStock = new Stock();
                        newStock.setName(info.stockName());
                        newStock.setCode(stockCode);
                        return stockRepository.save(newStock);
                    }).orElseThrow(() -> new RuntimeException("종목이 존재하지 않습니다."))
                    ;
        });

        // 3. 관심 종목 중복 검증
        if (userStockRepository.existsByUserIdAndStockId(user.getId(), stock.getId())) {
            return ResponseEntity.badRequest().body("이미 관심 종목에 등록되어 있습니다.");
        }
        // 4. UserStock 생성 및 저장
        UserStock userStock = new UserStock();
        userStock.setUser(user);
        userStock.setStock(stock);
        userStock.setAddAt(LocalDateTime.now());
        userStockRepository.save(userStock);

        return ResponseEntity.ok("관심 종목이 추가되었습니다.");
    }

    // 해당 관심 종목 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteWatchlist(Principal principal, @RequestParam String stockCode) {

        if (!ValidationUtils.isValidStockCode(stockCode)) {
            return ResponseEntity.badRequest().body("유효하지 않은 종목코드입니다.");
        }

        // 1. 사용자 검증
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("없는 사용자입니다."));
        // 2. 주식 검증
        Stock stock = stockRepository.findByCode(stockCode).orElseThrow(() -> new RuntimeException("없는 종목입니다."));
        // 3. 관심 종목 삭제
        userStockRepository.deleteByUserIdAndStockId(user.getId(), stock.getId());

        return ResponseEntity.ok("관심종목이 삭제되었습니다.");
    }

}
