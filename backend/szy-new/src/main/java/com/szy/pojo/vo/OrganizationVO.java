package com.szy.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 组织机构VO
 */
@Data
@Schema(description = "组织机构响应")
public class OrganizationVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "机构名称")
    private String organizationName;

    @Schema(description = "机构代码")
    private String organizationCode;

    @Schema(description = "行政区划")
    private String administrativeName;

    @Schema(description = "机构简称")
    private String organizationAbbr;

    @Schema(description = "法人代表")
    private String legalRepresentative;

    @Schema(description = "机构规格")
    private String agencySpecifications;

    @Schema(description = "隶属关系")
    private String subordinateRelations;

    @Schema(description = "机构类型")
    private String institutionalType;

    @Schema(description = "主要职能")
    private String mainFunction;

    @Schema(description = "主要审批内容")
    private String approveContent;

    @Schema(description = "网站")
    private String website;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "邮政编码")
    private String postalCode;

    @Schema(description = "办公室电话")
    private String officeTelephone;

    @Schema(description = "传真")
    private String fax;

    @Schema(description = "编制人数")
    private Integer staffSize;

    @Schema(description = "是否水务改革")
    private String whetherReform;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;
}
