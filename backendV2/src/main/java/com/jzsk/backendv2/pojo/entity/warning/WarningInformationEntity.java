package com.jzsk.backendv2.pojo.entity.warning;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预警信息实体
 * 数据库表：warning_information（数据源：yjxx）
 * 说明：物理删除，不使用逻辑删除（数据库表无 deleted 字段）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "WarningInformationEntity", description = "预警信息实体，对应 yjxx.warning_information 表")
public class WarningInformationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预警信息ID
     */
    @Schema(description = "预警信息ID", example = "1")
    private Long id;

    /**
     * 监测地点
     */
    @Schema(description = "监测地点", example = "LJ1-1")
    private String position;

    /**
     * 所属工程
     */
    @Schema(description = "所属工程", example = "鄂北水资源供水工程")
    private String project;

    /**
     * 预警内容
     */
    @Schema(description = "预警内容", example = "水位超过警戒线")
    private String content;

    /**
     * 预警类型
     */
    @Schema(description = "预警类型", example = "水位预警")
    private String type;

    /**
     * 预警等级
     */
    @Schema(description = "预警等级", example = "一般预警")
    private String level;

    /**
     * 预警状态
     */
    @Schema(description = "预警状态", example = "未解除")
    private String status;

    /**
     * 经度
     */
    @Schema(description = "经度", example = "112.5")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Schema(description = "纬度", example = "31.2")
    private BigDecimal latitude;

    /**
     * 发生时间
     */
    @Schema(description = "发生时间", example = "2024-06-01 10:00:00")
    private Date startTime;

    /**
     * 解除时间
     */
    @Schema(description = "解除时间", example = "2024-06-01 12:00:00")
    private Date overTime;

    /**
     * 持续时长
     */
    @Schema(description = "持续时长", example = "0天2小时0分钟")
    private String stayTime;

    /**
     * 几何点位（内部使用，不对外暴露）
     */
    @JsonIgnore
    @Schema(description = "几何点位，内部使用", example = "Point(112.5 31.2)")
    private String point;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2024-06-01 10:00:00")
    private Date createTime;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间", example = "2024-06-01 10:00:00")
    private Date updateTime;
}
