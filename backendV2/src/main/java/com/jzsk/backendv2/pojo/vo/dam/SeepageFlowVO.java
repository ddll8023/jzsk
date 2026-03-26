package com.jzsk.backendv2.pojo.vo.dam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 渗流量视图对象
 * 用途: 返回给前端的渗流量监测数据
 */
@Schema(name = "渗流量VO", description = "渗流量监测数据视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeepageFlowVO {

    @Schema(description = "测点编码", example = "P01")
    private String mpCd;

    @Schema(description = "监测时间", example = "2026-03-25T14:00:00")
    private LocalDateTime tm;

    @Schema(description = "渗流量", example = "0.52")
    private BigDecimal mpFl;

    @Schema(description = "渗流状态", example = "0")
    private String flCond;

    @Schema(description = "测点名称", example = "坝体渗压测点1")
    private String pointName;
}
