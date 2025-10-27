package com.example.crawling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CrawledPriceResponseDto {
    private String code;
    private String name;
    private double price;
    private LocalDateTime time;
}
