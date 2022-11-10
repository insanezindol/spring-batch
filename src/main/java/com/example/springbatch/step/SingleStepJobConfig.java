package com.example.springbatch.step;

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
public class SingleStepJobConfig {

    final JobBuilderFactory jobBuilderFactory;
    final StepBuilderFactory stepBuilderFactory;

    public SingleStepJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job singleStepJob() {
        Job exampleJob = jobBuilderFactory.get("singleStepJob")
                .start(singleStep())
                .build();
        return exampleJob;
    }

    @Bean
    public Step singleStep() {
        return stepBuilderFactory.get("singleStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Single Step!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


}
