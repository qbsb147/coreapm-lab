package io.github.chance.coreapm.aspect;

import io.github.chance.coreapm.common.ContextHolder;
import io.github.chance.coreapm.common.RequestContext;
import io.github.chance.coreapm.metrics.MetricsService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.CompletableFuture;

@Aspect
@Component
@RequiredArgsConstructor
public class MetricsAspect {
    private final MetricsService metricsService;

    @Around("execution(* io.github.chance.coreapm.controller..*(..)) || " +
            "execution(* io.github.chance.coreapm.service..*(..))")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable{

        RequestContext ctx = ContextHolder.get();
        if (ctx == null) {
            return joinPoint.proceed();
        }
        String traceId = ctx.getTraceId();

        String endpoint;
        ServletRequestAttributes attr =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            endpoint = attr.getRequest().getRequestURI();  // Controller
        } else {
            endpoint = joinPoint.getSignature().toShortString();  // Service / Async
        }


        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();

            if(result instanceof CompletableFuture<?>){
                return ((CompletableFuture<?>)result)
                        .whenComplete((res, ex) -> {
                            long duration = System.currentTimeMillis() - start;
                            boolean isError = (ex != null);
                            metricsService.record(endpoint, duration, isError, traceId);
                        });
            }else {
                long duration = System.currentTimeMillis() - start;
                metricsService.record(endpoint, duration, false, traceId);
                return result;
            }
        } catch (Throwable e) {
            long duration = System.currentTimeMillis() - start;
            metricsService.record(endpoint, duration, true, traceId);
            throw e;
        }
    }
/*
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
            metricsService.record(endpoint, duration, isError, UUID.randomUUID().toString());
        }
    }
*/
}
