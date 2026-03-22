package io.github.chance.coreapm.service;

import io.github.chance.coreapm.common.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AsyncTestService {

    @Async
    public CompletableFuture<String> asyncTask(String endpoint){
        log.info("Thread: {}", Thread.currentThread().getName());
        log.info("Context: {}", ContextHolder.get());
        String traceId = ContextHolder.get().getTraceId();
        log.info("Async task traceId: {}", traceId);
        return CompletableFuture.completedFuture("done");
    }
}
