package com.jzsk.backendv2.pojo.vo.dam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 时序数据视图对象
 * 用途: 返回给前端的水位、温度、水压等时序监测数据
 */
@Schema(name = "时序数据VO", description = "时序监测数据视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSeriesVO {

    @Schema(description = "监测时间", example = "2026-03-25T14:00:00")
    private LocalDateTime time;

    @Schema(description = "监测值", example = "15.23")
    private Double value;

    @Schema(description = "测点名称", example = "坝体渗压测点1")
    private String pointName;
}
