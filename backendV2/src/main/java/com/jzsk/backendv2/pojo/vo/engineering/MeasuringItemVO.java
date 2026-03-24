package com.jzsk.backendv2.pojo.vo.engineering;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测项视图对象
 * 用途：测项列表和详情的返回数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "测项视图对象", description = "测项列表和详情的返回数据")
public class MeasuringItemVO {

    @Schema(description = "测项ID", example = "1")
    private Long id;

    @Schema(description = "测项编号", example = "1")
    private String number;

    @Schema(description = "测项名称", example = "水位")
    private String name;

    @Schema(description = "测项单位", example = "m")
    private String unit;
}
