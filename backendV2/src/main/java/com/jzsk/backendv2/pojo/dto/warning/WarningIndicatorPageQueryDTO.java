package com.jzsk.backendv2.pojo.dto.warning;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 预警指标分页查询参数
 * 用途：预警指标分页查询的请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "预警指标分页查询参数", description = "预警指标分页查询的请求参数")
public class WarningIndicatorPageQueryDTO extends BasePageQueryDTO {

    /** 监测类型（精确匹配） */
    @Schema(description = "监测类型", example = "水位")
    private String type;

    /** 监测点（模糊匹配） */
    @Schema(description = "监测点", example = "水库")
    private String position;
}
