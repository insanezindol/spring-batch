package com.example.springbatch.tasklet;

import com.example.springbatch.service.CustomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class MethodInvokeTaskletJobConfig {

    final JobRepository jobRepository;
    final PlatformTransactionManager transactionManager;

    public MethodInvokeTaskletJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job methodInvokeTaskletJob() {
        return new JobBuilder("methodInvokeTaskletJob", jobRepository)
                .start(methodInvokeStep())
                .build();
    }

    @Bean
    public Step methodInvokeStep() {
        return new StepBuilder("methodInvokeStep", jobRepository)
                .tasklet(customMethodInvokeTasklet(), transactionManager).build();
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
