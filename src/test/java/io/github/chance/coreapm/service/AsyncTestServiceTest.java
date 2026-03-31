package io.github.chance.coreapm.service;

import io.github.chance.coreapm.common.ContextHolder;
import io.github.chance.coreapm.common.RequestContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AsyncTestServiceTest {

    @Autowired
    AsyncTestService asyncTestService;

    @Test
    void asyncTask()  {
        RequestContext ctx = new RequestContext("TRACE-123");
        ContextHolder.set(ctx);

        String result = null;
        try {
            result = asyncTestService.asyncTask(result).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        assertEquals("TRACE-123", result);

        ContextHolder.clear();
    }
    @Test
    void asyncThreadLocalConcurrencyTest() throws Exception {
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final String traceId = "TRACE-" + i;
            futures.add(CompletableFuture.supplyAsync(() -> {
                // 각 스레드마다 독립적인 RequestContext 설정
                RequestContext ctx = new RequestContext(traceId);
                ContextHolder.set(ctx);

                try {
                    // asyncTask 호출
                    return asyncTestService.asyncTask(traceId).get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                } finally {
                    ContextHolder.clear();
                }
            }, executor));
        }

        // 모든 결과 확인
        List<String> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        for (int i = 0; i < threadCount; i++) {
            assertEquals("TRACE-" + i, results.get(i));
        }

        executor.shutdown();
    }
}