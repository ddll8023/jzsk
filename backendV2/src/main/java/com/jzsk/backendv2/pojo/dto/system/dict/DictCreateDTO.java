package com.jzsk.backendv2.pojo.dto.system.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(name = "字典创建请求", description = "创建字典请求参数")
public class DictCreateDTO {

    @NotBlank(message = "字典名称不能为空")
    @Size(max = 100, message = "字典名称长度不能超过100个字符")
    @Schema(description = "字典名称", example = "预警等级", required = true)
    private String name;

    @Size(max = 255, message = "字典描述长度不能超过255个字符")
    @Schema(description = "字典描述", example = "用于维护预警等级选项")
    private String description;
}
