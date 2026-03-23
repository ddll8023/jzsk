package com.jzsk.backendv2.pojo.vo.system.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 组织机构VO
 * 用途：组织机构视图对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "组织机构VO", description = "组织机构视图对象")
public class OrganizationVO {

    @Schema(description = "组织机构ID", example = "1")
    private Long id;

    @Schema(description = "组织机构名称", example = "希望村组织部")
    private String organizationName;

    @Schema(description = "组织机构代码", example = "421343")
    private String organizationCode;

    @Schema(description = "行政区划名称", example = "湖北省随州市")
    private String administrativeName;

    @Schema(description = "机构简称", example = "组织部")
    private String organizationAbbr;

    @Schema(description = "法人代表", example = "张三")
    private String legalRepresentative;

    @Schema(description = "机构规格", example = "200")
    private String agencySpecifications;

    @Schema(description = "隶属关系", example = "希望村部门")
    private String subordinateRelations;

    @Schema(description = "机构类型", example = "组织部")
    private String institutionalType;

    @Schema(description = "主要职能", example = "组织希望村设备检查活动")
    private String mainFunction;

    @Schema(description = "主要审批内容", example = "设备是否合格")
    private String approveContent;

    @Schema(description = "网站", example = "http://xxxwww.com")
    private String website;

    @Schema(description = "邮箱", example = "xxxwww@qq.com")
    private String email;

    @Schema(description = "地址", example = "湖北省随州市曾都区")
    private String address;

    @Schema(description = "邮政编码", example = "421343")
    private String postalCode;

    @Schema(description = "办公室电话", example = "13648965236")
    private String officeTelephone;

    @Schema(description = "传真", example = "123456789")
    private String fax;

    @Schema(description = "编制人数", example = "200")
    private Integer staffSize;

    @Schema(description = "是否实行水务改革", example = "是")
    private String whetherReform;

    @Schema(description = "创建时间", example = "2022-03-04 16:24:34")
    private LocalDateTime createTime;

    @Schema(description = "修改时间", example = "2025-01-15 11:42:40")
    private LocalDateTime updateTime;
}
