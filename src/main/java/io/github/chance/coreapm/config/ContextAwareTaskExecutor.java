package io.github.chance.coreapm.config;

import io.github.chance.coreapm.common.ContextHolder;
import io.github.chance.coreapm.common.RequestContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskDecorator;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class ContextAwareTaskExecutor implements AsyncTaskExecutor {
    private final AsyncTaskExecutor delegate;
    private final TaskDecorator taskDecorator;

    @Override
    public void execute(Runnable task) {
        final RequestContext ctx = ContextHolder.get();

        Runnable decorated = () -> {
            try {
                if (ctx != null) ContextHolder.set(ctx);
                task.run();
            } finally {
                ContextHolder.clear();
            }
        };

        delegate.execute(decorated);
    }

    @Override
    public Future<?> submit(Runnable task) {
        final RequestContext ctx = ContextHolder.get();

        Runnable decorated = () -> {
            try {
                if (ctx != null) ContextHolder.set(ctx);
                task.run();
            } finally {
                ContextHolder.clear();
            }
        };

        return delegate.submit(decorated);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        final RequestContext ctx = ContextHolder.get();
        Callable<T> decorated = () -> {
            try {
                if (ctx != null) ContextHolder.set(ctx);
                return task.call();  // 한 번만 실행, 반환값 그대로 전달
            } finally {
                ContextHolder.clear();
            }
        };
        return delegate.submit(decorated);
    }
}
