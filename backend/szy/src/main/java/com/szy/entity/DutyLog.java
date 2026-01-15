package com.szy.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用于记录值班日志的表
 * </p>
 *
 * @author l
 * @since 2025-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DutyLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 值班日志的唯一标识
     */
    @TableId(value = "值班日志ID", type = IdType.AUTO)
    private Integer 值班日志id;

    /**
     * 值班的具体日期
     */
    private LocalDate 值班日期;

    /**
     * 当天的天气情况
     */
    private String 天气;

    /**
     * 当天的雨量，单位毫米
     */
    private BigDecimal 雨量;

    /**
     * 带班领导的姓名或标识
     */
    private String 带班领导;

    /**
     * 白班值班人员的姓名或标识
     */
    private String 白班值班人员;

    /**
     * 晚班值班人员的姓名或标识
     */
    private String 晚班值班人员;

    /**
     * 值班日志的具体内容
     */
    private String 日志内容;

    /**
     * 日志填写的时间
     */
    private LocalDateTime 日志填写时间;

    /**
     * 日志的填写状态
     */
    private String 日志状态;


}
