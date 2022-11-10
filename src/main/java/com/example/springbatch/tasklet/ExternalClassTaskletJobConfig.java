package com.example.springbatch.tasklet;

import com.example.springbatch.service.BusinessTasklet;
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
public class ExternalClassTaskletJobConfig {

    final JobBuilderFactory jobBuilderFactory;
    final StepBuilderFactory stepBuilderFactory;

    public ExternalClassTaskletJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job externalClassTaskletJob() {
        return jobBuilderFactory.get("externalClassTaskletJob")
                .start(externalClassTaskletStep())
                .build();
    }

    @Bean
    public Step externalClassTaskletStep() {
        return stepBuilderFactory.get("externalClassTaskletStep")
                .tasklet(new BusinessTasklet())
                .build();
    }

}
