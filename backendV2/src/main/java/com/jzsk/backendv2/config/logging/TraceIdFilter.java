package com.jzsk.backendv2.config.logging;

import com.jzsk.backendv2.config.LoggingProperties;
import com.jzsk.backendv2.utils.TraceIdUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class TraceIdFilter extends OncePerRequestFilter {

    private final LoggingProperties loggingProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = request.getHeader(loggingProperties.getTraceHeader());
        if (!StringUtils.hasText(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }

        MDC.put(TraceIdUtils.TRACE_ID_KEY, traceId);
        response.setHeader(loggingProperties.getTraceHeader(), traceId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TraceIdUtils.TRACE_ID_KEY);
        }
    }
}
