package io.github.chance.coreapm.controller;

import io.github.chance.coreapm.service.AsyncTestService;
import io.github.chance.coreapm.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;
    private final AsyncTestService asyncTestService;

    @GetMapping("/test")
    public String test() {
        testService.test();
        return "ok";
    }

    @GetMapping("/async")
    public String asyncTest(@RequestParam String type){
        try {
            return asyncTestService.asyncTask(type).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/slow")
    public String slow() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "slow";
    }
}