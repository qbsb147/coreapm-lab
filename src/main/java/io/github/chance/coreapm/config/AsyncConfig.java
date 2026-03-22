package io.github.chance.coreapm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public TaskDecorator taskDecorator(){
        return new ContextCopyTaskDecorator();
    }

    @Bean
    public TaskExecutor taskExecutor(TaskDecorator taskDecorator){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("async-executor-");
        executor.initialize();
        return new ContextAwareTaskExecutor(executor, taskDecorator);
    }
}
