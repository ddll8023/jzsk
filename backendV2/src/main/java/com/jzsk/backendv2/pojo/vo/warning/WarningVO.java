package com.jzsk.backendv2.pojo.vo.warning;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预警信息视图对象
 * 用途：预警信息列表和详情返回
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "WarningVO", description = "预警信息视图对象，用于列表和详情返回")
public class WarningVO {

    @Schema(description = "预警信息ID", example = "1")
    private Long id;

    @Schema(description = "预警地点", example = "LJ1-1")
    private String position;

    @Schema(description = "所属工程", example = "鄂北水资源供水工程")
    private String project;

    @Schema(description = "预警内容", example = "水位超过警戒线")
    private String content;

    @Schema(description = "预警类型", example = "水位预警")
    private String type;

    @Schema(description = "预警等级", example = "一般预警")
    private String level;

    @Schema(description = "预警状态", example = "未解除")
    private String status;

    @Schema(description = "经度", example = "112.5")
    private BigDecimal longitude;

    @Schema(description = "纬度", example = "31.2")
    private BigDecimal latitude;

    @Schema(description = "发生时间", example = "2024-06-01 10:00:00")
    private Date startTime;

    @Schema(description = "解除时间", example = "2024-06-01 12:00:00")
    private Date overTime;

    @Schema(description = "持续时长", example = "0天2小时0分钟")
    private String stayTime;
}
