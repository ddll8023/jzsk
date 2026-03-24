package com.jzsk.backendv2.pojo.dto.engineering;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 测项分页查询请求
 * 用途：分页查询测项列表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "测项分页查询请求", description = "分页查询测项列表")
public class MeasuringItemPageQueryDTO extends BasePageQueryDTO {

    @Schema(description = "测项名称（模糊搜索）", example = "水位", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;
}
