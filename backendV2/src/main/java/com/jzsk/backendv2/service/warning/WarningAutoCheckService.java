package com.jzsk.backendv2.service.warning;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自动预警检查服务接口
 * 职责：封装自动预警的阈值判断和预警生成通用逻辑，供各任务类调用
 */
public interface WarningAutoCheckService {

    /**
     * 检查数值是否超预警阈值，并生成预警信息落库
     *
     * @param position  测点名称
     * @param type      监测类型（水位/雨量/模数等）
     * @param value     当前监测值
     * @param time      监测时间
     * @param level     预警等级（一般预警/严重预警）
     * @param content   预警内容
     * @param longitude 经度（可选）
     * @param latitude  纬度（可选）
     */
    void checkAndInsertWarning(String position, String type, BigDecimal value,
                               LocalDateTime time, String level, String content,
                               BigDecimal longitude, BigDecimal latitude);
}
