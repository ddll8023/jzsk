package com.jzsk.backendv2.pojo.entity.system;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典详情实体类
 * 对应数据库表：dict_detail
 * 用途：字典详情的数据模型映射
 */
@Data
public class DictDetailEntity {

    /** 字典详情ID（主键，数据库自增） */
    private Long id;

    /** 所属字典ID（外键关联dict表） */
    private Long dictId;

    /** 字典标签 */
    private String label;

    /** 字典值 */
    private String value;

    /** 排序号 */
    private Integer dictSort;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
