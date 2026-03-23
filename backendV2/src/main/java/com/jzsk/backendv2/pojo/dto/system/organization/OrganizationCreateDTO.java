package com.jzsk.backendv2.pojo.dto.system.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 组织机构创建请求
 * 用途：创建新组织机构的请求参数
 */
@Data
@Schema(name = "组织机构创建请求", description = "创建新组织机构的请求参数")
public class OrganizationCreateDTO {

    @Schema(description = "组织机构名称", example = "希望村组织部", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "组织机构名称不能为空")
    @Size(max = 255, message = "组织机构名称长度不能超过255个字符")
    private String organizationName;

    @Schema(description = "组织机构代码", example = "421343", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "组织机构代码不能为空")
    @Size(max = 255, message = "组织机构代码长度不能超过255个字符")
    private String organizationCode;

    @Schema(description = "行政区划名称", example = "湖北省随州市", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "行政区划名称不能为空")
    @Size(max = 255, message = "行政区划名称长度不能超过255个字符")
    private String administrativeName;

    @Schema(description = "机构简称", example = "组织部", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "机构简称长度不能超过255个字符")
    private String organizationAbbr;

    @Schema(description = "法人代表", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "法人代表不能为空")
    @Size(max = 255, message = "法人代表长度不能超过255个字符")
    private String legalRepresentative;

    @Schema(description = "机构规格", example = "200", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "机构规格长度不能超过255个字符")
    private String agencySpecifications;

    @Schema(description = "隶属关系", example = "希望村部门", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "隶属关系长度不能超过255个字符")
    private String subordinateRelations;

    @Schema(description = "机构类型", example = "组织部", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "机构类型长度不能超过255个字符")
    private String institutionalType;

    @Schema(description = "主要职能", example = "组织希望村设备检查活动", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "主要职能不能为空")
    @Size(max = 255, message = "主要职能长度不能超过255个字符")
    private String mainFunction;

    @Schema(description = "主要审批内容", example = "设备是否合格", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "主要审批内容长度不能超过255个字符")
    private String approveContent;

    @Schema(description = "网站", example = "http://xxxwww.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "网站长度不能超过255个字符")
    private String website;

    @Schema(description = "邮箱", example = "xxxwww@qq.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Email(message = "邮箱格式不正确")
    @Size(max = 255, message = "邮箱长度不能超过255个字符")
    private String email;

    @Schema(description = "地址", example = "湖北省随州市曾都区", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "地址长度不能超过255个字符")
    private String address;

    @Schema(description = "邮政编码", example = "421343", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "邮政编码长度不能超过255个字符")
    private String postalCode;

    @Schema(description = "办公室电话", example = "13648965236", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "办公室电话长度不能超过255个字符")
    private String officeTelephone;

    @Schema(description = "传真", example = "123456789", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "传真长度不能超过255个字符")
    private String fax;

    @Schema(description = "编制人数", example = "200", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer staffSize;

    @Schema(description = "是否实行水务改革", example = "是", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "是否实行水务改革长度不能超过255个字符")
    private String whetherReform;
}
