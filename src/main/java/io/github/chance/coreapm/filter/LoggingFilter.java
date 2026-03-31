package io.github.chance.coreapm.filter;

import io.github.chance.coreapm.common.ContextHolder;
import io.github.chance.coreapm.common.RequestContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if(request.getDispatcherType()==DispatcherType.REQUEST){
            String headerTraceId = request.getHeader("X-Trace-Id");
            String traceId = (headerTraceId != null && !headerTraceId.isBlank())
                    ? headerTraceId
                    : UUID.randomUUID().toString();
            long startTime = System.currentTimeMillis();

            ContextHolder.set(new RequestContext(traceId, startTime));
        }

        try {
            filterChain.doFilter(request, response);
        } catch(Exception e){
            Throwable root = e;
            while (root.getCause()!=null) root = root.getCause();
            ContextHolder.get().setError(root.getClass().getSimpleName());
            throw e;
        } finally {
            RequestContext ctx = ContextHolder.get();
            long duration = System.currentTimeMillis() - ctx.getStartTime();
            boolean isError = ctx.getError() != null;
            int status = isError ? 500 : response.getStatus();

            System.out.println("[TRACE] " + ctx.getTraceId()
                    + " URI=" + request.getRequestURI()
                    + " status=" + status
                    + " TIME=" + duration + "ms"
                    + (ctx.getError() != null ? " ERROR=" + ctx.getError() : ""));

            for (String log : ctx.getLogs()) {
                System.out.println("  → " + log);
            }

            ContextHolder.clear(); // ⭐ 반드시
        }
    }
}