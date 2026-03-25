package com.jzsk.backendv2.pojo.dto.water;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 水位查询请求DTO
 * 用途: 查询水位数据的筛选条件
 */
@Schema(name = "水位查询请求", description = "查询水位数据的筛选条件")
@Data
public class WaterLevelQueryDTO {

    @Schema(description = "测站编码", example = "50102300")
    private String stcd;
}