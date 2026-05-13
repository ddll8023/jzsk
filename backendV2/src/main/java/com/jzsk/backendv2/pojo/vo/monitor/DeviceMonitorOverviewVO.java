package com.jzsk.backendv2.pojo.vo.monitor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 设备监控总览视图对象
 * 用途: 返回设备监控页面的到报统计数据和设备列表
 */
@Schema(name = "设备监控总览VO", description = "设备监控总览视图对象，包含各类型到报统计和设备列表")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceMonitorOverviewVO {

    @Schema(description = "各设备类型统计")
    private TypeStats overview;

    @Schema(description = "所有设备状态列表")
    private List<DeviceStatusVO> devices;

    /**
     * 设备类型统计
     */
    @Schema(name = "设备类型统计", description = "按设备类型分组的统计数据")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TypeStats {

        @Schema(description = "GNSS统计")
        private Stats gnss;

        @Schema(description = "雨水情统计")
        private Stats rain;

        @Schema(description = "渗流渗压统计")
        private Stats seepage;
    }

    /**
     * 单类型统计
     */
    @Schema(name = "设备统计", description = "单个设备类型的统计数据")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stats {

        @Schema(description = "总数", example = "8")
        private int total;

        @Schema(description = "已到报数", example = "7")
        private int online;

        @Schema(description = "未到报数", example = "0")
        private int offline;

        @Schema(description = "采集异常数", example = "1")
        private int abnormal;
    }
}
