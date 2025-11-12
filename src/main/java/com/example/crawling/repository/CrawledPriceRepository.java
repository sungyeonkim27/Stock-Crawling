package com.example.crawling.repository;

import com.example.crawling.model.CrawledPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrawledPriceRepository extends JpaRepository<CrawledPrice, Long> {
    List<CrawledPrice> findByCode(String code);

    List<CrawledPrice> findByCodeContaining(String keyword);
    List<CrawledPrice> findAllByOrderByIdDesc();

    List<CrawledPrice> findByCodeIn(List<String> matchingCodes);

    void deleteByCode(String code);
}

