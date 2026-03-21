package com.jzsk.backendv2.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final int httpStatus;

    private final int code;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getHttpStatus();
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.httpStatus = errorCode.getHttpStatus();
        this.code = errorCode.getCode();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.httpStatus = resolveHttpStatus(code);
        this.code = code;
    }

    private int resolveHttpStatus(int code) {
        if (code >= 100 && code < 600) {
            return code;
        }
        if (code >= 40100 && code < 40200) {
            return 401;
        }
        if (code >= 40300 && code < 40400) {
            return 403;
        }
        if (code >= 40400 && code < 40500) {
            return 404;
        }
        return 400;
    }
}
