package com.jzsk.backendv2.pojo.dto.external.latestmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 外部平台GNSS位移监测接口响应包装
 * 对应外部平台 /manager/project/latestMonitoringDataStatistics/{projectId} 接口的完整响应体
 */
@Data
public class LatestMonitorApiResponse {

    /** 业务状态码 */
    @JsonProperty("code")
    private Long code;

    /** 监测站点数据数组 */
    @JsonProperty("data")
    private LatestMonitorStationDto[] data;

    /** 响应消息 */
    @JsonProperty("msg")
    private String msg;

    /** 是否成功 */
    @JsonProperty("success")
    private Boolean success;
}
