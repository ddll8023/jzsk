package com.szy.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 字典主表实体类
 */
@Data
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /** 字典详情列表 */
    private List<DictDetail> dictDetails;
}
