package com.szy.common.constant;

/**
 * 公共常量定义
 * 遵循规范：KISS - 简单常量
 */
public class Const {

    /**
     * 成功状态码
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 失败状态码
     */
    public static final int ERROR_CODE = 400;

    /**
     * 未授权状态码
     */
    public static final int UNAUTHORIZED_CODE = 401;

    /**
     * 禁止访问状态码
     */
    public static final int FORBIDDEN_CODE = 403;

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE = 1;

    /**
     * 默认每页条数
     */
    public static final int DEFAULT_SIZE = 10;

    /**
     * 最大每页条数
     */
    public static final int MAX_SIZE = 100;

    private Const() {
    }

}
