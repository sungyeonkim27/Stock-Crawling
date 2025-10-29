package com.example.crawling.repository;

import com.example.crawling.model.CrawledPrice;
import com.example.crawling.model.MarketNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketNewsRepository extends JpaRepository<MarketNews, Long> {

    List<MarketNews> findByTitleContaining(String keyword);
    List<MarketNews> findAllByOrderByIdDesc();
}
