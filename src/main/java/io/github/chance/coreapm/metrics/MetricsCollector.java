package io.github.chance.coreapm.metrics;

import io.github.chance.coreapm.service.AlertService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import io.micrometer.core.instrument.Counter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class MetricsCollector {
    private final PrometheusMeterRegistry meterRegistry;
    private final Map<String, ApiMetrics> metricsMap = new ConcurrentHashMap<>();
    private final Map<String, Queue<ApiMetrics.Record>> traceRecords = new ConcurrentHashMap<>();
    private final AtomicInteger currentRequests = new AtomicInteger();
    private final Map<String, Map<String, AtomicLong>> durationBuckets = new ConcurrentHashMap<>();
    private final AlertService alertService;
    private final Map<String, Map<String, Gauge>> registeredGauges = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Timer> timerMap = new ConcurrentHashMap<>();

    public void incrementAndGet(){
        currentRequests.incrementAndGet();
    }

    public void decrementAndGet(){
        currentRequests.decrementAndGet();
    }

    public void record(String endpoint, long duration, boolean isError, String traceId) {
        ApiMetrics apiMetrics = metricsMap.computeIfAbsent(endpoint, k -> new ApiMetrics());
        apiMetrics.record(duration, isError);

        traceRecords
                .computeIfAbsent(traceId, k -> new ConcurrentLinkedQueue<>())
                .add(new ApiMetrics.Record(duration, isError, endpoint));

        /*
        Timer.builder("api.response.time")
                .tag("endpoint", endpoint)
                .register(meterRegistry)
                .record(duration, TimeUnit.MILLISECONDS);
                */
        getTimer(endpoint).record(duration, TimeUnit.MILLISECONDS);

        Counter callCounter = meterRegistry.counter("api.call.count", "endpoint", endpoint);
        callCounter.increment();

        if (isError) {
            Counter errorCounter = meterRegistry.counter("api.error.count", "endpoint", endpoint);
            errorCounter.increment();
        }

        if(duration>1000){
            alertService.notify(endpoint, duration);
        }
//        recordBucket(endpoint, duration);
    }

    private Timer getTimer(String endpoint){
        return timerMap.computeIfAbsent(endpoint, ep ->
                Timer.builder("api.duration")
                        .tag("endpoint", ep)
                        .publishPercentileHistogram()
                        .register(meterRegistry)
        );
    }

    private void recordBucket(String endpoint, long duration) {
        String bucket = duration < 100 ? "0-100ms"
                : duration < 500 ? "100-500ms"
                : duration < 1000 ? "500-1000ms" : ">1000ms";

        AtomicLong count = durationBuckets
                .computeIfAbsent(endpoint, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(bucket, k -> new AtomicLong());

        count.incrementAndGet();

        registeredGauges
                .computeIfAbsent(endpoint, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(bucket, k ->
                        Gauge.builder("api.duration.bucket", count, AtomicLong::get)
                                .tag("endpoint", endpoint)
                                .tag("bucket", bucket)
                                .register(meterRegistry)
                );
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
