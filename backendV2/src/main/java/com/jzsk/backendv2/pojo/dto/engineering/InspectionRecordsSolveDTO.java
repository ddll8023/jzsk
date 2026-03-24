package com.jzsk.backendv2.pojo.dto.engineering;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 巡检记录处理请求
 * 用途：将巡检记录标记为已处理
 */
@Data
@Schema(name = "巡检记录处理请求", description = "将巡检记录标记为已处理")
public class InspectionRecordsSolveDTO {

    @Schema(description = "巡检记录ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "巡检记录ID不能为空")
    private Long id;
}
