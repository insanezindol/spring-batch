package com.example.springbatch.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
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
public class FlowStepJobConfig {

    final JobRepository jobRepository;
    final PlatformTransactionManager transactionManager;

    public FlowStepJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job flowStepJob() {
        Job exampleJob = new JobBuilder("flowStepJob", jobRepository)
                .start(flowStartStep())
                .on("FAILED") //startStep의 ExitStatus가 FAILED일 경우
                .to(flowFailOverStep()) //failOver Step을 실행 시킨다.
                .on("*") //failOver Step의 결과와 상관없이
                .to(flowWriteStep()) //write Step을 실행 시킨다.
                .on("*") //write Step의 결과와 상관없 이
                .end() //Flow를 종료시킨다.

                .from(flowStartStep()) //startStep이 FAILED가 아니고
                .on("COMPLETED") //COMPLETED일 경우
                .to(flowProcessStep()) //process Step을 실행 시킨다
                .on("*") //process Step의 결과와 상관없이
                .to(flowWriteStep()) // write Step을 실행 시킨다.
                .on("*") //wrtie Step의 결과와 상관없이
                .end() //Flow를 종료 시킨다.

                .from(flowStartStep()) //startStep의 결과가 FAILED, COMPLETED가 아닌
                .on("*") //모든 경우
                .to(flowWriteStep()) //write Step을 실행시킨다.
                .on("*") //write Step의 결과와 상관없이
                .end() //Flow를 종료시킨다.
                .end()
                .build();

        return exampleJob;
    }

    @Bean
    public Step flowStartStep() {
        return new StepBuilder("flowStartStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("Start Step!");

                    String result = "COMPLETED";
                    // String result = "FAIL";
                    // String result = "UNKNOWN";

                    // Flow에서 on은 RepeatStatus가 아닌 ExitStatus를 바라본다.
                    switch (result) {
                        case "COMPLETED" -> contribution.setExitStatus(ExitStatus.COMPLETED);
                        case "FAIL" -> contribution.setExitStatus(ExitStatus.FAILED);
                        case "UNKNOWN" -> contribution.setExitStatus(ExitStatus.UNKNOWN);
                        default -> { }
                    }

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowFailOverStep() {
        return new StepBuilder("flowFailOverStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("FailOver Step!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowProcessStep() {
        return new StepBuilder("flowProcessStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("Process Step!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowWriteStep() {
        return new StepBuilder("flowWriteStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("Write Step!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

}
