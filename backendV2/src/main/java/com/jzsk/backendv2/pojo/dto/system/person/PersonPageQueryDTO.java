package com.jzsk.backendv2.pojo.dto.system.person;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人员信息分页查询请求
 * 用途：分页查询人员信息列表的请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "人员信息分页查询请求", description = "分页查询人员信息列表的请求参数")
public class PersonPageQueryDTO extends BasePageQueryDTO {

    @Schema(description = "姓名（模糊搜索）", example = "张")
    private String name;
}
