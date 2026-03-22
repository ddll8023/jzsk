package com.jzsk.backendv2.pojo.entity.system;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典实体类
 * 对应数据库表：dict
 * 用途：字典的数据模型映射
 */
@Data
public class DictEntity {

    /** 字典ID（主键，数据库自增） */
    private Long id;

    /** 字典名称 */
    private String name;

    /** 字典描述 */
    private String description;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 字典详情列表（关联查询） */
    private List<DictDetailEntity> dictDetails = new ArrayList<>();
}
