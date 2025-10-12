# 📈 Stock Crawling Project

> 네이버 증권 데이터를 Jsoup으로 크롤링하여 Spring Boot API로 제공하는 프로젝트입니다.

---

## 🚀 주요 기능

| 기능 | 설명 |
|------|------|
| `/summary` | 코스피 지수 및 주요 뉴스 요약 |
| `/stocks` | 삼성전자, NAVER, 카카오 등 주요 종목의 현재가/변동률 조회 |

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
| 엔드포인트      | 설명          | 예시                                                             |
| ---------- | ----------- | -------------------------------------------------------------- |
| `/summary` | 코스피 지수 및 뉴스 | [http://localhost:8080/summary](http://localhost:8080/summary) |
| `/stocks`  | 주요 종목 주가 정보 | [http://localhost:8080/stocks](http://localhost:8080/stocks)   |


