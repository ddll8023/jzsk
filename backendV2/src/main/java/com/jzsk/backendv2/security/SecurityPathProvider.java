package com.jzsk.backendv2.security;

import com.jzsk.backendv2.config.SecurityProperties;
import com.jzsk.backendv2.config.V2Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityPathProvider {

    private static final String DEFAULT_API_PREFIX = "/api";
    private static final String AUTH_LOGIN_SUFFIX = "/auth/login";

    private final SecurityProperties securityProperties;
    private final V2Properties v2Properties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public String[] getPermitAllPatterns() {
        List<String> patterns = getPermitAllPatternList();
        return patterns.toArray(new String[0]);
    }

    public boolean isPermitAll(String requestPath) {
        return getPermitAllPatternList().stream()
                .anyMatch(pattern -> antPathMatcher.match(pattern, requestPath));
    }

    private List<String> getPermitAllPatternList() {
        LinkedHashSet<String> patterns = new LinkedHashSet<>();
        patterns.add(buildAuthLoginPath());
        patterns.addAll(securityProperties.getPermitAll());
        return new ArrayList<>(patterns);
    }

    private String buildAuthLoginPath() {
        return normalizeApiPrefix(v2Properties.getApiPrefix()) + AUTH_LOGIN_SUFFIX;
    }

    private String normalizeApiPrefix(String apiPrefix) {
        if (!StringUtils.hasText(apiPrefix)) {
            return DEFAULT_API_PREFIX;
        }
        String normalized = apiPrefix.trim();
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
