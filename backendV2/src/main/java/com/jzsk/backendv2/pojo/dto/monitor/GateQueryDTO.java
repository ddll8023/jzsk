package com.jzsk.backendv2.pojo.dto.monitor;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 闸门数据查询DTO
 * 用于查询指定闸门的数据，支持分页
 */
@Schema(name = "闸门数据查询请求", description = "查询闸门数据的请求参数")
@Data
@EqualsAndHashCode(callSuper = true)
public class GateQueryDTO extends BasePageQueryDTO {

    @Schema(description = "闸门编码", example = "dgq",
            allowableValues = {"dgq", "dzdf", "qst", "xgq", "yhd"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Parameter(description = "闸门编码", required = true, example = "dgq")
    private String gateCode;

    @Schema(description = "开始时间", example = "2024-01-01 00:00:00")
    @Parameter(description = "开始时间", required = false)
    private String startTime;

    @Schema(description = "结束时间", example = "2024-01-02 00:00:00")
    @Parameter(description = "结束时间", required = false)
    private String endTime;
}
