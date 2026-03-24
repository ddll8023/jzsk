package com.jzsk.backendv2.pojo.vo.engineering;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 监测站点下拉选项视图对象
 * 用途：下拉选择框的精简返回数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "监测站点下拉选项", description = "下拉选择框的精简返回数据")
public class MeasuringStationOptionVO {

    @Schema(description = "监测站点ID", example = "1")
    private Long id;

    @Schema(description = "站点名称", example = "坝前水位雨量站")
    private String name;
}
