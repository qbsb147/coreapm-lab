package io.github.chance.coreapm.metrics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final MetricsCollector collector;


    public void record(String endpoint, long duration, boolean isError, String traceId) {
        collector.record(endpoint, duration, isError, traceId);
    }

    public Map<String, ApiMetrics> getMetrics(){
        return collector.getAllMetrics();
    }

    public void incrementAndGet(){
        collector.incrementAndGet();
    }

    public void decrementAndGet(){
        collector.decrementAndGet();
    }
}
