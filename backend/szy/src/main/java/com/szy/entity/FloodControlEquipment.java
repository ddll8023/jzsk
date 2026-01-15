package com.szy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 防汛设备管理表
 * </p>
 *
 * @author l
 * @since 2025-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FloodControlEquipment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备唯一标识
     */
    @TableId(value = "equipment_id", type = IdType.AUTO)
    private Integer equipmentId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备类型
     */
    private String equipmentType;

    /**
     * 安装日期
     */
    private LocalDate installationDate;

    /**
     * 上次维护日期
     */
    private LocalDate maintenanceDate;

    /**
     * 下次维护日期
     */
    private LocalDate nextMaintenanceDate;

    /**
     * 设备状态
     */
    private String status;

    /**
     * 设备位置
     */
    private String location;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 记录创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
