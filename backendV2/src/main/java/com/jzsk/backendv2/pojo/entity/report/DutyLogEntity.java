package com.jzsk.backendv2.pojo.entity.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 值班日志实体类
 * 对应数据库表：duty_log
 * 用途：值班日志的数据模型映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DutyLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 值班日志ID（主键） */
    private Integer dutyLogId;

    /** 值班日期 */
    private LocalDate dutyDate;

    /** 天气 */
    private String weather;

    /** 雨量（毫米） */
    private BigDecimal rainfall;

    /** 带班领导 */
    private String leader;

    /** 白班值班人员 */
    private String dayShiftPerson;

    /** 晚班值班人员 */
    private String nightShiftPerson;

    /** 日志内容 */
    private String logContent;

    /** 日志填写时间 */
    private LocalDateTime fillTime;

    /** 日志状态 */
    private String logStatus;
}
