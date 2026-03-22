package com.jzsk.backendv2.pojo.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 * 对应数据库表：sys_user
 * 用途：用户数据模型映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID（主键，数据库自增） */
    private Long id;

    /** 用户名（唯一） */
    private String username;

    /** 密码（BCrypt加密存储） */
    private String password;

    /** 姓名 */
    private String name;

    /** 所属部门 */
    private String department;

    /** 用户类型：超级管理员/数据维护人员/只读用户 */
    private String type;

    /** 性别 */
    private String gender;

    /** 身份证号码 */
    private String idNumber;

    /** 岗位 */
    private String position;

    /** 技术职称 */
    private String technicalTitle;

    /** 学历 */
    private String academicQualifications;

    /** 失效时间 */
    private Date expirationTime;

    /** 手机号码 */
    private String phoneNumber;

    /** 工作时间 */
    private Date workingTime;

    /** 毕业院校 */
    private String graduationInstitution;

    /** 专业 */
    private String major;

    /** 家庭住址 */
    private String address;

    /** 出生地 */
    private String birthplace;

    /** 民族 */
    private String ethnicity;

    /** 电子邮件 */
    private String email;

    /** 出生年月 */
    private String birthday;

    /** 政治面貌 */
    private String politicalAppearance;

    /** 备注 */
    private String note;

    /** 排序号 */
    private Integer userOrder;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;

    /** 状态：1启用 0禁用 */
    private Integer status;
}
