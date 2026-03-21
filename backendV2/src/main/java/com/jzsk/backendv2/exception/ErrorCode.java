package com.jzsk.backendv2.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    SUCCESS(200, 200, "成功"),
    BAD_REQUEST(400, 400, "请求参数错误"),
    UNAUTHORIZED(401, 401, "未认证或登录已失效"),
    FORBIDDEN(403, 403, "无权限访问"),
    NOT_FOUND(404, 404, "资源不存在"),
    AUTHENTICATION_FAILED(401, 40101, "用户名或密码错误"),
    TOKEN_INVALID(401, 40102, "登录状态已失效"),
    INTERNAL_ERROR(500, 500, "系统异常，请稍后重试");

    private final int httpStatus;
    private final int code;

    private final String message;
}
