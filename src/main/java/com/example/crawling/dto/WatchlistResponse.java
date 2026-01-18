package com.example.crawling.dto;

import com.example.crawling.model.UserStock;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class WatchlistResponse {
    private Long id;
    private String stockCode;
    private String stockName;
    private LocalDateTime createdAt;

    public static WatchlistResponse from(UserStock userStock) {
        return new WatchlistResponse(
                userStock.getId(),
                userStock.getStock().getCode(),
                userStock.getStock().getName(),
                userStock.getAddAt()
        );
    }
}
