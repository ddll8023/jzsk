package com.szy.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织机构实体
 */
@Data
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 机构名称（必填） */
    private String organizationName;

    /** 机构代码（必填） */
    private String organizationCode;

    /** 行政区划名称（必填） */
    private String administrativeName;

    /** 机构简称 */
    private String organizationAbbr;

    /** 法人代表（必填） */
    private String legalRepresentative;

    /** 机构规格 */
    private String agencySpecifications;

    /** 隶属关系 */
    private String subordinateRelations;

    /** 机构类型 */
    private String institutionalType;

    /** 主要职能（必填） */
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

    /** 是否施行水务改革 */
    private String whetherReform;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;
}
