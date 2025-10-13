package com.example.crawling.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String homePage() {
        // templates 폴더의 main.html 파일 렌더링
        return "main";
    }
}
