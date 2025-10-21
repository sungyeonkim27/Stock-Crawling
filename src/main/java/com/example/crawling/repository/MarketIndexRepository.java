package com.example.crawling.repository;

import com.example.crawling.model.MarketIndex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.InterfaceAddress;

public interface MarketIndexRepository extends JpaRepository<MarketIndex, Long> {

}
