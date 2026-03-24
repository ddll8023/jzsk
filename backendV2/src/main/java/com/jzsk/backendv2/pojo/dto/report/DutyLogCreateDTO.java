package com.jzsk.backendv2.pojo.dto.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 值班日志创建请求
 */
@Data
@Schema(name = "值班日志创建请求", description = "创建值班日志的请求参数")
public class DutyLogCreateDTO {

    @Schema(description = "值班日期", example = "2025-06-01", required = true)
    @NotNull(message = "值班日期不能为空")
    private LocalDate dutyDate;

    @Schema(description = "天气", example = "晴")
    @Size(max = 50, message = "天气长度不能超过50个字符")
    private String weather;

    @Schema(description = "雨量（毫米）", example = "0.00")
    private BigDecimal rainfall;

    @Schema(description = "带班领导", example = "李四")
    @Size(max = 50, message = "带班领导长度不能超过50个字符")
    private String leader;

    @Schema(description = "白班值班人员", example = "张三")
    @Size(max = 50, message = "白班值班人员长度不能超过50个字符")
    private String dayShiftPerson;

    @Schema(description = "晚班值班人员", example = "王五")
    @Size(max = 50, message = "晚班值班人员长度不能超过50个字符")
    private String nightShiftPerson;

    @Schema(description = "日志内容", example = "一切正常，无异常情况。")
    private String logContent;

    @Schema(description = "日志状态", example = "已填写")
    private String logStatus;
}
