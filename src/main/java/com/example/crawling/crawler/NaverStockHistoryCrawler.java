package com.example.crawling.crawler;

import com.example.crawling.exception.CrawlerException;
import com.example.crawling.model.StockHistory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;   // ← javax.swing 아닌 jsoup
import org.jsoup.nodes.Element;    // ← javax.swing 아닌 jsoup
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class NaverStockHistoryCrawler {

    public List<StockHistory> crawlHistory(String code, String stockName, int count) {
        List<StockHistory> result = new ArrayList<>();
        String url = "https://fchart.stock.naver.com/sise.nhn?symbol="
                + code + "&timeframe=day&count=" + count + "&requestType=0";
        try {
            Document doc = Jsoup.connect(url)
                    .parser(Parser.xmlParser())
                    .get();

            Elements items = doc.select("item");

            for (Element item : items) {
                String dataAttr = item.attr("data");
                String[] parts = dataAttr.split("\\|");

                if (parts.length < 5) continue;

                LocalDate tradeDate = LocalDate.parse(parts[0], DateTimeFormatter.ofPattern("yyyyMMdd"));
                long closePrice = Long.parseLong(parts[4]);

                StockHistory history = new StockHistory();
                history.setCode(code);
                history.setStockName(stockName);
                history.setTradeDate(tradeDate);
                history.setClosePrice(closePrice);
                result.add(history);
            }
        } catch (Exception e) {
            throw new CrawlerException("종가 크롤링 실패:" + code);  // 추가
        }
        return result;
    }
}