package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 字典详情请求参数
 */
@Schema(name = "字典详情DTO", description = "字典详情创建/更新请求参数")
@Data
public class DictDetailDTO {

    @Schema(description = "详情ID", example = "1", required = false)
    private Long id;

    @Schema(description = "字典ID", example = "1", required = true)
    @NotNull(message = "字典ID不能为空")
    private Long dictId;

    @Schema(description = "显示标签", example = "水库", required = true)
    @NotBlank(message = "显示标签不能为空")
    @Size(max = 100, message = "显示标签长度不能超过100个字符")
    private String label;

    @Schema(description = "值", example = "reservoir", required = true)
    @NotBlank(message = "值不能为空")
    @Size(max = 100, message = "值长度不能超过100个字符")
    private String value;

    @Schema(description = "排序", example = "1", required = false)
    private Integer dictSort;
}
