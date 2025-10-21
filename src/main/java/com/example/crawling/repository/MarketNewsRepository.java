package com.example.crawling.repository;

import com.example.crawling.model.MarketNews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketNewsRepository extends JpaRepository<MarketNews, Long> {
}
