package com.jzsk.backendv2.pojo.dto.water;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 水位分页查询参数
 * 用途: 分页查询水位数据的筛选条件
 * 遵循规范: 与 HourlyRainfallPageQueryDTO 保持一致的设计风格
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "水位分页查询参数", description = "水位分页查询参数")
public class WaterLevelPageQueryDTO extends BasePageQueryDTO {

    @Schema(description = "测站编码", example = "50102300")
    private String stcd;

    @Schema(description = "开始时间", example = "2026-03-01T00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Schema(description = "结束时间", example = "2026-03-25T23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}
