package com.jzsk.backendv2.pojo.dto.system.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(name = "字典选项查询参数", description = "按字典名称查询选项参数")
public class DictOptionQueryDTO {

    @NotBlank(message = "字典名称不能为空")
    @Size(max = 100, message = "字典名称长度不能超过100个字符")
    @Schema(description = "字典名称", example = "预警等级", required = true)
    private String name;
}
