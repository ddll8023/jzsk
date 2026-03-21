package com.jzsk.backendv2.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jzsk.backendv2.utils.TraceIdUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "统一接口返回结构")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "业务状态码", example = "200")
    private final int code;

    @Schema(description = "返回消息", example = "成功")
    private final String message;

    @Schema(description = "返回数据")
    private final T data;

    @Schema(description = "响应时间")
    private final LocalDateTime timestamp;

    @Schema(description = "请求追踪标识")
    private final String traceId;

    private ApiResult(int code, String message, T data, LocalDateTime timestamp, String traceId) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
        this.traceId = traceId;
    }

    public static <T> ApiResult<T> success(T data) {
        return build(200, "成功", data);
    }

    public static <T> ApiResult<T> success(T data, String message) {
        return build(200, message, data);
    }

    public static <T> ApiResult<T> successMessage(String message) {
        return build(200, message, null);
    }

    public static <T> ApiResult<T> error(int code, String message) {
        return build(code, message, null);
    }

    private static <T> ApiResult<T> build(int code, String message, T data) {
        return new ApiResult<>(code, message, data, LocalDateTime.now(), TraceIdUtils.getTraceId());
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getTraceId() {
        return traceId;
    }
}
