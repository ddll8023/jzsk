package com.szy.common.exception;

/**
 * 业务异常
 * 用于抛出业务逻辑错误
 * 遵循规范：简单异常类（规范8.1.1）
 */
public class BusinessException extends RuntimeException {

    private int code;

    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
