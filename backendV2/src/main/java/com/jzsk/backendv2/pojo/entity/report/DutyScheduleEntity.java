package com.jzsk.backendv2.pojo.entity.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 值班安排实体类
 * 对应数据库表：duty_schedule
 * 用途：值班安排的数据模型映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DutyScheduleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 值班安排ID（主键） */
    private Integer dutyScheduleId;

    /** 值班人员 */
    private String dutyPerson;

    /** 带班领导 */
    private String leader;

    /** 值班时间 */
    private LocalDateTime dutyTime;

    /** 值班岗位 */
    private String dutyPost;

    /** 创建时间 */
    private LocalDateTime createTime;
}
