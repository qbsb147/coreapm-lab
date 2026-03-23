package io.github.chance.coreapm.controller;

import io.github.chance.coreapm.service.AsyncTestService;
import io.github.chance.coreapm.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String asyncTest(){
        asyncTestService.asyncTask("/async");
        return "async started";
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