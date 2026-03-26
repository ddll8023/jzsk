package com.jzsk.backendv2.pojo.vo.dam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 位移键值对视图对象
 * 用途: 返回给前端的位移监测数据中的键值对信息
 */
@Schema(name = "位移键值对VO", description = "位移监测数据键值对视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplacementKeyValueVO {

    @Schema(description = "传感器名称", example = "水平位移传感器")
    private String sensorName;

    @Schema(description = "传感器编码", example = "L1_GP")
    private String sensor;

    @Schema(description = "值", example = "12.5")
    private String value;

    @Schema(description = "键", example = "displacement_x")
    private String key;
}
