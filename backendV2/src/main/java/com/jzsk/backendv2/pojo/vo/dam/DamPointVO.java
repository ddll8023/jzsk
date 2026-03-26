package com.jzsk.backendv2.pojo.vo.dam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 大坝监测测点视图对象
 * 用途: 返回给前端的大坝监测测点列表
 */
@Schema(name = "大坝监测测点VO", description = "大坝监测测点视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DamPointVO {

    @Schema(description = "测点ID", example = "1")
    private Long id;

    @Schema(description = "测点编号", example = "P01")
    private String pointId;

    @Schema(description = "测点名称", example = "坝体渗压测点1")
    private String name;
}
