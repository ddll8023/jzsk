package com.jzsk.backendv2.pojo.vo.engineering;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 养护记录导出Excel模型
 * 用途：用于EasyExcel导出时的表头和数据映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "养护记录导出Excel模型", description = "用于EasyExcel导出时的表头和数据映射")
public class MaintenanceRecordsExcelVO {

    @ExcelProperty(value = "工程名称", index = 0)
    @Schema(description = "工程名称", example = "荆竹水库水位监测子系统")
    private String name;

    @ExcelProperty(value = "工程编码", index = 1)
    @Schema(description = "工程代码", example = "123545689")
    private String code;

    @ExcelProperty(value = "备注", index = 2)
    @Schema(description = "备注", example = "对水位监测传感器进行维护")
    private String note;

    @ExcelProperty(value = "负责人", index = 3)
    @Schema(description = "负责人", example = "李四")
    private String responsiblePerson;

    @ExcelProperty(value = "负责人电话", index = 4)
    @Schema(description = "负责人电话", example = "13545687895")
    private String phone;

    @ExcelProperty(value = "开始维护时间", index = 5)
    @Schema(description = "开始维护时间", example = "2025-06-05 05:07:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ExcelProperty(value = "结束维护时间", index = 6)
    @Schema(description = "结束维护时间", example = "2025-06-06 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime overTime;

    @ExcelIgnore
    private Long id;
}
