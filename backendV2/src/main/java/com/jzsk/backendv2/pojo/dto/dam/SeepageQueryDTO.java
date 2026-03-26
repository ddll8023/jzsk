package com.jzsk.backendv2.pojo.dto.dam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 渗压数据分页查询请求DTO
 * 用途：查询渗压监测数据的筛选条件
 */
@Schema(name = "渗压数据分页查询请求", description = "查询渗压监测数据的筛选条件")
@Data
public class SeepageQueryDTO {

    @Schema(description = "测点编号", example = "P01")
    @Size(max = 50, message = "测点编号长度不能超过50个字符")
    private String pointId;

    @Schema(description = "开始时间", example = "2026-03-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "结束时间", example = "2026-03-26 23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "测站编码", example = "50102300")
    @Size(max = 20, message = "测站编码长度不能超过20个字符")
    private String stcd;
}
