package com.jzsk.backendv2.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Schema(name = "单ID动作请求", description = "删除、重置、单条动作类接口的通用请求参数")
public class IdRequestDTO {

    @NotNull(message = "ID不能为空")
    @Schema(description = "主键ID", example = "1", required = true)
    private Long id;
}
