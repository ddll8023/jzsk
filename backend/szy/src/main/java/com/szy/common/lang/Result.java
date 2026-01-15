package com.szy.common.lang;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 6738387175874422264L;
    private Integer code;

    private String message;

    private T data;
    private Result() {
    }

    public static <T> Result<T> ok() {
        return createResult(ResponseCode.SUCCESS.getCode(), null, null);
    }

    public static <T> Result<T> ok(T data) {
        return createResult(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

//    public static <T> Result<T> ok(String message) {
//        return createResult(ResponseCode.SUCCESS.getCode(), message, null);
//    }

    public static <T> Result<T> ok(T data, String message) {
        return createResult(ResponseCode.SUCCESS.getCode(), message, data);
    }

    public static <T> Result<T> fail() {
        return createResult(ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getMessage(), null);
    }

    public static <T> Result<T> fail(String message) {
        return createResult(ResponseCode.FAIL.getCode(), message, null);
    }

    public static <T> Result<T> fail(ResponseCode responseCode) {
        return createResult(responseCode.getCode(), responseCode.getMessage(), null);
    }

    public static <T> Result<T> fail(ResponseCode responseCode, T data) {
        return createResult(responseCode.getCode(), responseCode.getMessage(), data);
    }

    public static <T> Result<T> fail(ResponseCode responseCode, String message) {
        return createResult(responseCode.getCode(),
                String.format("%s %s", responseCode.getMessage(), message), null);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return createResult(code, message, null);
    }

    private static <T> Result<T> createResult(Integer code, String message, T data) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }
}
