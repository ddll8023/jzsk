package com.jzsk.backendv2.pojo.vo.dam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 位移历史视图对象
 * 用途: 返回给前端的外部位移监测历史数据
 */
@Schema(name = "位移历史VO", description = "外部位移监测历史数据视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplacementHistoryVO {

    @Schema(description = "采集时间", example = "2026-03-25T14:00:00")
    private String collectTime;

    @Schema(description = "方位角", example = "120.5")
    private Double azimuth;

    @Schema(description = "站点名称", example = "GNSS监测站1")
    private String stationName;

    @Schema(description = "键值对列表")
    private List<DisplacementKeyValueVO> keyValues;

    @Schema(description = "设备ID", example = "DEV001")
    private String deviceId;

    @Schema(description = "设备序列号", example = "SN123456")
    private String deviceSn;

    @Schema(description = "站点ID", example = "33210")
    private Long stationId;
}
