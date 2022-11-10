package com.example.springbatch.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class LambdaTaskletJobConfig {

    final JobBuilderFactory jobBuilderFactory;
    final StepBuilderFactory stepBuilderFactory;

    public LambdaTaskletJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job lambdaTaskletJob() {
        return jobBuilderFactory.get("lambdaTaskletJob")
                .start(lambdaTaskletStep())
                .build();
    }

    @Bean
    public Step lambdaTaskletStep() {
        return stepBuilderFactory.get("lambdaTaskletStep")
                .tasklet((contribution, chunkContext) -> {
                    // 비즈니스 로직
                    for (int idx = 0; idx < 10; idx++) {
                        log.info("[idx] = " + idx);
                    }
                    return RepeatStatus.FINISHED;
                }).build();
    }

}
