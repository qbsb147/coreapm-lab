package io.github.chance.coreapm.filter;

import io.github.chance.coreapm.common.ContextHolder;
import io.github.chance.coreapm.common.RequestContext;
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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String traceId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        ContextHolder.set(new RequestContext(traceId, startTime));

        try {
            filterChain.doFilter(request, response);
        } finally {
            RequestContext ctx = ContextHolder.get();
            long duration = System.currentTimeMillis() - ctx.getStartTime();

            System.out.println("[TRACE] " + ctx.getTraceId()
                    + " URI=" + request.getRequestURI()
                    + " TIME=" + duration + "ms");

            ContextHolder.clear(); // ⭐ 반드시
        }
    }
}