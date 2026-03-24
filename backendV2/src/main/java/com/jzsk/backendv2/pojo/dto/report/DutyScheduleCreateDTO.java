package com.jzsk.backendv2.pojo.dto.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 值班安排创建请求
 */
@Data
@Schema(name = "值班安排创建请求", description = "创建值班安排的请求参数")
public class DutyScheduleCreateDTO {

    @Schema(description = "值班人员", example = "张三", required = true)
    @NotBlank(message = "值班人员不能为空")
    @Size(max = 50, message = "值班人员长度不能超过50个字符")
    private String dutyPerson;

    @Schema(description = "带班领导", example = "李四")
    @Size(max = 50, message = "带班领导长度不能超过50个字符")
    private String leader;

    @Schema(description = "值班时间", example = "2025-06-01 08:00:00", required = true)
    @NotNull(message = "值班时间不能为空")
    private LocalDateTime dutyTime;

    @Schema(description = "值班岗位", example = "监控室", required = true)
    @NotBlank(message = "值班岗位不能为空")
    @Size(max = 50, message = "值班岗位长度不能超过50个字符")
    private String dutyPost;
}
