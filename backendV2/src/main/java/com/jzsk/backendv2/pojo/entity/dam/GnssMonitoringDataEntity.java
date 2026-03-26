package com.jzsk.backendv2.pojo.entity.dam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * GNSS监测数据实体
 * 对应数据库表：gnss_monitoring_data（SQL Server，数据源：gcdd）
 * 用途：存储GNSS位移监测数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GnssMonitoringDataEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 站点ID */
    private String stationId;

    /** 站点名称 */
    private String stationName;

    /** 经度 */
    private Double longitude;

    /** 纬度 */
    private Double latitude;

    /** 高程 */
    private Double elevation;

    /** X方向位移 */
    private Double displacementX;

    /** Y方向位移 */
    private Double displacementY;

    /** Z方向位移 */
    private Double displacementZ;

    /** 监测时间 */
    private LocalDateTime monitorTime;

    /** 创建时间 */
    private LocalDateTime createTime;
}
