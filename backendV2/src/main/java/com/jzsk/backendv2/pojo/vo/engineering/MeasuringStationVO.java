package com.jzsk.backendv2.pojo.vo.engineering;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 监测站点视图对象
 * 用途：监测站点列表和详情的返回数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "监测站点视图对象", description = "监测站点列表和详情的返回数据")
public class MeasuringStationVO {

    @Schema(description = "监测站点ID", example = "1")
    private Long id;

    @Schema(description = "站码", example = "4211820043")
    private String code;

    @Schema(description = "站名", example = "坝前水位雨量站")
    private String name;

    @Schema(description = "水系名称", example = "长江")
    private String waterName;

    @Schema(description = "河流名称", example = "汉江")
    private String riverName;

    @Schema(description = "施测项目码", example = "Q")
    private String monitorCode;

    @Schema(description = "行政区划码", example = "421182")
    private String addressCode;

    @Schema(description = "设站年月", example = "2025-06")
    private String establishDate;

    @Schema(description = "经度", example = "113.4920780")
    private BigDecimal longitude;

    @Schema(description = "纬度", example = "31.8824470")
    private BigDecimal latitude;

    @Schema(description = "备注", example = "新建监测站点")
    private String note;
}
