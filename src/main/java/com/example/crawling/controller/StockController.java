package com.example.crawling.controller;

import com.example.crawling.dto.CrawledPriceResponseDto;
import com.example.crawling.dto.StockResponseDto;
import com.example.crawling.model.CrawledPrice;
import com.example.crawling.model.Stock;
import com.example.crawling.model.StockHistory;
import com.example.crawling.model.User;
import com.example.crawling.repository.StockRepository;
import com.example.crawling.repository.UserRepository;
import com.example.crawling.service.StockService;
import com.example.crawling.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    // 크롤링
    @GetMapping
    public Map<String, StockResponseDto> getStocks(Principal principal) {
        // 해당 사용자의 관심 종목을 모두 불러와서 크롤링
        return stockService.getStockData(principal);
    }

    // 키워드 검색 조회
    @GetMapping("/search")
    public ResponseEntity<List<CrawledPriceResponseDto>> searchCrawledPrice(Principal principal, @RequestParam String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (keyword.length() > 50) {
            return ResponseEntity.badRequest().build();
        }

        List<CrawledPriceResponseDto> result = stockService.searchCrawledPriceByKeyword(keyword, principal.getName());
        return ResponseEntity.ok(result);
    }

    // 전체 조회
    @GetMapping("/allSearch")
    public ResponseEntity<List<CrawledPriceResponseDto>> getAllCrawledPrice(Principal principal) {
        return ResponseEntity.ok(stockService.getAllCrawledPrice(principal.getName()));
    }

    // 해당 코드의 모든 내용 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteByCode")
    public ResponseEntity<?> deleteStock(@RequestParam String stockCode, Principal principal) {
        if (!ValidationUtils.isValidStockCode(stockCode)) {
            return ResponseEntity.badRequest().body("유효하지 않은 종목코드입니다.");
        }
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("없는 사용자입니다."));

        //관리자 권한 확인
        if (!"ROLE_ADMIN".equals(user.getRole())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("관리자만 삭제가능합니다.");
        }

        stockService.deletePricesByStockCode(stockCode);
        return ResponseEntity.noContent().build();
    }


    // 종목 히스토리 조회
    @GetMapping("/history")
    public ResponseEntity<List<StockHistory>> getStockHistory(
            @RequestParam String code,
            @RequestParam String stockName
    ) {
        if (!ValidationUtils.isValidStockCode(code)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(stockService.getStockHistory(code, stockName));
    }

}
