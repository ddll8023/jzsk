package com.jzsk.backendv2.config.logging;

import com.jzsk.backendv2.config.LoggingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final LoggingProperties loggingProperties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !loggingProperties.isRequestEnabled();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long startAt = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long costMs = System.currentTimeMillis() - startAt;
            log.info("request completed method={} uri={} status={} costMs={} clientIp={}",
                    request.getMethod(),
                    buildRequestUri(request),
                    response.getStatus(),
                    costMs,
                    resolveClientIp(request));
        }
    }

    private String buildRequestUri(HttpServletRequest request) {
        if (!StringUtils.hasText(request.getQueryString())) {
            return request.getRequestURI();
        }
        return request.getRequestURI() + "?" + request.getQueryString();
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwardedFor)) {
            String[] ips = forwardedFor.split(",");
            return ips[0].trim();
        }
        return request.getRemoteAddr();
    }
}
