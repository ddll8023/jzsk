package com.jzsk.backendv2.pojo.dto.warning;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 预警指标创建请求
 * 用途：创建新预警指标的请求参数
 */
@Data
@Schema(name = "预警指标创建请求", description = "创建新预警指标的请求参数")
public class WarningIndicatorCreateDTO {

    /** 监测点 */
    @Schema(description = "监测点", example = "两河口水库", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "监测点不能为空")
    private String position;

    /** 监测类型 */
    @Schema(description = "监测类型", example = "水位", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "监测类型不能为空")
    private String type;

    /** 上上限 */
    @Schema(description = "上上限", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "上上限不能为空")
    private Double upUpLimit;

    /** 上限 */
    @Schema(description = "上限", example = "70", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "上限不能为空")
    private Double upLimit;

    /** 下限 */
    @Schema(description = "下限", example = "0.1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "下限不能为空")
    private Double lowLimit;

    /** 下下限 */
    @Schema(description = "下下限", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "下下限不能为空")
    private Double lowerLimit;

    /** 单位 */
    @Schema(description = "单位", example = "m", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "单位不能为空")
    private String unit;

    /** 经度 */
    @Schema(description = "经度", example = "113.4975840")
    private BigDecimal longitude;

    /** 纬度 */
    @Schema(description = "纬度", example = "31.8838420")
    private BigDecimal latitude;
}
