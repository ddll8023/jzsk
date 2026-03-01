package com.szy.pojo.dto;

import lombok.Data;

/**
 * 用户查询参数
 */
@Data
public class UserQueryDTO {

    /** 当前页 */
    private Integer currentPage;

    /** 每页条数 */
    private Integer pageSize;

    /** 用户名（模糊搜索） */
    private String username;

    /** 姓名（模糊搜索） */
    private String name;
}