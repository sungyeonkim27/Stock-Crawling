# 📈 Stock Crawling Project

> 네이버 증권 데이터를 Jsoup을 활용해 크롤링하고, Spring Boot 기반의 Web & REST API로 제공하는 실시간 금융 데이터 서비스입니다.
관리자는 웹 UI를 통해 데이터 수집/조회/삭제 가능하며, CI/CD 및 systemd 기반 자동 배포 환경까지 구축되어 있습니다.
---
# 배포 주소 
[Stock Crawling Dashboard](http://158.247.207.161:6100/)

## 🚀 주요 기능

### 1. 웹 UI 대시보드

| 기능              | 설명                    |
| --------------- | --------------------- |
| 🔑 로그인/로그아웃     | Spring Security 기반 인증 |
| 📈 주가 크롤링       | 주요 종목 데이터 수집          |
| 💹 코스피 & 뉴스 크롤링 | 코스피 지수 및 관련 뉴스 수집     |
| 🔍 검색 기능        | 종목명/코드 및 뉴스 검색        |
| 🗂 데이터 조회       | 저장 데이터 실시간 조회         |
| ❌ 관리자 데이터 삭제    | 종목 삭제 및 코스피/뉴스 전체 삭제  |

### 2. API 엔드포인트
| HTTP Method | 엔드포인트                             | 설명                  |
| ----------- | --------------------------------- | ------------------- |
| `GET`       | `/api/market/summary`             | 코스피 지수 및 주요 뉴스 요약   |
| `GET`       | `/api/market/search?keyword=삼성`   | 뉴스 검색               |
| `GET`       | `/api/market/allSearch`           | 전체 코스피 & 뉴스 조회      |
| `GET`       | `/api/stocks`                     | 주요 종목 주가 조회         |
| `GET`       | `/api/stocks/search?keyword=삼성전자` | 종목명/코드 검색           |
| `GET`       | `/api/stocks/allSearch`           | 전체 주가 정보            |
| `DELETE`    | `/api/stocks/search?code=005930`  | 종목 삭제 *(관리자)*       |
| `DELETE`    | `/api/market/delete`              | 코스피 & 뉴스 삭제 *(관리자)* |

---

## ⚙️ 기술 스택

- **Language:** Java 17
- **Framework:** Spring Boot 3.3.x
- **Build Tool:** Maven
- **Library:** Jsoup (HTML Parser)
- **Version Control:** Git & GitHub
- **Web Template:** Thymeleaf
- **Security:** Spring Security (로그인/로그아웃)
- **HTML 파싱:** Jsoup
- **DB:** PostgreSQL
- **Deployment:** Ubuntu + systemctl 서비스 관리
- **CI/CD:** GitHub Actions + SSH 자동 배포

---

## 🧪 CI/CD & 자동 배포 구조

- main 브랜치 push 시 GitHub Actions 실행
- EC2 서버 접속 후 git reset --hard & Maven 빌드
- systemctl restart crawling으로 서비스 재시작
- Spring Boot 애플리케이션 자동 적용
