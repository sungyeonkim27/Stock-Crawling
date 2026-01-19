package com.example.crawling.dto;

import com.example.crawling.model.CrawledPrice;
import com.example.crawling.model.Stock;
import com.example.crawling.repository.StockRepository;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrawledPriceResponseDto {
    private String code;
    private String name;
    private double price;
    private LocalDateTime time;


    public CrawledPriceResponseDto(CrawledPrice crawledPrice, String stockName) {
        this.code = crawledPrice.getCode();
        this.name = stockName;
        this.price = crawledPrice.getPrice();
        this.time = crawledPrice.getTime();
    }
}
