package com.jzsk.backendv2.pojo.vo.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 值班安排响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "值班安排响应", description = "值班安排信息")
public class DutyScheduleVO {

    @Schema(description = "值班安排ID", example = "1")
    private Integer dutyScheduleId;

    @Schema(description = "值班人员", example = "张三")
    private String dutyPerson;

    @Schema(description = "带班领导", example = "李四")
    private String leader;

    @Schema(description = "值班时间", example = "2025-06-01 08:00:00")
    private LocalDateTime dutyTime;

    @Schema(description = "值班岗位", example = "监控室")
    private String dutyPost;

    @Schema(description = "创建时间", example = "2025-05-25 15:55:20")
    private LocalDateTime createTime;
}
