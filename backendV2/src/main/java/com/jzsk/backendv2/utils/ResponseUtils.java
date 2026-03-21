package com.jzsk.backendv2.utils;

import com.jzsk.backendv2.pojo.vo.ApiResult;
import org.springframework.http.ResponseEntity;

public final class ResponseUtils {

    private ResponseUtils() {
    }

    public static <T> ResponseEntity<ApiResult<T>> ok(T data) {
        return ResponseEntity.ok(ApiResult.success(data));
    }

    public static <T> ResponseEntity<ApiResult<T>> ok(T data, String message) {
        return ResponseEntity.ok(ApiResult.success(data, message));
    }

    public static ResponseEntity<ApiResult<Void>> okMessage(String message) {
        return ResponseEntity.ok(ApiResult.successMessage(message));
    }
}
