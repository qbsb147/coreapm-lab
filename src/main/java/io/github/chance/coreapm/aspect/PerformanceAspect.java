package io.github.chance.coreapm.aspect;

import io.github.chance.coreapm.common.ContextHolder;
import io.github.chance.coreapm.common.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Around("execution(* io.github.chance.coreapm..service..*(..)) || " +
            "execution(* io.github.chance.coreapm..repository..*(..))")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {

        RequestContext ctx = ContextHolder.get();

        // ⭐ 없으면 그냥 실행 (필터 안 타는 경우 대비)
        if (ctx == null) {
            return joinPoint.proceed();
        }

        ctx.increaseDepth(); // 진입
        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        }catch (Exception e) {
            log.error("[ERROR] {} {}", ctx.getTraceId(), e.getMessage());
            throw e;
        } finally {
            long end = System.currentTimeMillis();
            long duration = end - start;
            String signature = joinPoint.getSignature().toShortString();
            int depth = ctx.getDepth();

            String log = "  ".repeat(depth - 1)
                    + signature
                    + " " + duration + "ms";
            if((duration)>1000){
                log += " ⚠ SLOW";
            }

            ctx.addLog(log);

            ctx.decreaseDepth(); // 종료
        }
    }
}