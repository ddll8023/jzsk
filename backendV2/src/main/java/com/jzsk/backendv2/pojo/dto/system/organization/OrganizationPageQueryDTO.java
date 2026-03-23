package com.jzsk.backendv2.pojo.dto.system.organization;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织机构分页查询请求
 * 用途：分页查询组织机构列表的请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "组织机构分页查询请求", description = "分页查询组织机构列表的请求参数")
public class OrganizationPageQueryDTO extends BasePageQueryDTO {

    @Schema(description = "组织机构名称（模糊搜索）", example = "组织部")
    private String name;

    @Schema(description = "行政区划名称（模糊搜索）", example = "湖北省")
    private String administrativeName;
}
