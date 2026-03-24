package com.jzsk.backendv2.pojo.dto.report;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 值班日志分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "值班日志分页查询参数", description = "值班日志分页查询参数")
public class DutyLogPageQueryDTO extends BasePageQueryDTO {

    @Schema(description = "开始日期", example = "2025-01-01")
    private String startDate;

    @Schema(description = "结束日期", example = "2025-12-31")
    private String endDate;
}
