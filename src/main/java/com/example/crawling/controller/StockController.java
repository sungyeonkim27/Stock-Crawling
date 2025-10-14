package com.example.crawling.controller;

import com.example.crawling.dto.StockResponseDto;
import com.example.crawling.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    @GetMapping
    public Map<String, StockResponseDto> getStocks() {
        return stockService.getStockData();
    }
}
