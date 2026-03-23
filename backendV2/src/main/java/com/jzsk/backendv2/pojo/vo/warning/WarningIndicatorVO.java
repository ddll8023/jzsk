package com.jzsk.backendv2.pojo.vo.warning;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 预警指标VO
 * 用途：预警指标视图对象，用于返回给前端的数据
 */
@Data
@Schema(name = "预警指标VO", description = "预警指标视图对象")
public class WarningIndicatorVO {

    /** 主键ID */
    @Schema(description = "主键ID", example = "1")
    private Long id;

    /** 监测点 */
    @Schema(description = "监测点", example = "两河口水库")
    private String position;

    /** 监测类型 */
    @Schema(description = "监测类型", example = "水位")
    private String type;

    /** 上上限 */
    @Schema(description = "上上限", example = "100")
    private Double upUpLimit;

    /** 上限 */
    @Schema(description = "上限", example = "70")
    private Double upLimit;

    /** 下限 */
    @Schema(description = "下限", example = "0.1")
    private Double lowLimit;

    /** 下下限 */
    @Schema(description = "下下限", example = "0")
    private Double lowerLimit;

    /** 单位 */
    @Schema(description = "单位", example = "m")
    private String unit;

    /** 经度 */
    @Schema(description = "经度", example = "113.4975840")
    private BigDecimal longitude;

    /** 纬度 */
    @Schema(description = "纬度", example = "31.8838420")
    private BigDecimal latitude;
}
