package com.example.crawling.crawler;

import com.example.crawling.model.StockHistory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class NaverStockHistoryCrawlerTest {

    @Autowired
    private NaverStockHistoryCrawler crawler;

    @Test
    void 삼성전자_종가_크롤링() {
        List<StockHistory> result = crawler.crawlHistory("005930", "삼성전자", 90);

        log.info("총 데이터 수: {}", result.size());
        result.forEach(h ->
                log.info("{} | {}", h.getTradeDate(), h.getClosePrice())
        );

        assert !result.isEmpty() : "크롤링 결과가 비어있음";
    }
}
