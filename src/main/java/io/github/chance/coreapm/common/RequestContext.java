package io.github.chance.coreapm.common;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class RequestContext {
    @Getter
    private final String traceId;
    @Getter
    private final long startTime;
    @Getter
    private int depth;
    private List<String> logs = new ArrayList<>();
    @Getter
    @Setter
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

}