package com.jzsk.backendv2.pojo.dto.warning;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预警信息更新请求
 * 用途：更新预警信息（主要用于解除预警操作）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "预警信息更新请求", description = "更新预警信息，用于解除预警等操作")
public class WarningUpdateDTO {

    /**
     * 预警信息ID
     */
    @NotNull(message = "预警ID不能为空")
    @Schema(description = "预警信息ID", example = "1")
    private Long id;

    /**
     * 预警地点
     */
    @Schema(description = "预警地点", example = "LJ1-1")
    @Size(max = 200, message = "预警地点长度不能超过200个字符")
    private String position;

    /**
     * 所属工程
     */
    @Schema(description = "所属工程", example = "鄂北水资源供水工程")
    @Size(max = 200, message = "所属工程长度不能超过200个字符")
    private String project;

    /**
     * 预警内容
     */
    @Schema(description = "预警内容", example = "水位超过警戒线")
    @Size(max = 500, message = "预警内容长度不能超过500个字符")
    private String content;

    /**
     * 预警类型
     */
    @Schema(description = "预警类型", example = "水位预警")
    @Size(max = 50, message = "预警类型长度不能超过50个字符")
    private String type;

    /**
     * 预警等级
     */
    @Schema(description = "预警等级", example = "一般预警")
    @Size(max = 50, message = "预警等级长度不能超过50个字符")
    private String level;

    /**
     * 预警状态
     */
    @Schema(description = "预警状态", example = "已解除")
    @Size(max = 50, message = "预警状态长度不能超过50个字符")
    private String status;

    /**
     * 经度
     */
    @Schema(description = "经度", example = "112.5")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Schema(description = "纬度", example = "31.2")
    private BigDecimal latitude;

    /**
     * 发生时间
     */
    @Schema(description = "发生时间", example = "2024-06-01 10:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 解除时间（解除预警时由后端自动设置为当前时间）
     */
    @Schema(description = "解除时间", example = "2024-06-01 12:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date overTime;
}
