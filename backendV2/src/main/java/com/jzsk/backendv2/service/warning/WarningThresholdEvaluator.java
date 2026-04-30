package com.jzsk.backendv2.service.warning;

import com.jzsk.backendv2.pojo.entity.warning.WarningIndicatorEntity;
import com.jzsk.backendv2.pojo.enums.WarningLevel;
import org.springframework.stereotype.Component;

/**
 * 预警阈值判断组件
 * 职责：统一自动预警任务的阈值判断顺序和等级口径
 */
@Component
public class WarningThresholdEvaluator {

    /**
     * 根据预警指标和监测值计算预警结果
     *
     * @param setting 预警指标
     * @param value   监测值
     * @param type    监测类型
     * @return 超阈值时返回预警结果，未超阈值时返回null
     */
    public WarningThresholdResult evaluate(WarningIndicatorEntity setting, double value, String type) {
        if (setting == null || type == null) {
            return null;
        }
        if (setting.getUpUpLimit() != null && value > setting.getUpUpLimit()) {
            return new WarningThresholdResult(
                    WarningLevel.SERIOUS.getDescription(),
                    type + "超上上限，当前值：" + value);
        }
        if (setting.getUpLimit() != null && value > setting.getUpLimit()) {
            return new WarningThresholdResult(
                    WarningLevel.GENERAL.getDescription(),
                    type + "超上限，当前值：" + value);
        }
        if (setting.getLowerLimit() != null && value < setting.getLowerLimit()) {
            return new WarningThresholdResult(
                    WarningLevel.SERIOUS.getDescription(),
                    type + "低于下下限，当前值：" + value);
        }
        if (setting.getLowLimit() != null && value < setting.getLowLimit()) {
            return new WarningThresholdResult(
                    WarningLevel.GENERAL.getDescription(),
                    type + "低于下限，当前值：" + value);
        }
        return null;
    }

    /**
     * 阈值判断结果
     */
    public static class WarningThresholdResult {

        private final String level;

        private final String content;

        public WarningThresholdResult(String level, String content) {
            this.level = level;
            this.content = content;
        }

        public String getLevel() {
            return level;
        }

        public String getContent() {
            return content;
        }
    }
}
