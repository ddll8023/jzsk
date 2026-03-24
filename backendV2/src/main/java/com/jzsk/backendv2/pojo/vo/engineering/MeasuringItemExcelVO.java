package com.jzsk.backendv2.pojo.vo.engineering;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测项导出Excel模型
 * 用途：用于EasyExcel导出时的表头和数据映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "测项导出Excel模型", description = "用于EasyExcel导出时的表头和数据映射")
public class MeasuringItemExcelVO {

    @ExcelProperty(value = "测项编号", index = 0)
    @Schema(description = "测项编号", example = "1")
    private String number;

    @ExcelProperty(value = "测项名称", index = 1)
    @Schema(description = "测项名称", example = "水位")
    private String name;

    @ExcelProperty(value = "测项单位", index = 2)
    @Schema(description = "测项单位", example = "m")
    private String unit;

    @ExcelIgnore
    private Long id;
}
