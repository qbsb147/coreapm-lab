package io.github.chance.coreapm.metrics;

import io.github.chance.coreapm.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final MetricsCollector collector;
    private final AlertService alertService;

    public void record(String endpoint, long duration, boolean isError) {
        collector.record(endpoint, duration, isError);
        if(duration> 1000){
            alertService.notify(endpoint, duration);
        }
    }

    public Map<String,ApiMetrics> getMetrics(){
        return collector.getAllMetrics();
    }
}
