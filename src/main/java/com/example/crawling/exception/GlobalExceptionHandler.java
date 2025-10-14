package com.example.crawling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CrawlerException.class)
    public ResponseEntity<String> handleCrawlerException(CrawlerException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("크롤링 오류: " + ex.getMessage());
    }
}
