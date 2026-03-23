package com.jzsk.backendv2.pojo.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组织机构实体类
 * 对应数据库表：organization
 * 用途：组织机构数据模型映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 组织机构ID（主键，数据库自增） */
    private Long id;

    /** 组织机构名称 */
    private String organizationName;

    /** 组织机构代码 */
    private String organizationCode;

    /** 行政区划名称 */
    private String administrativeName;

    /** 机构简称 */
    private String organizationAbbr;

    /** 法人代表 */
    private String legalRepresentative;

    /** 机构规格 */
    private String agencySpecifications;

    /** 隶属关系 */
    private String subordinateRelations;

    /** 机构类型 */
    private String institutionalType;

    /** 主要职能 */
    private String mainFunction;

    /** 主要审批内容 */
    private String approveContent;

    /** 网站 */
    private String website;

    /** 邮箱 */
    private String email;

    /** 地址 */
    private String address;

    /** 邮政编码 */
    private String postalCode;

    /** 办公室电话 */
    private String officeTelephone;

    /** 传真 */
    private String fax;

    /** 编制人数 */
    private Integer staffSize;

    /** 是否实行水务改革 */
    private String whetherReform;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime updateTime;
}
