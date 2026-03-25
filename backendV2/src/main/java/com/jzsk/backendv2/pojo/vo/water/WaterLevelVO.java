package com.jzsk.backendv2.pojo.vo.water;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 水位视图对象
 * 用途: 返回给前端的水位数据
 */
@Schema(name = "水位VO", description = "水位数据视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterLevelVO {

    @Schema(description = "测站编码", example = "50102300")
    private String stcd;

    @Schema(description = "时间", example = "2026-03-25T14:00:00")
    private LocalDateTime tm;

    @Schema(description = "水位(m)", example = "15.23")
    private BigDecimal z1;

    @Schema(description = "流量(m³/s)", example = "1250.5")
    private BigDecimal q1;
}