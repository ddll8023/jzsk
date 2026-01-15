package com.szy.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 组织机构信息
 * </p>
 *
 * @author l
 * @since 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织机构id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 组织机构名称
     */
    @NotBlank(message = "组织机构名称不能为空")
    private String organizationName;

    /**
     * 组织机构代码
     */
    @NotBlank(message = "组织机构代码不能为空")
    private String organizationCode;

    /**
     * 行政区划名称
     */
    @NotBlank(message = "行政区划名称不能为空")
    private String administrativeName;

    /**
     * 机构简称
     */
    private String organizationAbbr;

    /**
     * 法人代表
     */
    @NotBlank(message = "法人代表不能为空")
    private String legalRepresentative;

    /**
     * 机构规格
     */
    private String agencySpecifications;

    /**
     * 隶属关系
     */
    private String subordinateRelations;

    /**
     * 机构类型
     */
    private String institutionalType;

    /**
     * 主要职能
     */
    @NotBlank(message = "主要职能不能为空")
    private String mainFunction;

    /**
     * 主要审批内容
     */
    private String approveContent;

    /**
     * 网站
     */
    private String website;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮政编码
     */
    private String postalCode;

    /**
     * 办公室电话
     */
    private String officeTelephone;

    /**
     * 传真
     */
    private String fax;

    /**
     * 编制人数
     */
    private Integer staffSize;

    /**
     * 是否实行水务改革
     */
    private String whetherReform;

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
}
