package com.szy.common.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode implements IResponseCode {

    /**
     * 成功
     */
    SUCCESS(200, "请求成功"),

    /**
     * 失败
     */
    FAIL(400, "请求失败"),

    /**
     * 用户信息不存在
     */
    USER_NOT_FOUND(10001, "用户信息不存在"),

    /**
     * 插入重复记录
     */
    INSERT_NOT_UNIQUE(10002,"用户名已存在"),

    /**
     * 操作记录不存在
     */
    NOT_EXIST(10003,"操作记录不存在"),
    ;


    /**
     * code
     */
    final Integer code;

    /**
     * message desc
     */
    final String message;
}
