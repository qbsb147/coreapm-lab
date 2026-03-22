package io.github.chance.coreapm.common;

import java.util.ArrayList;
import java.util.List;

public class RequestContext {
    private final String traceId;
    private final long startTime;
    private int depth;
    private List<String> logs = new ArrayList<>();
    private String error;

    public RequestContext(String traceId, long startTime) {
        this.traceId = traceId;
        this.startTime = startTime;
        this.depth = 0;
    }

    public RequestContext(String traceId) {
        this.traceId = traceId;
        this.startTime = System.currentTimeMillis();
        this.depth = 0;
    }

    public void increaseDepth(){
        depth++;
    }

    public void decreaseDepth(){
        depth--;
    }

    public void addLog(String log){
        logs.add(log);
    }

    public String[] getLogs(){
        return logs.toArray(new String[0]);
    }

    public void setError(String error) {
        this.error = error;
    }
    public String getError(){
        return error;
    }
    public int getDepth(){return depth;}
    public String getTraceId() { return traceId; }
    public long getStartTime() { return startTime; }
}