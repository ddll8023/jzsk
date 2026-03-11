package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 字典请求参数
 */
@Schema(name = "字典DTO", description = "字典创建/更新请求参数")
@Data
public class DictDTO {

    @Schema(description = "字典ID", example = "1", required = false)
    private Long id;

    @Schema(description = "数据项名称", example = "监测站类型", required = true)
    @NotBlank(message = "数据项名称不能为空")
    @Size(max = 100, message = "数据项名称长度不能超过100个字符")
    private String name;

    @Schema(description = "描述", example = "监测站的类型分类", required = false)
    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;
}
