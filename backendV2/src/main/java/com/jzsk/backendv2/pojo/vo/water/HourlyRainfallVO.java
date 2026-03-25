package com.jzsk.backendv2.pojo.vo.water;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 小时雨量视图对象
 * 用途: 返回给前端的小时雨量数据
 */
@Schema(name = "小时雨量VO", description = "小时雨量数据视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyRainfallVO {

    @Schema(description = "站码", example = "50102300")
    private String stcd;

    @Schema(description = "时间", example = "2026-03-25T14:00:00")
    private LocalDateTime tm;

    @Schema(description = "降水量(mm)", example = "12.5")
    private BigDecimal drp;

    @Schema(description = "时段长", example = "1.0")
    private BigDecimal intv;

    @Schema(description = "日雨量(mm)", example = "35.2")
    private BigDecimal pdr;

    @Schema(description = "天雨量(mm)", example = "42.0")
    private BigDecimal dyp;

    @Schema(description = "测站类型", example = "P")
    private String wth;
}