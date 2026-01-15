package com.szy.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlarmValue {
    @JsonProperty("L1_GP")
    private MonitorTypeConfig L1_GP;
    @JsonProperty("L1_GP_AVG_MONTH")
    private MonitorTypeConfig L1_GP_AVG_MONTH;
    @JsonProperty("L1_GP_SPEED_MONTH")
    private MonitorTypeConfig L1_GP_SPEED_MONTH;
    @JsonProperty("L1_GP_AVG_HOUR")
    private MonitorTypeConfig L1_GP_AVG_HOUR;
    @JsonProperty("L1_GP_AC_DAY")
    private MonitorTypeConfig L1_GP_AC_DAY;
    @JsonProperty("L1_GP_SPEED_DAY")
    private MonitorTypeConfig L1_GP_SPEED_DAY;
    @JsonProperty("L1_GP_AVG_DAY")
    private MonitorTypeConfig L1_GP_AVG_DAY;
    @JsonProperty("L1_GP_AVG_WEEK")
    private MonitorTypeConfig L1_GP_AVG_WEEK;
    @JsonProperty("L1_GP_SPEED_HOUR")
    private MonitorTypeConfig L1_GP_SPEED_HOUR;
    @JsonProperty("L1_GP_AC_MONTH")
    private MonitorTypeConfig L1_GP_AC_MONTH;
    @JsonProperty("L1_GP_AC_WEEK")
    private MonitorTypeConfig L1_GP_AC_WEEK;
    @JsonProperty("L1_GP_AC_HOUR")
    private MonitorTypeConfig L1_GP_AC_HOUR;
    @JsonProperty("L1_GP_SPEED_WEEK")
    private MonitorTypeConfig L1_GP_SPEED_WEEK;
    @JsonProperty("L1_GP_VAR")
    private MonitorTypeConfig L1_GP_VAR;

    // Getters and Setters for all L1_GP fields
    public MonitorTypeConfig getL1_GP() { return L1_GP; }
    public void setL1_GP(MonitorTypeConfig value) { this.L1_GP = value; }

    public MonitorTypeConfig getL1_GP_AVG_MONTH() { return L1_GP_AVG_MONTH; }
    public void setL1_GP_AVG_MONTH(MonitorTypeConfig value) { this.L1_GP_AVG_MONTH = value; }

    public MonitorTypeConfig getL1_GP_SPEED_MONTH() { return L1_GP_SPEED_MONTH; }
    public void setL1_GP_SPEED_MONTH(MonitorTypeConfig value) { this.L1_GP_SPEED_MONTH = value; }

    public MonitorTypeConfig getL1_GP_AVG_HOUR() { return L1_GP_AVG_HOUR; }
    public void setL1_GP_AVG_HOUR(MonitorTypeConfig value) { this.L1_GP_AVG_HOUR = value; }

    public MonitorTypeConfig getL1_GP_AC_DAY() { return L1_GP_AC_DAY; }
    public void setL1_GP_AC_DAY(MonitorTypeConfig value) { this.L1_GP_AC_DAY = value; }

    public MonitorTypeConfig getL1_GP_SPEED_DAY() { return L1_GP_SPEED_DAY; }
    public void setL1_GP_SPEED_DAY(MonitorTypeConfig value) { this.L1_GP_SPEED_DAY = value; }

    public MonitorTypeConfig getL1_GP_AVG_DAY() { return L1_GP_AVG_DAY; }
    public void setL1_GP_AVG_DAY(MonitorTypeConfig value) { this.L1_GP_AVG_DAY = value; }

    public MonitorTypeConfig getL1_GP_AVG_WEEK() { return L1_GP_AVG_WEEK; }
    public void setL1_GP_AVG_WEEK(MonitorTypeConfig value) { this.L1_GP_AVG_WEEK = value; }

    public MonitorTypeConfig getL1_GP_SPEED_HOUR() { return L1_GP_SPEED_HOUR; }
    public void setL1_GP_SPEED_HOUR(MonitorTypeConfig value) { this.L1_GP_SPEED_HOUR = value; }

    public MonitorTypeConfig getL1_GP_AC_MONTH() { return L1_GP_AC_MONTH; }
    public void setL1_GP_AC_MONTH(MonitorTypeConfig value) { this.L1_GP_AC_MONTH = value; }

    public MonitorTypeConfig getL1_GP_AC_WEEK() { return L1_GP_AC_WEEK; }
    public void setL1_GP_AC_WEEK(MonitorTypeConfig value) { this.L1_GP_AC_WEEK = value; }

    public MonitorTypeConfig getL1_GP_AC_HOUR() { return L1_GP_AC_HOUR; }
    public void setL1_GP_AC_HOUR(MonitorTypeConfig value) { this.L1_GP_AC_HOUR = value; }

    public MonitorTypeConfig getL1_GP_SPEED_WEEK() { return L1_GP_SPEED_WEEK; }
    public void setL1_GP_SPEED_WEEK(MonitorTypeConfig value) { this.L1_GP_SPEED_WEEK = value; }

    public MonitorTypeConfig getL1_GP_VAR() { return L1_GP_VAR; }
    public void setL1_GP_VAR(MonitorTypeConfig value) { this.L1_GP_VAR = value; }

    public static class MonitorTypeConfig {
        private int filter;
        private String monitorType;
        private boolean isError;
        private boolean isEnabled;
        @JsonProperty("isValueError")
        private Boolean isValueError; // Use Boolean for nullable boolean
        private int statisticType;
        private int count;
        private Rule[] rule;

        // Getters and Setters
        public int getFilter() { return filter; }
        public void setFilter(int value) { this.filter = value; }

        public String getMonitorType() { return monitorType; }
        public void setMonitorType(String value) { this.monitorType = value; }

        public boolean getIsError() { return isError; }
        public void setIsError(boolean value) { this.isError = value; }

        public boolean getIsEnabled() { return isEnabled; }
        public void setIsEnabled(boolean value) { this.isEnabled = value; }

        public Boolean getIsValueError() { return isValueError; }
        public void setIsValueError(Boolean value) { this.isValueError = value; }

        public int getStatisticType() { return statisticType; }
        public void setStatisticType(int value) { this.statisticType = value; }

        public int getCount() { return count; }
        public void setCount(int value) { this.count = value; }

        public Rule[] getRule() { return rule; }
        public void setRule(Rule[] value) { this.rule = value; }
    }

    public static class Rule {
        private String color;
        private int level;
        private int count;
        private InnerRule[] rules;
        private String label;
        private String title;

        // Getters and Setters
        public String getColor() { return color; }
        public void setColor(String value) { this.color = value; }

        public int getLevel() { return level; }
        public void setLevel(int value) { this.level = value; }

        public int getCount() { return count; }
        public void setCount(int value) { this.count = value; }

        public InnerRule[] getRules() { return rules; }
        public void setRules(InnerRule[] value) { this.rules = value; }

        public String getLabel() { return label; }
        public void setLabel(String value) { this.label = value; }

        public String getTitle() { return title; }
        public void setTitle(String value) { this.title = value; }
    }

    public static class InnerRule {
        private boolean isError;
        private String measureItem;
        private String alarmValue;
        private String fieldUnit;
        private String measureItemId;
        private String key;
        private String operator;

        // Getters and Setters
        public boolean getIsError() { return isError; }
        public void setIsError(boolean value) { this.isError = value; }

        public String getMeasureItem() { return measureItem; }
        public void setMeasureItem(String value) { this.measureItem = value; }

        public String getAlarmValue() { return alarmValue; }
        public void setAlarmValue(String value) { this.alarmValue = value; }

        public String getFieldUnit() { return fieldUnit; }
        public void setFieldUnit(String value) { this.fieldUnit = value; }

        public String getMeasureItemId() { return measureItemId; }
        public void setMeasureItemId(String value) { this.measureItemId = value; }

        public String getKey() { return key; }
        public void setKey(String value) { this.key = value; }

        public String getOperator() { return operator; }
        public void setOperator(String value) { this.operator = value; }
    }
} 