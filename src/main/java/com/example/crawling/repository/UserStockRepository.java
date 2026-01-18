package com.example.crawling.repository;


import com.example.crawling.model.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStockRepository extends JpaRepository<UserStock, Long> {


    List<UserStock> findByUserId(Long userId);

    Boolean existsByUserIdAndStockId(Long UserId, Long StockId);

    Optional<UserStock> findByUserIdAndStockId(Long UserId, Long StockId);

    void deleteByUserIdAndStockId(Long UserId, Long StockId);
}

