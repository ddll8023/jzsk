package com.jzsk.backendv2.pojo.vo.engineering;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 巡检记录导出Excel模型
 * 用途：用于EasyExcel导出时的表头和数据映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "巡检记录导出Excel模型", description = "用于EasyExcel导出时的表头和数据映射")
public class InspectionRecordsExcelVO {

    @ExcelProperty(value = "巡检站点", index = 0)
    @Schema(description = "巡检站点", example = "测站1")
    private String project;

    @ExcelProperty(value = "经度", index = 1)
    @Schema(description = "经度", example = "114.6186324")
    private BigDecimal longitude;

    @ExcelProperty(value = "纬度", index = 2)
    @Schema(description = "纬度", example = "30.4627869")
    private BigDecimal latitude;

    @ExcelProperty(value = "巡检类型", index = 3)
    @Schema(description = "巡检类型", example = "日常巡检")
    private String type;

    @ExcelProperty(value = "异常情况", index = 4)
    @Schema(description = "异常情况", example = "有异常")
    private String abnormal;

    @ExcelProperty(value = "巡检情况", index = 5)
    @Schema(description = "巡检情况", example = "设备正常")
    private String situation;

    @ExcelProperty(value = "处理状态", index = 6)
    @Schema(description = "处理状态", example = "已处理")
    private String solve;

    @ExcelProperty(value = "负责人", index = 7)
    @Schema(description = "负责人", example = "张三")
    private String person;

    @ExcelProperty(value = "巡检日期", index = 8)
    @Schema(description = "巡检日期", example = "2025-06-13 01:38:50")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime date;

    @ExcelIgnore
    private Long id;
}
