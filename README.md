# Spring Batch 예제 프로젝트

## 📖 프로젝트 소개

이 프로젝트는 Spring Batch 프레임워크를 활용한 배치 처리 시스템의 다양한 구현 방법을 보여주는 예제입니다.
단일 스텝, 다중 스텝, Flow 기반 스텝, JPA를 활용한 데이터베이스 배치 처리, 다양한 Tasklet 구현 방법들을 포함하고 있습니다.

## 🛠 기술 스택

| 구분            | 버전                                                    |
| --------------- | ------------------------------------------------------- |
| Java            | 25 (Gradle Toolchain)                                   |
| Spring Boot     | 4.1.1                                                   |
| Spring Batch    | 6.0.x (`spring-boot-starter-batch-jdbc`)                |
| Spring Data JPA | Hibernate 7.4 / Jakarta Persistence                     |
| MySQL           | 8.4 (Docker), `com.mysql:mysql-connector-j`             |
| Lombok          | Spring Boot 관리 버전                                    |
| Gradle          | 9.8.1 (Gradle Wrapper)                                  |

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
│       ├── docker-compose.yml                   # Docker(MySQL 8.4) 설정
│       └── init.sql                             # DB/계정 생성, Spring Batch 6 메타 테이블, 샘플 데이터
```

## 🚀 시작하기

### 1. 사전 요구사항

-   JDK 25
    -   Gradle Toolchain 으로 Java 25 를 사용하므로, Gradle 을 실행하는 JDK 는 17 이상이면 됩니다.
    -   JDK 를 기본 경로가 아닌 곳에 설치했다면 `~/.gradle/gradle.properties` 에 경로를 등록합니다.
        ```properties
        org.gradle.java.installations.paths=C:/java/jdk-25.0.4.1
        ```
-   MySQL 8.x
-   Docker (선택사항)

### 2. 데이터베이스 설정

#### Option A: Docker 사용

```bash
cd src/main/resources
docker compose up -d
```

컨테이너 최초 기동 시 `init.sql` 이 실행되어 다음 항목이 생성됩니다.

-   `appdb` 데이터베이스와 `appuser` 계정 (비밀번호: `123456`)
-   Spring Batch 6 메타 테이블 (`BATCH_JOB_INSTANCE`, `BATCH_JOB_EXECUTION`, ... `BATCH_JOB_INSTANCE_SEQ`)
-   `member` 테이블과 JPA 예제용 샘플 데이터

> `init.sql` 은 데이터 볼륨이 비어 있을 때만 실행됩니다. 스키마를 다시 만들려면 `docker compose down -v` 후 재기동하세요.
>
> Spring Batch 4.x(Spring Boot 2.x) 시절의 `appdb` 를 그대로 사용하면 안 됩니다. 메타 테이블 구조가 변경되었기 때문입니다
> (`BATCH_JOB_SEQ` → `BATCH_JOB_INSTANCE_SEQ`, `BATCH_JOB_EXECUTION_PARAMS` 컬럼 변경, `JOB_CONFIGURATION_LOCATION` 제거 등).
> `init.sql` 은 `CREATE TABLE IF NOT EXISTS` 로 되어 있어 기존 테이블을 변경하지 않으므로, 기존 `BATCH_*` 테이블을 삭제한 후 다시 생성하세요.

#### Option B: 로컬 MySQL 사용

`src/main/resources/init.sql` 을 root 계정으로 실행합니다.

```bash
mysql -u root -p < src/main/resources/init.sql
```

### 3. 애플리케이션 실행

Spring Boot 3 부터는 Job 이 여러 개 등록되어 있으면 실행할 Job 이름을 반드시 지정해야 합니다.
`application.yml` 에서 `spring.batch.job.name: ${job.name:singleStepJob}` 으로 설정되어 있어 `--job.name` 으로 실행할 Job 을 선택하며, 지정하지 않으면 `singleStepJob` 이 실행됩니다.

```bash
# Gradle 을 사용한 실행
./gradlew bootRun --args='--job.name=jpaJob date=2026-10-17'

# 또는 JAR 파일 빌드 후 실행
./gradlew build
java -jar build/libs/spring-batch-0.0.1-SNAPSHOT.jar --job.name=jpaJob date=2026-10-17
```

-   `--` 로 시작하는 인자는 Spring 프로퍼티, `key=value` 형태의 인자는 Job Parameter 로 전달됩니다.
-   동일한 Job 을 동일한 Job Parameter 로 다시 실행하면 이미 완료된 JobInstance 이므로 실행되지 않습니다. 다시 실행하려면 Job Parameter 값을 변경하세요.

실행 가능한 Job 이름

| Job 이름                  | 설정 클래스                     |
| ------------------------- | ------------------------------- |
| `singleStepJob`           | `SingleStepJobConfig`           |
| `multipleStepJob`         | `MultipleStepJobConfig`         |
| `flowStepJob`             | `FlowStepJobConfig`             |
| `jpaJob`                  | `JpaJobConfig`                  |
| `lambdaTaskletJob`        | `LambdaTaskletJobConfig`        |
| `externalClassTaskletJob` | `ExternalClassTaskletJobConfig` |
| `methodInvokeTaskletJob`  | `MethodInvokeTaskletJobConfig`  |

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

-   `JpaPagingItemReader` → `ItemProcessor` → `JpaItemWriter` 로 구성된 Chunk 지향 처리
-   `amount >= 10000` 인 회원을 조회하여 1000원을 추가 적립
-   `@JobScope`, `@StepScope` 와 Job Parameter(`date`) Late Binding 예제

### 5. Tasklet 구현 방식

#### Lambda Tasklet (LambdaTaskletJobConfig)

-   람다 표현식을 활용한 간결한 태스클릿 구현

#### External Class Tasklet (ExternalClassTaskletJobConfig)

-   별도의 클래스로 분리된 태스클릿 구현 (`StepExecutionListener` 함께 구현)

#### Method Invoke Tasklet (MethodInvokeTaskletJobConfig)

-   `MethodInvokingTaskletAdapter` 로 기존 메소드를 호출하는 태스클릿 구현

## ⚙️ 설정

### application.yml 주요 설정

```yaml
spring:
    datasource:
        url: jdbc:mysql://localhost:3306/appdb?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=UTC
        username: appuser
        password: 123456

    batch:
        job:
            enabled: true # 애플리케이션 기동 시 배치 작업 자동 실행
            name: ${job.name:singleStepJob} # 실행할 Job 이름
        jdbc:
            initialize-schema: never # 메타 테이블은 init.sql 로 생성

    jpa:
        hibernate:
            ddl-auto: update
        properties:
            hibernate:
                show_sql: true # SQL 쿼리 로깅
```

## 🔧 개발 가이드

### 새로운 배치 작업 추가하기

Spring Batch 5 부터 `JobBuilderFactory`, `StepBuilderFactory` 가 제거되었습니다.
`JobBuilder`, `StepBuilder` 에 `JobRepository` 와 `PlatformTransactionManager` 를 직접 전달합니다.
또한 Spring Boot 환경에서는 `@EnableBatchProcessing` 을 선언하면 Batch 자동 설정이 비활성화되므로 사용하지 않습니다.

1. **Job Configuration 클래스 생성**

    ```java
    @Configuration
    public class MyJobConfig {

        @Bean
        public Job myJob(JobRepository jobRepository, Step myStep) {
            return new JobBuilder("myJob", jobRepository)
                    .start(myStep)
                    .build();
        }

        @Bean
        public Step myStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
            return new StepBuilder("myStep", jobRepository)
                    .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED, transactionManager)
                    .build();
        }
    }
    ```

2. **Chunk 지향 Step**

    Spring Batch 6 에서는 `chunk(int, PlatformTransactionManager)` 가 deprecated 되었습니다.

    ```java
    new StepBuilder("myChunkStep", jobRepository)
            .<Input, Output>chunk(10)
            .transactionManager(transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    ```

3. **필요한 경우 Entity 및 Repository 추가** (`jakarta.persistence` 패키지 사용)

    ```java
    @Entity
    public class MyEntity {
        // 엔티티 정의
    }
    ```

### Spring Batch 6 패키지 변경 사항

| 이전 (Batch 4.x)                                     | 현재 (Batch 6.x)                                             |
| ---------------------------------------------------- | ------------------------------------------------------------ |
| `org.springframework.batch.core.Job`                 | `org.springframework.batch.core.job.Job`                     |
| `org.springframework.batch.core.Step`                | `org.springframework.batch.core.step.Step`                   |
| `org.springframework.batch.core.StepContribution`    | `org.springframework.batch.core.step.StepContribution`       |
| `org.springframework.batch.core.StepExecution`       | `org.springframework.batch.core.step.StepExecution`          |
| `org.springframework.batch.core.StepExecutionListener` | `org.springframework.batch.core.listener.StepExecutionListener` |
| `org.springframework.batch.item.*`                   | `org.springframework.batch.infrastructure.item.*`            |
| `org.springframework.batch.repeat.RepeatStatus`      | `org.springframework.batch.infrastructure.repeat.RepeatStatus` |
| `javax.persistence.*`                                | `jakarta.persistence.*`                                      |

## 📝 로깅

-   SQL 쿼리 로깅이 활성화되어 있어 실행되는 쿼리를 콘솔에서 확인할 수 있습니다
-   Spring Batch 실행 로그를 통해 배치 작업의 진행 상황을 모니터링할 수 있습니다
