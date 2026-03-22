package io.github.chance.coreapm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AlertService {
    private static final Logger log = LoggerFactory.getLogger(AlertService.class);

/*
    Slow API 알림
    @param endpoint 호출된 API 이름
    @param duration 실행 시간(ms)
*/
    public void notify(String endpoint, long duration){
        log.warn("[ALERT] Slow API detected: {} took {}ms", endpoint, duration);
    }
    // 추가 확장 가능: Slack, Email 등
}
