package com.szy.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 人员实体
 */
@Data
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 人员ID */
    private Long id;

    /** 姓名 */
    private String name;

    /** 年龄 */
    private Integer age;

    /** 性别 */
    private String gender;

    /** 电话 */
    private String phone;

    /** 所属机构 */
    private String organization;

    /** 职位 */
    private String position;

    /** 职责 */
    private String duty;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}
