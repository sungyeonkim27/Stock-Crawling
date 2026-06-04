package com.example.crawling.repository;

import com.example.crawling.model.StockHistory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockHistoryRepositoryCustom {
    List<StockHistory> findHistoryByCode(String code);
    Optional<StockHistory> findLatestByCode(String code);
    boolean existsByCodeAndDate(String code, LocalDate date);
}
