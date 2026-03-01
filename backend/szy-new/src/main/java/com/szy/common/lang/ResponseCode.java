package com.szy.common.lang;

import lombok.Getter;

/**
 * 通用响应码枚举
 */
@Getter
public enum ResponseCode implements IResponseCode {

    /** 成功 */
    SUCCESS(200, "操作成功"),

    /** 失败 */
    FAIL(400, "操作失败"),

    /** 参数错误 */
    PARAM_ERROR(400, "参数错误"),

    /** 未授权 */
    UNAUTHORIZED(401, "未授权，请登录"),

    /** 禁止访问 */
    FORBIDDEN(403, "禁止访问"),

    /** 资源不存在 */
    NOT_FOUND(404, "资源不存在"),

    /** 服务器内部错误 */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}