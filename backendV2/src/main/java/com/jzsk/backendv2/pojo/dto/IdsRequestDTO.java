package com.jzsk.backendv2.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Schema(name = "批量ID动作请求", description = "批量删除、批量处理类接口的通用请求参数")
public class IdsRequestDTO {

    @NotEmpty(message = "ID列表不能为空")
    @Schema(description = "主键ID列表", required = true)
    private List<Long> ids;
}
