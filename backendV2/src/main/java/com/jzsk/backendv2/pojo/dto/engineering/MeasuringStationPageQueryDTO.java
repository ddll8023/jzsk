package com.jzsk.backendv2.pojo.dto.engineering;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;

/**
 * 监测站点分页查询请求
 * 用途：分页查询监测站点列表的请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "监测站点分页查询请求", description = "分页查询监测站点列表的请求参数")
public class MeasuringStationPageQueryDTO extends BasePageQueryDTO {

    @Size(max = 100, message = "站点名称长度不能超过100个字符")
    @Schema(description = "站点名称（模糊搜索）", example = "坝前", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;
}
