package com.jzsk.backendv2.pojo.dto.engineering;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 测项更新请求
 * 用途：更新测项信息的请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "测项更新请求", description = "更新测项信息的请求参数")
public class MeasuringItemUpdateDTO {

    @NotNull(message = "测项ID不能为空")
    @Schema(description = "测项ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotBlank(message = "测项编号不能为空")
    @Size(max = 255, message = "测项编号长度不能超过255个字符")
    @Schema(description = "测项编号", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String number;

    @NotBlank(message = "测项名称不能为空")
    @Size(max = 255, message = "测项名称长度不能超过255个字符")
    @Schema(description = "测项名称", example = "水位", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 255, message = "测项单位长度不能超过255个字符")
    @Schema(description = "测项单位", example = "m", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String unit;
}
