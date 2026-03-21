package com.jzsk.backendv2.utils;

import org.slf4j.MDC;

public final class TraceIdUtils {

    public static final String TRACE_ID_KEY = "traceId";

    private TraceIdUtils() {
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }
}
