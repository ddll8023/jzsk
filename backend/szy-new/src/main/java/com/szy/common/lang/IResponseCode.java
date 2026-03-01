package com.szy.common.lang;

/**
 * 响应码接口
 */
public interface IResponseCode {

    /**
     * 获取状态码
     */
    int getCode();

    /**
     * 获取消息
     */
    String getMessage();

}