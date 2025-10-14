package com.example.crawling.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockResponseDto {
    private String name;
    private double price;
    private String code;
}
