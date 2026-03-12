package com.szy.common.result;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 统一API响应封装
 * 遵循规范：不使用Lombok，提供静态工厂方法（规范7.7.1）
 */
@Schema(description = "统一API响应")
public class ApiResult<T> {

    @Schema(description = "状态码")
    private int code;

    @Schema(description = "响应消息")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    public ApiResult() {
    }

    public ApiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功响应
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "操作成功", data);
    }

    public static <T> ApiResult<T> success(T data, String message) {
        return new ApiResult<>(200, message, data);
    }

    public static <T> ApiResult<T> success(String message) {
        return new ApiResult<>(200, message, null);
    }

    // 失败响应
    public static <T> ApiResult<T> error(int code, String message) {
        return new ApiResult<>(code, message, null);
    }

    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(400, message, null);
    }

    // Getter and Setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
