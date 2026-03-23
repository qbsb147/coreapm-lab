package io.github.chance.coreapm.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import io.micrometer.core.instrument.Counter;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class MetricsCollector {
    private final PrometheusMeterRegistry meterRegistry;
    private final Map<String, ApiMetrics> metricsMap = new ConcurrentHashMap<>();
    private final AtomicInteger currentRequests = new AtomicInteger();

    public void incrementAndGet(){
        currentRequests.incrementAndGet();
    }

    public void decrementAndGet(){
        currentRequests.decrementAndGet();
    }

    public void record(String endpoint, long duration, boolean isError) {
        currentRequests.incrementAndGet();
        try {
            ApiMetrics apiMetrics = metricsMap.computeIfAbsent(endpoint, k -> new ApiMetrics());
            apiMetrics.record(duration, isError);
            Timer.builder("api.response.time")
                    .tag("endpoint", endpoint)
                    .register(meterRegistry)
                    .record(duration, TimeUnit.MILLISECONDS);

            Counter callCounter = meterRegistry.counter("api.call.count", "endpoint", endpoint);
            callCounter.increment();

            if (isError) {
                Counter errorCounter = meterRegistry.counter("api.error.count", "endpoint", endpoint);
                errorCounter.increment();
            }
        } finally {
            currentRequests.decrementAndGet();
        }
    }
    public double getCurrentRequests() {
        return currentRequests.get();
    }
    @PostConstruct
    public void initGauge(){
        Gauge.builder("api.current.requests", this, MetricsCollector::getCurrentRequests)
                .description("현재 처리 중인 API 요청 수")
                .register(meterRegistry);
    }

    public Map<String, ApiMetrics> getAllMetrics(){
        return metricsMap;
    }
}
