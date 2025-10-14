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

    public Optional<Double> getPrice(String code) {
        String url = "https://finance.naver.com/item/main.naver?code=" + code;

        try {
            Document doc = Jsoup.connect(url).get();
            Element priceElement = doc.selectFirst(".no_today .blind");

            if (priceElement == null) {
                log.warn("가격 정보 없음: {}", code);
                return Optional.empty();
            }

            double price = Double.parseDouble(priceElement.text().replace(",", ""));
            return Optional.of(price);

        } catch (IOException e) {
            log.error("크롤링 실패: {}", e.getMessage());
            return Optional.empty();
        }
    }
}

