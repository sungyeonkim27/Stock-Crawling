package com.example.crawling.repository;

import com.example.crawling.model.CrawledPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrawledPriceRepository extends JpaRepository<CrawledPrice, Long> {
    List<CrawledPrice> findByCode(String code);
}
