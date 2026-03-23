package com.jzsk.backendv2.pojo.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 人员信息实体类
 * 对应数据库表：person
 * 用途：人员信息的数据模型映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 人员信息ID（主键，数据库自增） */
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
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
