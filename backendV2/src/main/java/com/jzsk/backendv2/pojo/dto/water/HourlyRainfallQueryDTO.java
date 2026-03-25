package com.jzsk.backendv2.pojo.dto.water;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 小时雨量查询请求DTO
 * 用途: 查询小时雨量数据的筛选条件
 */
@Schema(name = "小时雨量查询请求", description = "查询小时雨量数据的筛选条件")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyRainfallQueryDTO {

    @Schema(description = "开始时间", example = "2026-03-01T00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Schema(description = "结束时间", example = "2026-03-25T23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}