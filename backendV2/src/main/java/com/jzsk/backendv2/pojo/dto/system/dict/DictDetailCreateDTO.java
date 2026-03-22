package com.jzsk.backendv2.pojo.dto.system.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Schema(name = "字典详情创建请求", description = "创建字典详情请求参数")
public class DictDetailCreateDTO {

    @NotNull(message = "所属字典ID不能为空")
    @Schema(description = "所属字典ID", example = "1", required = true)
    private Long dictId;

    @NotBlank(message = "字典标签不能为空")
    @Size(max = 100, message = "字典标签长度不能超过100个字符")
    @Schema(description = "字典标签", example = "一级预警", required = true)
    private String label;

    @NotBlank(message = "字典值不能为空")
    @Size(max = 100, message = "字典值长度不能超过100个字符")
    @Schema(description = "字典值", example = "1", required = true)
    private String value;

    @Schema(description = "排序号", example = "1")
    private Integer dictSort;
}
