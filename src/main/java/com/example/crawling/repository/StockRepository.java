package com.example.crawling.repository;

import com.example.crawling.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByCode(String code);

    List<Stock> findByNameContaining(String keyword);
    List<Stock> findAllByOrderByIdDesc();


}
