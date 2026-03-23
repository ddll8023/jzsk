package com.jzsk.backendv2.pojo.dto.warning;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * 预警设施创建请求
 * 用途：创建新预警设施的请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "预警设施创建请求", description = "创建新预警设施的请求参数")
public class WarningFacilityCreateDTO {

    /** 设施名称 */
    @Schema(description = "设施名称", example = "两河口预警站", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设施名称不能为空")
    @Size(max = 100, message = "设施名称长度不能超过100个字符")
    private String facilityName;

    /** 类型 */
    @Schema(description = "类型", example = "水位监测站", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "类型不能为空")
    @Size(max = 50, message = "类型长度不能超过50个字符")
    private String type;

    /** 位置 */
    @Schema(description = "位置", example = "两河口水库大坝", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "位置不能为空")
    @Size(max = 200, message = "位置长度不能超过200个字符")
    private String location;

    /** 状态 */
    @Schema(description = "状态", example = "正常", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "状态不能为空")
    @Size(max = 20, message = "状态长度不能超过20个字符")
    private String status;

    /** 负责人 */
    @Schema(description = "负责人", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "负责人不能为空")
    @Size(max = 50, message = "负责人长度不能超过50个字符")
    private String manager;

    /** 最后维护时间 */
    @Schema(description = "最后维护时间", example = "2025-01-15 10:30:00")
    private LocalDateTime lastUpdate;

    /** 建档时间 */
    @Schema(description = "建档时间", example = "2024-06-01 08:00:00")
    private LocalDateTime recordTime;
}
