package com.jzsk.backendv2.pojo.dto.water;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 小时雨量分页查询参数
 * 用途: 分页查询小时雨量数据的筛选条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "小时雨量分页查询参数", description = "小时雨量分页查询参数")
public class HourlyRainfallPageQueryDTO extends BasePageQueryDTO {

    @Schema(description = "开始时间", example = "2026-03-01T00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Schema(description = "结束时间", example = "2026-03-25T23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}