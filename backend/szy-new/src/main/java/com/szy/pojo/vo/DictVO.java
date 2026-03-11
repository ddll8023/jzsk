package com.szy.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * 字典响应对象
 */
@Data
public class DictVO {

    /** 字典ID */
    private Long id;

    /** 数据项名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;
}
