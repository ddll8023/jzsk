package com.jzsk.backendv2.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@Schema(name = "分页查询参数", description = "通用分页查询参数")
public class BasePageQueryDTO {

    @Schema(description = "页码", example = "1")
    @Min(value = 1L, message = "页码必须大于等于1")
    private long page = 1L;

    @Schema(description = "每页大小（实际上限由各服务定义，建议不超过100）", example = "20")
    @Min(value = 1L, message = "每页大小必须大于等于1")
    private long size = 10L;
}
