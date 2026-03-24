package com.jzsk.backendv2.pojo.dto.engineering;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 养护记录更新请求
 * 用途：更新已有的养护记录
 */
@Data
@Schema(name = "养护记录更新请求", description = "更新已有的养护记录")
public class MaintenanceRecordsUpdateDTO {

    @Schema(description = "养护记录ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "养护记录ID不能为空")
    private Long id;

    @Schema(description = "工程名称", example = "荆竹水库水位监测子系统", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "工程名称长度不能超过255个字符")
    private String name;

    @Schema(description = "工程代码", example = "123545689", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "工程代码长度不能超过255个字符")
    private String code;

    @Schema(description = "备注", example = "对水位监测传感器进行维护", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "备注长度不能超过255个字符")
    private String note;

    @Schema(description = "负责人", example = "李四", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "负责人长度不能超过255个字符")
    private String responsiblePerson;

    @Schema(description = "负责人电话", example = "13545687895", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 20, message = "负责人电话长度不能超过20个字符")
    private String phone;

    @Schema(description = "开始维护时间", example = "2025-06-05 05:07:59", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "结束维护时间", example = "2025-06-06 00:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime overTime;
}
