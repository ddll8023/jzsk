package com.jzsk.backendv2.pojo.entity.engineering;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 养护记录实体类
 * 对应数据库表：gcdd.maintence_record
 * 数据源：gcdd（通过 @DS 注解切换）
 * 使用 Lombok 简化代码，纯 MyBatis 数据映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRecordsEntity {

    /** 工程记录id（主键，数据库自增） */
    private Long id;

    /** 工程名称 */
    private String name;

    /** 工程代码 */
    private String code;

    /** 备注 */
    private String note;

    /** 负责人 */
    private String responsiblePerson;

    /** 负责人电话 */
    private String phone;

    /** 开始维护时间 */
    private LocalDateTime startTime;

    /** 结束维护时间 */
    private LocalDateTime overTime;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
