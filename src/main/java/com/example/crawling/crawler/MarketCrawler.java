package com.example.crawling.crawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MarketCrawler {

    private static final String KOSPI_URL = "https://finance.naver.com/sise/sise_index.naver?code=KOSPI";
    private static final String NEWS_URL = "https://finance.naver.com/news/mainnews.naver";

    /** ✅ 코스피 지수 + 변동폭 가져오기 */
    public Map<String, Object> getKospiInfo() {
        Map<String, Object> kospiData = new HashMap<>();

        try {
            Document doc = Jsoup.connect(KOSPI_URL).get();

            // 현재 지수
            Element nowValue = doc.selectFirst("#now_value");

            // 변동폭(전일 대비)
            Element changeElement = doc.selectFirst("#change_value_and_rate");

            if (nowValue != null) {
                kospiData.put("current", Double.parseDouble(nowValue.text().replace(",", "")));
            }
            if (changeElement != null) {
                String changeText = changeElement.text().trim();
                kospiData.put("change", changeText);

                // 등락 방향 계산
                if (changeText.contains("▲") || changeText.contains("+")) {
                    kospiData.put("direction", "상승");
                } else if (changeText.contains("▼") || changeText.contains("-")) {
                    kospiData.put("direction", "하락");
                } else {
                    kospiData.put("direction", "보합");
                }
            }

        } catch (IOException e) {
            log.error("❌ 코스피 지수 크롤링 실패: {}", e.getMessage());
            kospiData.put("error", "크롤링 실패");
        }

        return kospiData;
    }

    /** ✅ 주요 뉴스 제목 5개 가져오기 */
    public List<String> getTopNews() {
        List<String> newsList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(NEWS_URL).get();
            for (Element item : doc.select(".newsList li a")) {
                String title = item.text();
                if (!title.isEmpty()) newsList.add(title);
                if (newsList.size() >= 5) break;
            }
        } catch (IOException e) {
            log.error("❌ 뉴스 크롤링 실패: {}", e.getMessage());
        }
        return newsList;
    }
}
