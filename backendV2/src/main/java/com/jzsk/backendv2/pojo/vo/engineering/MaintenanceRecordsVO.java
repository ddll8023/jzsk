package com.jzsk.backendv2.pojo.vo.engineering;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 养护记录视图对象
 * 用途：养护记录详情和列表查询的返回数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "养护记录视图对象", description = "养护记录详情和列表查询的返回数据")
public class MaintenanceRecordsVO {

    @Schema(description = "养护记录ID", example = "1")
    private Long id;

    @Schema(description = "工程名称", example = "荆竹水库水位监测子系统")
    private String name;

    @Schema(description = "工程代码", example = "123545689")
    private String code;

    @Schema(description = "备注", example = "对水位监测传感器进行维护")
    private String note;

    @Schema(description = "负责人", example = "李四")
    private String responsiblePerson;

    @Schema(description = "负责人电话", example = "13545687895")
    private String phone;

    @Schema(description = "开始维护时间", example = "2025-06-05 05:07:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @Schema(description = "结束维护时间", example = "2025-06-06 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime overTime;

    @Schema(description = "创建时间", example = "2024-06-20 21:17:52")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2025-06-23 15:12:11")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
