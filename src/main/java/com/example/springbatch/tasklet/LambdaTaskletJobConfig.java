package com.example.springbatch.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class LambdaTaskletJobConfig {

    final JobRepository jobRepository;
    final PlatformTransactionManager transactionManager;

    public LambdaTaskletJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job lambdaTaskletJob() {
        return new JobBuilder("lambdaTaskletJob", jobRepository)
                .start(lambdaTaskletStep())
                .build();
    }

    @Bean
    public Step lambdaTaskletStep() {
        return new StepBuilder("lambdaTaskletStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    // 비즈니스 로직
                    for (int idx = 0; idx < 10; idx++) {
                        log.info("[idx] = " + idx);
                    }
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }

}
