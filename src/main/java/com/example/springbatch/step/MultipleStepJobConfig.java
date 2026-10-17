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
public class MultipleStepJobConfig {

    final JobRepository jobRepository;
    final PlatformTransactionManager transactionManager;

    public MultipleStepJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job multipleStepJob(){
        Job exampleJob = new JobBuilder("multipleStepJob", jobRepository)
                .start(multipleStartStep())
                .next(multipleNextStep())
                .next(multipleLastStep())
                .build();
        return exampleJob;
    }

    @Bean
    public Step multipleStartStep() {
        return new StepBuilder("multipleStartStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("Start Step!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step multipleNextStep(){
        return new StepBuilder("multipleNextStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("Next Step!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step multipleLastStep(){
        return new StepBuilder("multipleLastStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("Last Step!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }


}
