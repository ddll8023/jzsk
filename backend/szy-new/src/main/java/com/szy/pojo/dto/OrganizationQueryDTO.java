package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 组织机构查询DTO
 */
@Data
@Schema(description = "组织机构查询")
public class OrganizationQueryDTO {

    @Schema(description = "当前页", example = "1")
    private Integer currentPage = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "机构名称（搜索关键字）", example = "水利局")
    private String name;
}
