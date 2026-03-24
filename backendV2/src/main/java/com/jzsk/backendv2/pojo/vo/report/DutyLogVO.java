package com.jzsk.backendv2.pojo.vo.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 值班日志响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "值班日志响应", description = "值班日志信息")
public class DutyLogVO {

    @Schema(description = "值班日志ID", example = "1")
    private Integer dutyLogId;

    @Schema(description = "值班日期", example = "2025-06-01")
    private LocalDate dutyDate;

    @Schema(description = "天气", example = "晴")
    private String weather;

    @Schema(description = "雨量（毫米）", example = "0.00")
    private BigDecimal rainfall;

    @Schema(description = "带班领导", example = "李四")
    private String leader;

    @Schema(description = "白班值班人员", example = "张三")
    private String dayShiftPerson;

    @Schema(description = "晚班值班人员", example = "王五")
    private String nightShiftPerson;

    @Schema(description = "日志内容", example = "一切正常，无异常情况。")
    private String logContent;

    @Schema(description = "日志填写时间", example = "2025-05-25 15:55:39")
    private LocalDateTime fillTime;

    @Schema(description = "日志状态", example = "已填写")
    private String logStatus;
}
