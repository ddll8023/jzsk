package com.jzsk.backendv2.pojo.vo.dam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 最新水位高程视图对象
 * 用途: 返回给前端的各测点最新水位高程数据
 */
@Schema(name = "最新水位高程VO", description = "各测点最新水位高程数据视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LatestWaterElevationVO {

    @Schema(description = "测点编号", example = "P01")
    private String pointId;

    @Schema(description = "测点名称", example = "坝体渗压测点1")
    private String pointName;

    @Schema(description = "水位高程", example = "15.23")
    private Double waterElevation;

    @Schema(description = "监测时间", example = "2026-03-25T14:00:00")
    private LocalDateTime time;
}
