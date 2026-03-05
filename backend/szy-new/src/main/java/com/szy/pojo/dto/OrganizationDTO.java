package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 组织机构DTO
 */
@Data
@Schema(description = "组织机构请求")
public class OrganizationDTO {

    @Schema(description = "ID（更新时必填）")
    private Long id;

    @NotBlank(message = "机构名称不能为空")
    @Schema(description = "机构名称", example = "XX市水利局")
    private String organizationName;

    @NotBlank(message = "机构代码不能为空")
    @Schema(description = "机构代码", example = "ORG001")
    private String organizationCode;

    @NotBlank(message = "行政区划名称不能为空")
    @Schema(description = "行政区划", example = "XX省XX市")
    private String administrativeName;

    @Schema(description = "机构简称")
    private String organizationAbbr;

    @NotBlank(message = "法人代表不能为空")
    @Schema(description = "法人代表", example = "张三")
    private String legalRepresentative;

    @Schema(description = "机构规格", example = "正处级")
    private String agencySpecifications;

    @Schema(description = "隶属关系")
    private String subordinateRelations;

    @Schema(description = "机构类型")
    private String institutionalType;

    @NotBlank(message = "主要职能不能为空")
    @Schema(description = "主要职能", example = "水利管理")
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
}
