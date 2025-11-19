package com.example.crawling.repository;

import com.example.crawling.model.MarketIndex;
import com.example.crawling.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
