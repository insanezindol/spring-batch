package com.example.springbatch.tasklet;

import com.example.springbatch.service.CustomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MethodInvokeTaskletJobConfig {

    final JobBuilderFactory jobBuilderFactory;
    final StepBuilderFactory stepBuilderFactory;

    public MethodInvokeTaskletJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job methodInvokeTaskletJob() {
        return jobBuilderFactory.get("methodInvokeTaskletJob")
                .start(methodInvokeStep())
                .build();
    }

    @Bean
    public Step methodInvokeStep() {
        return stepBuilderFactory.get("methodInvokeStep")
                .tasklet(customMethodInvokeTasklet()).build();
    }

    @Bean
    public CustomService customService() {
        return new CustomService();
    }

    @Bean
    public MethodInvokingTaskletAdapter customMethodInvokeTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(customService());
        adapter.setTargetMethod("businessLogic");
        return adapter;
    }

}
