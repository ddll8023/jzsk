package com.jzsk.backendv2.pojo.dto.external.latestmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * GNSS位移监测站点数据传输对象
 * 对应外部平台 /manager/project/latestMonitoringDataStatistics 接口返回的 data 数组元素
 * 遵循旧后端契约，不修改字段语义
 */
@Data
public class LatestMonitorStationDto {

    /** 预警等级 */
    @JsonProperty("alarmLevel")
    private Long alarmLevel;

    /** 预警状态 */
    @JsonProperty("alarmState")
    private Long alarmState;

    /** 绑定设备 */
    @JsonProperty("bindDevice")
    private String bindDevice;

    /** 采集时间 */
    @JsonProperty("collectTime")
    private String collectTime;

    /** 设备视频列表 */
    @JsonProperty("deviceVideoList")
    private Object deviceVideoList;

    /** 高程 */
    @JsonProperty("h")
    private String h;

    /** 图标 */
    @JsonProperty("icon")
    private String icon;

    /** 3D图标 */
    @JsonProperty("icon3d")
    private String icon3d;

    /** 图片 */
    @JsonProperty("image")
    private String image;

    /** 纬度 */
    @JsonProperty("lat")
    private String lat;

    /** 经度 */
    @JsonProperty("lon")
    private String lon;

    /** 监测数据日期（JSON字符串，解析后为传感器数据数组） */
    @JsonProperty("monDate")
    private String monDate;

    /** 站点名称（冗余字段） */
    @JsonProperty("name")
    private String name;

    /** 项目ID */
    @JsonProperty("projectId")
    private Long projectId;

    /** 备注信息 */
    @JsonProperty("remark")
    private RemarkDto remark;

    /** 站点ID */
    @JsonProperty("stationId")
    private Long stationId;

    /** 监测方式 */
    @JsonProperty("stationMethods")
    private String stationMethods;

    /** 站点名称 */
    @JsonProperty("stationName")
    private String stationName;

    /** 站点类型 */
    @JsonProperty("stationType")
    private Long stationType;

    /** 站点类型编码 */
    @JsonProperty("stationTypeCode")
    private Long stationTypeCode;

    /** 站点动态值 */
    @JsonProperty("stationDynamicValue")
    private String stationDynamicValue;

    /** 工作状态 */
    @JsonProperty("workStatus")
    private Long workStatus;

    /** 绑定传感器 */
    @JsonProperty("bindSensor")
    private String bindSensor;
}
