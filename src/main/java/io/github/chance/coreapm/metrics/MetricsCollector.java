package io.github.chance.coreapm.metrics;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MetricsCollector {
    private final Map<String, ApiMetrics> metricsMap = new ConcurrentHashMap<>();

    public void record(String endpoint, long duration, boolean isError) {
        metricsMap.computeIfAbsent(endpoint, k -> new ApiMetrics())
                .record(duration,isError);
    }

    public Map<String, ApiMetrics> getAllMetrics(){
        return metricsMap;
    }
}
