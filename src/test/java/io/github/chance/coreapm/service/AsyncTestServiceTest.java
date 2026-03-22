package io.github.chance.coreapm.service;

import io.github.chance.coreapm.common.ContextHolder;
import io.github.chance.coreapm.common.RequestContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AsyncTestServiceTest {
    @Test
    void test() {
        System.out.println("hello");
    }
/*    @Autowired
    AsyncTestService asyncTestService;

    @Test
    void asyncTask()  {
        RequestContext ctx = new RequestContext("TRACE-123");
        ContextHolder.set(ctx);

        System.out.println("ctx = " + ctx);

        CompletableFuture<String> future = asyncTestService.asyncTask("task1");

        String result = null;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        // ⚠️ 여기 중요 (지금 코드 기준)
        assertEquals("done", result);

        ContextHolder.clear();
    }*/
}