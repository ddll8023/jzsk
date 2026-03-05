package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 人员查询参数
 */
@Schema(name = "人员查询DTO", description = "人员列表查询请求参数")
@Data
public class PersonQueryDTO {

    @Schema(description = "当前页", example = "1", required = true)
    private Integer currentPage;

    @Schema(description = "每页条数", example = "10", required = true)
    private Integer pageSize;

    @Schema(description = "姓名（模糊搜索）", example = "张", required = false)
    private String name;
}
