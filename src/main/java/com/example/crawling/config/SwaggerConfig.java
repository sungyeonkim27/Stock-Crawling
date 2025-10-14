package com.example.crawling.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI stockApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Stock Crawling API")
                        .version("v1.0")
                        .description("네이버 증권 크롤링 기반 주식 정보 조회 API")
                );
    }
}
