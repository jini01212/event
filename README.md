# Event Platform

간단하고 효율적인 소규모 이벤트 예매 플랫폼입니다.

## 주요 기능

### 사용자 기능
- 이벤트 목록 조회 및 상세보기
- 온라인 예매 (개인정보 입력)
- 3가지 결제 방식 지원 (토스페이/카카오페이/계좌이체)
- 고유 티켓번호 발급 및 이메일 발송
- 마이페이지에서 예매 내역 조회
- 현장 입장 시 티켓번호로 확인

### 관리자 기능
- 이벤트 등록/수정/관리
- 이벤트 이미지 업로드
- 예매 현황 조회 및 관리
- 결제 승인/거절 처리
- 현장 입장 처리 시스템
- 실시간 통계 대시보드

## 기술 스택

- **Backend**: Spring Boot 3.x, Spring Data JPA, Spring Security
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript
- **Database**: MySQL 8.x
- **Build Tool**: Gradle
- **Email**: Spring Mail (SMTP)

## 설치 및 실행

### 1. 사전 요구사항
- Java 17 이상
- MySQL 8.0 이상
- Gradle 7.x 이상

### 2. 데이터베이스 설정
```sql
CREATE DATABASE event_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 설정 파일 수정
`src/main/resources/application.properties` 파일에서 다음 항목들을 수정하세요:

```properties
# 데이터베이스 정보
spring.datasource.username=username
spring.datasource.password=password

# 이메일 설정
spring.mail.username=your_email@gmail.com
spring.mail.password=password

# 결제 계좌 정보
payment.bank.account=123-456-789012
payment.bank.holder=홍길동
```

### 4. 프로젝트 실행
```bash
./gradlew bootRun
```

### 5. 접속 확인
- 사용자 페이지: http://localhost:8080
- 관리자 페이지: http://localhost:8080/admin
- 관리자 계정: admin / admin123

## 사용 가이드

### 사용자 이용 방법
1. 메인 페이지에서 원하는 이벤트 선택
2. 이벤트 상세 페이지에서 예매 정보 입력
3. 결제 방식 선택 후 결제 진행
4. 이메일로 전송된 티켓번호 확인
5. 현장에서 티켓번호 제시하여 입장

### 관리자 이용 방법
1. `/admin`으로 접속하여 로그인
2. 대시보드에서 전체 현황 확인
3. 새 이벤트 등록 또는 기존 이벤트 수정
4. 예매 관리에서 결제 승인/거절 처리
5. 입장 처리 페이지에서 현장 입장 관리

## 🔐 보안 설정

### 기본 관리자 계정
- 사용자명: `admin`
- 비밀번호: `admin123`
