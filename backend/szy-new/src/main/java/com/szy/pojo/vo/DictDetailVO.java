package com.szy.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * 字典详情响应对象
 */
@Data
public class DictDetailVO {

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
