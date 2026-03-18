package io.github.chance.coreapm.service;

import io.github.chance.coreapm.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public void test() {
        testRepository.find();
    }
}