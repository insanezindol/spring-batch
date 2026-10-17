package com.example.springbatch.step;

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
public class SingleStepJobConfig {

    final JobRepository jobRepository;
    final PlatformTransactionManager transactionManager;

    public SingleStepJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job singleStepJob() {
        Job exampleJob = new JobBuilder("singleStepJob", jobRepository)
                .start(singleStep())
                .build();
        return exampleJob;
    }

    @Bean
    public Step singleStep() {
        return new StepBuilder("singleStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("Single Step!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }


}
