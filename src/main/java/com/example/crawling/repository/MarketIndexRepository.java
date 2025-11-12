package com.example.crawling.repository;

import com.example.crawling.model.MarketIndex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketIndexRepository extends JpaRepository<MarketIndex, Long> {

    List<MarketIndex> findAllByOrderByCrawledAtDesc();
    void deleteAll();
}
