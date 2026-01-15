package com.szy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author l
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id（主键）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 密码
     */
    private String password;

    /**
     * 人员id
     */
    @TableField("personID")
    private Long personid;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 所属的角色，多个角色使用逗号隔开
     */
    @TableField(exist = false)
    private List<Role> roles = new ArrayList<>();

    /**
     * 所属部门
     */
    private String department;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 性别
     */
    private String gender;

    /**
     * 身份证号码
     */
    @TableField("ID_number")
    private String idNumber;

    /**
     * 岗位
     */
    private String position;

    /**
     * 技术职称
     */
    private String technicalTitle;

    /**
     * 学历
     */
    private String academicQualifications;

    /**
     * 失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTime;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 工作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date workingTime;

    /**
     * 毕业院校
     */
    private String graduationInstitution;

    /**
     * 专业
     */
    private String major;

    /**
     * 家庭住址
     */
    private String address;

    /**
     * 出生地
     */
    private String birthplace;

    /**
     * 民族
     */
    private String ethnicity;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 出生年月
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private String birthday;

    /**
     * 政治面貌
     */
    private String politicalAppearance;

    /**
     * 备注
     */
    private String note;

    /**
     * 顺序
     */
    private Integer userOrder;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;
}
