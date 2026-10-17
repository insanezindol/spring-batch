package com.example.springbatch.database;

import com.example.springbatch.entity.Member;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Slf4j
@Configuration
public class JpaJobConfig {

    final JobRepository jobRepository;
    final PlatformTransactionManager transactionManager;
    final EntityManagerFactory entityManagerFactory;

    public JpaJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, EntityManagerFactory entityManagerFactory) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public Job jpaJob() {
        Job exampleJob = new JobBuilder("jpaJob", jobRepository)
                .start(jpaJobStep())
                .build();
        return exampleJob;
    }

    // @JobScope Step 선언문에 사용 가능
    // @StepScope는 Step을 구성하는 ItemReader, ItemProcessor, ItemWriter에 사용 가능
    // @JobScope와 @StepScope는 Singleton 패턴이 아닌 Annotation이 명시된 메소드의 실행 시점에 Bean이 생성
    // @JobScope와 @StepScope Bean이 생성 될 때 JobParameter가 생성되기 때문에 JobParameter 사용하기 위해선 반드시 Scope를 지정해주어야 함
    // 이는 LateBinding을 하여 JobParameter를 비즈니스 로직 단계에서 할당하여 보다 유연한 설계를 가능하게 하고 서로 다른 Step이 서로를 침범하지 않고 병렬로 실행되게 하기 위함

    @Bean
    @JobScope
    public Step jpaJobStep() {
        return new StepBuilder("Step", jobRepository)
                .<Member, Member>chunk(10)
                .transactionManager(transactionManager)
                .reader(jpaReader(null))
                .processor(jpaProcessor(null))
                .writer(jpaWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Member> jpaReader(@Value("#{jobParameters[date]}")  String date) {

        log.info("jobParameters value : " + date);

        return new JpaPagingItemReaderBuilder<Member>()
                .pageSize(10)
                .parameterValues(Map.of("amount", 10000))
                .queryString("SELECT p FROM com.example.springbatch.entity.Member p WHERE p.amount >= :amount ORDER BY p.pid ASC")
                .entityManagerFactory(entityManagerFactory)
                .name("JpaPagingItemReader")
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Member, Member> jpaProcessor(@Value("#{jobParameters[date]}")  String date) {
        return member -> {
            log.info("jobParameters value : " + date);
            // 1000원 추가 적립
            member.setAmount(member.getAmount() + 1000);
            return member;
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<Member> jpaWriter(@Value("#{jobParameters[date]}")  String date) {

        log.info("jobParameters value : " + date);

        return new JpaItemWriterBuilder<Member>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }


}
