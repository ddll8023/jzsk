package com.jzsk.backendv2.pojo.dto.warning;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 预警设施分页查询请求
 * 用途：分页查询预警设施列表的请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "预警设施分页查询请求", description = "分页查询预警设施列表的请求参数")
public class WarningFacilityPageQueryDTO extends BasePageQueryDTO {

    /** 设施名称（模糊匹配） */
    @Schema(description = "设施名称", example = "监控")
    private String facilityName;

    /** 类型（精确匹配） */
    @Schema(description = "类型", example = "水位监测站")
    private String type;

    /** 状态（精确匹配） */
    @Schema(description = "状态", example = "正常")
    private String status;
}
