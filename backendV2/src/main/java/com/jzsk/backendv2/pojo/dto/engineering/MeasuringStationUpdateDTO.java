package com.jzsk.backendv2.pojo.dto.engineering;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 监测站点更新请求
 * 用途：更新监测站点信息的请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "监测站点更新请求", description = "更新监测站点信息的请求参数")
public class MeasuringStationUpdateDTO {

    @NotNull(message = "ID不能为空")
    @Schema(description = "监测站点ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotBlank(message = "站码不能为空")
    @Size(max = 255, message = "站码长度不能超过255个字符")
    @Schema(description = "站码", example = "4211820043", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @NotBlank(message = "站名不能为空")
    @Size(max = 255, message = "站名长度不能超过255个字符")
    @Schema(description = "站名", example = "坝前水位站", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 255, message = "水系名称长度不能超过255个字符")
    @Schema(description = "水系名称", example = "长江", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String waterName;

    @Size(max = 255, message = "河流名称长度不能超过255个字符")
    @Schema(description = "河流名称", example = "汉江", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String riverName;

    @Size(max = 3, message = "施测项目码长度不能超过3个字符")
    @Schema(description = "施测项目码", example = "Q", allowableValues = {"Z", "Q", "L", "W", "V"}, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String monitorCode;

    @Size(max = 10, message = "行政区划码长度不能超过10个字符")
    @Schema(description = "行政区划码", example = "421182", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String addressCode;

    @Schema(description = "设站年月（格式：yyyy-MM）", example = "2025-06", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String establishDate;

    @NotNull(message = "经度不能为空")
    @DecimalMin(value = "-180", message = "经度范围：-180到180")
    @DecimalMax(value = "180", message = "经度范围：-180到180")
    @Schema(description = "经度", example = "113.492078", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal longitude;

    @NotNull(message = "纬度不能为空")
    @DecimalMin(value = "-90", message = "纬度范围：-90到90")
    @DecimalMax(value = "90", message = "纬度范围：-90到90")
    @Schema(description = "纬度", example = "31.882447", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal latitude;

    @Size(max = 255, message = "备注长度不能超过255个字符")
    @Schema(description = "备注", example = "更新监测站点信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String note;
}
