package com.jzsk.backendv2.pojo.dto.warning;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 预警信息分页查询请求
 * 用途：分页查询预警信息列表的请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "预警信息分页查询请求", description = "分页查询预警信息列表，支持按地点、状态、等级、类型、时间范围筛选")
public class WarningPageQueryDTO extends BasePageQueryDTO {

    /**
     * 预警地点（模糊搜索）
     */
    @Schema(description = "预警地点（模糊搜索）", example = "LJ1-1")
    private String position;

    /**
     * 预警状态
     */
    @Schema(description = "预警状态", example = "未解除")
    private String status;

    /**
     * 预警等级
     */
    @Schema(description = "预警等级", example = "一般预警")
    private String level;

    /**
     * 预警类型
     */
    @Schema(description = "预警类型", example = "水位预警")
    private String type;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间", example = "2024-06-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间", example = "2024-06-30 23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
