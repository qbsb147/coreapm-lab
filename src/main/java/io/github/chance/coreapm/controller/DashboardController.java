package io.github.chance.coreapm.controller;

import io.github.chance.coreapm.metrics.ApiMetrics;
import io.github.chance.coreapm.metrics.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final MetricsService metricsService;

    @GetMapping("/metrics")
    public Map<String, ApiMetrics> getMetrics(){
        return metricsService.getMetrics();
    }

    @GetMapping("/metrics/slow")
    public Map<String, ApiMetrics> getSlowApis(){
        return metricsService.getMetrics().entrySet().stream()
                .filter(e -> e.getValue().getAverageTime() > 1000)
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }
}
