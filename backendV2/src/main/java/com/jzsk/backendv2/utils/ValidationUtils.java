package com.jzsk.backendv2.utils;

import org.springframework.util.StringUtils;

/**
 * 参数校验工具类
 * 职责：提供统一的参数校验和规范化方法
 * 遵循KISS原则：方法简洁，职责单一
 */
public class ValidationUtils {

    private ValidationUtils() {
    }

    /**
     * 校验并返回非空文本
     * @param value 文本值
     * @param message 非空时的错误消息
     * @return 去除首尾空格后的文本
     * @throws IllegalArgumentException 文本为空时抛出
     */
    public static String requireNonBlank(String value, String message) {
        String normalized = normalizeOptional(value);
        if (!StringUtils.hasText(normalized)) {
            throw new IllegalArgumentException(message);
        }
        return normalized;
    }

    /**
     * 规范化可选文本
     * @param value 文本值
     * @return 去除首尾空格后的文本，空白文本返回null
     */
    public static String normalizeOptional(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
