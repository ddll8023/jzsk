package com.jzsk.backendv2.pojo.vo.engineering;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测项下拉选项视图对象
 * 用途：下拉选择框的精简返回数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "测项下拉选项", description = "下拉选择框的精简返回数据")
public class MeasuringItemOptionVO {

    @Schema(description = "测项ID", example = "1")
    private Long id;

    @Schema(description = "测项名称", example = "水位")
    private String name;
}
