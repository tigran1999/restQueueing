package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    private int coreCount = Runtime.getRuntime().availableProcessors();

    @Bean(name = "threadPoolExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreCount);
        executor.setMaxPoolSize(25);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("threadPoolExecutor-");
        executor.initialize();
        return executor;
    }

}
