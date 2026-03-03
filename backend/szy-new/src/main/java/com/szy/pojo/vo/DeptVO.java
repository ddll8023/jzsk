package com.szy.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * 部门响应对象
 */
@Data
public class DeptVO {

    /** 部门ID */
    private Long id;

    /** 部门名称 */
    private String departmentName;

    /** 部门职责 */
    private String departmentResponsibility;

    /** 部门级别 */
    private String level;

    /** 所属公司 */
    private String company;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;
}
