package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典查询参数
 */
@Schema(name = "字典查询DTO", description = "字典列表查询请求参数")
@Data
public class DictQueryDTO {

    @Schema(description = "当前页", example = "1", required = true)
    private Integer currentPage;

    @Schema(description = "每页条数", example = "10", required = true)
    private Integer pageSize;

    @Schema(description = "关键字（模糊搜索）", example = "监测", required = false)
    private String blurry;
}
