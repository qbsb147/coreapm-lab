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
    public CompletableFuture<String> asyncTask(String type){

        metricsService.incrementAndGet();

        String traceId = ContextHolder.get().getTraceId();
        try{
            System.out.println("Async traceId = " + traceId);
            if("fast".equals(type))
                Thread.sleep(100);
            else if("slow".equals(type))
                Thread.sleep(500);
            else throw new RuntimeException();
            return CompletableFuture.completedFuture(traceId);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            metricsService.decrementAndGet();
        }
    }
}
