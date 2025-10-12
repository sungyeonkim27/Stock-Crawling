package com.example.crawling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrawlingApplication.class, args);
		System.out.println("✅ 서버 실행 중: http://localhost:8080/summary");
		System.out.println("✅ 서버 실행 중: http://localhost:8080/stocks");

	}

}
