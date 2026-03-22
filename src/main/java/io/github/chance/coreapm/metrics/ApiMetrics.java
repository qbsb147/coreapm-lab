package io.github.chance.coreapm.metrics;

public class ApiMetrics {
    private long totalTime;
    private int count;
    private int errorCount;

    public synchronized void record(long duration, boolean isError) {
        this.totalTime += duration;
        this.count++;
        if(isError) this.errorCount++;
    }

    public synchronized double getAverageTime(){
        return count == 0 ? 0 : (double) totalTime / count;
    }
    public int getCount(){return count;}
    public int getErrorCount(){return errorCount;}
}