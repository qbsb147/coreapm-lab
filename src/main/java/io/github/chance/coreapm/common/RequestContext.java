package io.github.chance.coreapm.common;

public class RequestContext {
    private final String traceId;
    private final long startTime;

    public RequestContext(String traceId, long startTime) {
        this.traceId = traceId;
        this.startTime = startTime;
    }

    public String getTraceId() { return traceId; }
    public long getStartTime() { return startTime; }
}