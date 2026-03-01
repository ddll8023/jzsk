package com.szy.common.exception;

import com.szy.common.lang.IResponseCode;
import com.szy.common.lang.ResponseCode;
import lombok.Getter;

/**
 * 业务异常类
 * 用途：业务逻辑中抛出可预知的异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = ResponseCode.FAIL.getCode();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(IResponseCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
    }

}