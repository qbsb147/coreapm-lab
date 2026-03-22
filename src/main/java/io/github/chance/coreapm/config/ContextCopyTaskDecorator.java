package io.github.chance.coreapm.config;

import io.github.chance.coreapm.common.ContextHolder;
import io.github.chance.coreapm.common.RequestContext;
import org.springframework.core.task.TaskDecorator;

public class ContextCopyTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        RequestContext context = ContextHolder.get();
        return () -> {
            try {
                ContextHolder.set(context);
                runnable.run();
            }finally {
                ContextHolder.clear();
            }
        };
    }
}
