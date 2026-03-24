package com.jzsk.backendv2.pojo.enums;

/**
 * 预警等级枚举
 * 统一管理预警等级命名，与预警指标阈值对应
 */
public enum WarningLevel {

    /** 一般预警 */
    GENERAL("一般预警"),

    /** 严重预警 */
    SERIOUS("严重预警"),

    /** 特别严重预警 */
    EXTRAORDINARY("特别严重预警");

    private final String description;

    WarningLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
