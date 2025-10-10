package com.example.crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class StockController {

    @GetMapping("/stocks")
    public Map<String, Object> getMultipleStocks() throws IOException {
        // ✅ 관심 있는 종목 코드 (네이버 증권 주소에 표시되는 숫자)
        Map<String, String> stockCodes = Map.of(
                "삼성전자", "005930",
                "SK하이닉스", "000660",
                "NAVER", "035420",
                "카카오", "035720",
                "LG에너지솔루션", "373220"
        );

        Map<String, Object> stockData = new HashMap<>();

        // ✅ 각 종목별로 크롤링 수행
        for (Map.Entry<String, String> entry : stockCodes.entrySet()) {
            String name = entry.getKey();
            String code = entry.getValue();

            String url = "https://finance.naver.com/item/main.naver?code=" + code;
            Document doc = Jsoup.connect(url).get();

            // 📈 현재가
            Element priceElement = doc.selectFirst(".no_today .blind");
            String price = priceElement != null ? priceElement.text() : "데이터 없음";

            // 📊 전일 대비 변동 (상승/하락)
            Element changeElement = doc.selectFirst(".no_exday span.blind");
            String change = changeElement != null ? changeElement.text() : "변동 없음";

            // ✅ Map 형태로 정리
            Map<String, String> info = new HashMap<>();
            info.put("code", code);
            info.put("price", price);
            info.put("change", change);

            stockData.put(name, info);
        }

        return stockData;
    }
}
