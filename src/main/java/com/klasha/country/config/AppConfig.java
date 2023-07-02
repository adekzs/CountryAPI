package com.klasha.country.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(40); // Set the desired core pool size
        executor.setMaxPoolSize(40); // Set the desired maximum pool size
        executor.setQueueCapacity(100); // Set the desired queue capacity
        executor.initialize();
        return executor;
    }
}
