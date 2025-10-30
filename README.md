# 📈 Stock Crawling Project

> 네이버 증권 데이터를 Jsoup으로 크롤링하여 Spring Boot API로 제공하는 프로젝트입니다.

---

## 🚀 주요 기능

| 엔드포인트                   | 설명                                          |
|-------------------------|---------------------------------------------|
| `/api/market/summary`   | 코스피 지수 및 주요 뉴스 요약 조회                        |
| `/api/market/search`    | 키워드로 뉴스 제목 검색                                 |
| `/api/market/allSearch` | 크롤링된 전체 코스피 지수 및 뉴스 조회                    |
| `/api/stocks`           | 주요 종목(삼성전자, NAVER, 카카오 등)의 현재가/변동률 조회         |
| `/api/stocks/search`    | 종목명 또는 코드로 크롤링된 주가 검색                     |
| `/api/stocks/allSearch` | 크롤링된 전체 주가 정보 조회                              |

---

## ⚙️ 기술 스택

- **Language:** Java 17
- **Framework:** Spring Boot 3.3.x
- **Build Tool:** Maven
- **Library:** Jsoup (HTML Parser)
- **Version Control:** Git & GitHub

---

## 💻 실행 방법

### 1️⃣ 클론
```bash
git clone https://github.com/sungyeonkim27/Stock-Crawling.git
cd Stock-Crawling
```
### 2️⃣ API 테스트 
| 엔드포인트                   | 설명                | 예시 URL                                                                                               |
|-------------------------| ----------------- |------------------------------------------------------------------------------------------------------|
| `/api/market/summary`   | 코스피 지수 및 뉴스 요약 조회 | [http://localhost:6100/api/market/summary](http://localhost:6100/market/summary)                     |
| `/api/market/search`    | 뉴스 키워드 검색         | [http://localhost:6100/api/market/search?keyword=삼성](http://localhost:6100/market/search?keyword=삼성) |
| `/api/market/allSearch` | 전체 코스피 및 뉴스 조회    | [http://localhost:6100/api/market/allSearch](http://localhost:6100/market/allSearch)                 |
| `/api/stocks`           | 주요 종목 주가 크롤링 실행   | [http://localhost:6100/api/stocks](http://localhost:6100/stocks)                                     |
| `/api/stocks/search`    | 종목명/코드로 주가 검색     | [http://localhost:6100/api/stocks/search?keyword=삼성](http://localhost:6100/stocks/search?keyword=삼성) |
| `/api/stocks/allSearch` | 전체 주가 정보 조회       | [http://localhost:6100/api/stocks/allSearch](http://localhost:6100/stocks/allSearch)                 |
