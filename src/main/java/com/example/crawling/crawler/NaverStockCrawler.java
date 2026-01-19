package com.example.crawling.crawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class NaverStockCrawler {

    public record StockInfo(String stockName, Double price) {}

    // 네이버 금융에서 해당 코드의 종목이름과 가격을 가져오기
    public Optional<StockInfo> getStockInfo(String code) {
        String url = "https://finance.naver.com/item/main.naver?code=" + code;

        try {
            Document doc = Jsoup.connect(url).get();
            Element priceElement = doc.selectFirst(".no_today .blind");
            Element nameElement = doc.selectFirst(".wrap_company h2 a");

            if (priceElement == null || nameElement == null) {
                log.warn("종목 정보 없음: {}", code);
                return Optional.empty();
            }

            double price = Double.parseDouble(priceElement.text().replace(",", ""));
            String name = nameElement.text().replace(",", "");
            return Optional.of(new StockInfo(name, price));

        } catch (IOException e) {
            log.error("크롤링 실패: {}", e.getMessage());
            return Optional.empty();
        }
    }
}

