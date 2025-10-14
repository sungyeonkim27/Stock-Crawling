package com.example.crawling.config;

import com.example.crawling.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    private final StockService stockService;

    @Scheduled(fixedDelay = 300_000) // 5분마다 실행
    public void scheduledCrawling() {
        var data = stockService.getStockData();
        log.info("스케줄링된 주가 업데이트 완료: {}", data);
    }
}
