package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户查询参数
 */
@Schema(name = "用户查询DTO", description = "用户列表查询请求参数")
@Data
public class UserQueryDTO {

    @Schema(description = "当前页", example = "1", required = false)
    private Integer currentPage;

    @Schema(description = "每页条数", example = "10", required = false)
    private Integer pageSize;

    @Schema(description = "用户名（模糊搜索）", example = "admin", required = false)
    private String username;

    @Schema(description = "姓名（模糊搜索）", example = "张", required = false)
    private String name;
}
