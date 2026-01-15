package com.szy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用于记录值班安排的表
 * </p>
 *
 * @author l
 * @since 2025-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DutySchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 值班安排的唯一标识
     */
    @TableId(value = "值班安排ID", type = IdType.AUTO)
    private Integer 值班安排id;

    /**
     * 值班人员的姓名或标识
     */
    private String 值班人员;

    /**
     * 带班领导的姓名或标识
     */
    private String 带班领导;

    /**
     * 值班的具体时间
     */
    private LocalDateTime 值班时间;

    /**
     * 值班人员所在的岗位
     */
    private String 值班岗位;

    /**
     * 值班安排的创建时间
     */
    private LocalDateTime 创建时间;


}
