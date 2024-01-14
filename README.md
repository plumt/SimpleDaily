# SimpleDaily

---

# 출근길 및 짧은 시간 동안 날씨와 뉴스, 인기 검색어, 환율 등의 정보를 볼 수 있는 안드로이드 앱

기간 2022.04.15 ~

---

- 날씨 핫 뉴스
- 인기 검색어
- 환율

---

- Android Gradle Plugin Version 4.1.2
- Gradle Version 6.5
- koin Version 2.1.6
- Kotlin Version 1.4.31

---


# 1. API

## 인기 검색어 순위 및 핫 뉴스 API


BASE URL : https://api.signal.bz/news/realtime

별도 기한 및 키가 존재 하지 않는다.

Retrofit2 방식 사용




## 날씨 API


BASE URL : https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=

BASE IMAGE URL : https://ssl.pstatic.net/sstatic/keypage/outside/scui/weather_new_new/img/weather_svg/

별도 기한 및 키가 존재 하지 않는다.

크롤링 방식 사용




## 우리나라 지역 주소 API


BASE URL : https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app

Retrofit2 방식 사용





---

## 2. 라이브러리
