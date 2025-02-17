# BidMarkit

## 📑 프로젝트 소개

이베이, 야후 옥션 등을 참조하여 경매 시스템을 개발하는 프로젝트입니다.

이 프로젝트는 대규모 트래픽을 처리할 수 있는 서버를 직접 구현해보고 이해하는 것을 목표로 합니다.

이를 위해 동시성 처리, 부하 분산, 데이터 정합성 유지 등이 중요한 경매 시스템을 주제로 선정하였습니다.

## 📆 개발 기간
- \`24.03.06. ~ \`24.06.19. (약 4달)

## 📚 기술 스택

### 프론트엔드

> **Framework**

<img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=black"/> <img src="https://img.shields.io/badge/React Query-FF4154?style=for-the-badge&logo=reactquery&logoColor=black"/>

> **Deployment**

<img src="https://img.shields.io/badge/PWA-5A0FC8?style=for-the-badge&logo=pwa&logoColor=white"/> <img src="https://img.shields.io/badge/Firebase-DD2C00?style=for-the-badge&logo=firebase&logoColor=white"/><br>

### 백엔드

> **Framework**

<img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/> <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"/>

> **Database**

<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/> <img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white"/> **(Cache, Message Queue)**

> **Deployment**

<img src="https://img.shields.io/badge/GCP-4285F4?style=for-the-badge&logo=googlecloud&logoColor=white"/> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"/> <img src="https://img.shields.io/badge/kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white"/>

> **Etc.**

<img src="https://img.shields.io/badge/elasticstack-005571?style=for-the-badge&logo=elasticstack&logoColor=white"/>

## ⚙️ 시스템 아키텍처

![image](https://github.com/user-attachments/assets/57fcddb6-e3a7-4e0d-b28b-8b9afec04466)

## 🖥️ 주요 화면

| ![image](https://github.com/user-attachments/assets/bd7974c9-048a-456f-bd58-90ccdcbd255e) | ![image](https://github.com/user-attachments/assets/50688b0f-a0cb-4345-9fa0-3ea5d4e9eab4) |
| :--: | :--: |
| 메인 페이지 | 상품명 검색어 추천 |
| ![image](https://github.com/user-attachments/assets/3fc15f31-cc5e-4cf2-a0b8-80ede8bd83c1) | ![image](https://github.com/user-attachments/assets/63963b6c-683c-4318-b096-1b7fb217d9ec) |
| 실시간 채팅 | 상품 등록 |

## 📦 주요 기능

### 📌 사용자 관련

- 회원 가입 및 탈퇴
- 로그인 및 로그아웃
- 회원정보 조회 및 수정
- 구매, 판매 내역 조회

### 📌 채팅 관련

- 채팅방 목록 조회
- 메시지 수신, 발신

### 📌 상품 관련

- 상품 등록, 수정, 삭제
- 상품명 검색
- 상품명 검색어 추천
- 상품 입찰 및 즉시 구매
