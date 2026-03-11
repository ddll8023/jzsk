package com.szy.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典详情实体类
 */
@Data
public class DictDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 详情ID */
    private Long id;

    /** 字典ID */
    private Long dictId;

    /** 显示标签 */
    private String label;

    /** 值 */
    private String value;

    /** 排序 */
    private Integer dictSort;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;
}
