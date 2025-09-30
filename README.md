# Spring Batch 예제 프로젝트

## 📖 프로젝트 소개

이 프로젝트는 Spring Batch 프레임워크를 활용한 배치 처리 시스템의 다양한 구현 방법을 보여주는 예제입니다.
단일 스텝, 다중 스텝, Flow 기반 스텝, JPA를 활용한 데이터베이스 배치 처리, 다양한 Tasklet 구현 방법들을 포함하고 있습니다.

## 🛠 기술 스택

-   **Java**: 11
-   **Spring Boot**: 2.7.5
-   **Spring Batch**: Spring Boot Starter Batch
-   **Spring Data JPA**: 데이터베이스 연동
-   **MySQL**: 데이터베이스
-   **Lombok**: 코드 간소화
-   **Gradle**: 빌드 도구

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── java/com/example/springbatch/
│   │   ├── SpringBatchApplication.java          # 메인 애플리케이션
│   │   ├── database/
│   │   │   └── JpaJobConfig.java                # JPA 기반 배치 작업
│   │   ├── entity/
│   │   │   └── Member.java                      # 멤버 엔티티
│   │   ├── repository/
│   │   │   └── MemberRepository.java            # 멤버 리포지토리
│   │   ├── service/
│   │   │   ├── BusinessTasklet.java             # 비즈니스 로직 태스클릿
│   │   │   └── CustomService.java               # 커스텀 서비스
│   │   ├── step/
│   │   │   ├── FlowStepJobConfig.java           # Flow 기반 스텝 설정
│   │   │   ├── MultipleStepJobConfig.java       # 다중 스텝 작업 설정
│   │   │   └── SingleStepJobConfig.java         # 단일 스텝 작업 설정
│   │   └── tasklet/
│   │       ├── ExternalClassTaskletJobConfig.java    # 외부 클래스 태스클릿
│   │       ├── LambdaTaskletJobConfig.java           # 람다 태스클릿
│   │       └── MethodInvokeTaskletJobConfig.java     # 메소드 호출 태스클릿
│   └── resources/
│       ├── application.yml                      # 애플리케이션 설정
│       ├── docker-compose.yml                   # Docker 설정
│       └── init.sql                            # 초기 데이터베이스 스키마
```

## 🚀 시작하기

### 1. 사전 요구사항

-   Java 11 이상
-   MySQL 데이터베이스
-   Docker (선택사항)

### 2. 데이터베이스 설정

#### Option A: Docker 사용

```bash
# Docker Compose로 MySQL 실행
docker-compose up -d
```

#### Option B: 로컬 MySQL 사용

-   MySQL 서버 실행
-   데이터베이스 생성: `appdb`
-   사용자 생성: `appuser` (비밀번호: `123456`)

```sql
CREATE DATABASE appdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'appuser'@'localhost' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON appdb.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;
```

### 3. 애플리케이션 실행

```bash
# Gradle을 사용한 실행
./gradlew bootRun

# 또는 JAR 파일 빌드 후 실행
./gradlew build
java -jar build/libs/spring-batch-0.0.1-SNAPSHOT.jar
```

## 📋 배치 작업 유형

### 1. 단일 스텝 작업 (SingleStepJobConfig)

-   하나의 스텝으로 구성된 간단한 배치 작업
-   기본적인 배치 처리 패턴 학습

### 2. 다중 스텝 작업 (MultipleStepJobConfig)

-   여러 스텝을 순차적으로 실행하는 배치 작업
-   복잡한 비즈니스 프로세스 처리

### 3. Flow 기반 스텝 (FlowStepJobConfig)

-   조건부 실행과 분기 처리가 가능한 배치 작업
-   동적 워크플로우 구현

### 4. JPA 데이터베이스 작업 (JpaJobConfig)

-   JPA를 활용한 데이터베이스 배치 처리
-   대용량 데이터 읽기/쓰기 최적화

### 5. Tasklet 구현 방식

#### Lambda Tasklet (LambdaTaskletJobConfig)

-   람다 표현식을 활용한 간결한 태스클릿 구현

#### External Class Tasklet (ExternalClassTaskletJobConfig)

-   별도의 클래스로 분리된 태스클릿 구현

#### Method Invoke Tasklet (MethodInvokeTaskletJobConfig)

-   기존 메소드를 호출하는 태스클릿 구현

## ⚙️ 설정

### application.yml 주요 설정

```yaml
spring:
    datasource:
        url: jdbc:mysql://localhost:3306/appdb
        username: appuser
        password: 123456

    batch:
        job:
            enabled: true # 배치 작업 자동 실행 활성화

    jpa:
        hibernate:
            ddl-auto: validate # 스키마 검증 모드
        show-sql: true # SQL 쿼리 로깅
```

## 🔧 개발 가이드

### 새로운 배치 작업 추가하기

1. **Job Configuration 클래스 생성**

    ```java
    @Configuration
    @EnableBatchProcessing
    public class MyJobConfig {
        // Job 및 Step 정의
    }
    ```

2. **필요한 경우 Entity 및 Repository 추가**

    ```java
    @Entity
    public class MyEntity {
        // 엔티티 정의
    }
    ```

3. **비즈니스 로직 구현**
    ```java
    @Component
    public class MyTasklet implements Tasklet {
        // 태스클릿 로직 구현
    }
    ```

## 📝 로깅

-   SQL 쿼리 로깅이 활성화되어 있어 실행되는 쿼리를 콘솔에서 확인할 수 있습니다
-   Spring Batch 실행 로그를 통해 배치 작업의 진행 상황을 모니터링할 수 있습니다
