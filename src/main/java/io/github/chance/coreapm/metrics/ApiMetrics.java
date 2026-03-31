package io.github.chance.coreapm.metrics;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiMetrics {
    private long totalTime;
    @Getter
    private int count;
    @Getter
    private int errorCount;
    @Getter
    private final List<Record> records = Collections.synchronizedList(new ArrayList<>());

    @AllArgsConstructor
    @Getter
    public static class Record{
        private final long duration;
        private final boolean isError;
        private final String traceId;
    }

    public synchronized void record(long duration, boolean isError) {
        this.totalTime += duration;
        this.count++;
        if(isError) this.errorCount++;
    }

    public synchronized double getAverageTime(){
        return count == 0 ? 0 : (double) totalTime / count;
    }
}