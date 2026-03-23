package com.jzsk.backendv2.pojo.vo.warning;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预警设施VO
 * 用途：预警设施视图对象，用于返回给前端的数据
 */
@Data
@Schema(name = "预警设施VO", description = "预警设施视图对象")
public class WarningFacilityVO {

    /** 预警设施ID */
    @Schema(description = "预警设施ID", example = "1")
    private Long id;

    /** 设施名称 */
    @Schema(description = "设施名称", example = "两河口预警站")
    private String facilityName;

    /** 类型 */
    @Schema(description = "类型", example = "水位监测站")
    private String type;

    /** 位置 */
    @Schema(description = "位置", example = "两河口水库大坝")
    private String location;

    /** 状态 */
    @Schema(description = "状态", example = "正常")
    private String status;

    /** 负责人 */
    @Schema(description = "负责人", example = "张三")
    private String manager;

    /** 最后维护时间 */
    @Schema(description = "最后维护时间", example = "2025-01-15 10:30:00")
    private LocalDateTime lastUpdate;

    /** 建档时间 */
    @Schema(description = "建档时间", example = "2024-06-01 08:00:00")
    private LocalDateTime recordTime;
}
