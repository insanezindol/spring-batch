package com.example.springbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Spring Boot 3+ 에서는 @EnableBatchProcessing 을 선언하면 Batch 자동 설정(JobRepository, Job 자동 실행 등)이 비활성화되므로 사용하지 않는다.
@SpringBootApplication
public class SpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

}
