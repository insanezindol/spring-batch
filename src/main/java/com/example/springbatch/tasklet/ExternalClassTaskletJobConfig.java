package com.example.springbatch.tasklet;

import com.example.springbatch.service.BusinessTasklet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class ExternalClassTaskletJobConfig {

    final JobRepository jobRepository;
    final PlatformTransactionManager transactionManager;

    public ExternalClassTaskletJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job externalClassTaskletJob() {
        return new JobBuilder("externalClassTaskletJob", jobRepository)
                .start(externalClassTaskletStep())
                .build();
    }

    @Bean
    public Step externalClassTaskletStep() {
        return new StepBuilder("externalClassTaskletStep", jobRepository)
                .tasklet(new BusinessTasklet(), transactionManager)
                .build();
    }

}
