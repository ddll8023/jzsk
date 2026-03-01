package com.szy.pojo.dto;

import lombok.Data;

/**
 * 角色查询参数
 */
@Data
public class RoleQueryDTO {

    /** 当前页 */
    private Integer currentPage;

    /** 每页条数 */
    private Integer pageSize;

    /** 角色名称（模糊搜索） */
    private String name;
}