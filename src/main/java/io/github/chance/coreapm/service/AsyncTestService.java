package io.github.chance.coreapm.service;

import io.github.chance.coreapm.common.ContextHolder;
import io.github.chance.coreapm.metrics.MetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncTestService {

    private final MetricsService metricsService;

    @Async
    public CompletableFuture<String> asyncTask(String endpoint){

        metricsService.incrementAndGet();

        String traceId = ContextHolder.get().getTraceId();
        long start = System.nanoTime();
        boolean isError = false;
        try{
            Thread.sleep(5000);
            System.out.println("Async traceId = " + traceId);
            return CompletableFuture.completedFuture(traceId);
        }catch (Exception e){
            isError = true;
            throw new RuntimeException(e);
        }finally {
            long duration = (System.nanoTime() - start)/1_000_000;
            metricsService.record(endpoint, duration, isError);
            metricsService.decrementAndGet();
        }
    }
}
