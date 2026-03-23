package io.github.chance.coreapm.aspect;

import io.github.chance.coreapm.metrics.MetricsCollector;
import io.github.chance.coreapm.metrics.MetricsService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MetricsAspect {
    private final MetricsService metricsService;

    // Controller 패키지 기준 모든 메서드 적용
    @Around("execution(* io.github.chance.coreapm.controller..*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String endpoint = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();
        boolean isError = false;

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            isError = true;
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - start;
            metricsService.record(endpoint, duration, isError);
        }
    }
}
