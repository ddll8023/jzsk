package com.jzsk.backendv2.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Excel导出数据封装
 * 用途：封装Excel导出的文件名和字节数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Excel导出数据", description = "封装Excel导出的文件名和字节数据")
public class ExcelExportData {

    @Schema(description = "文件名（不含扩展名）", example = "测项信息")
    private String fileName;

    @Schema(description = "Excel文件字节数据")
    private byte[] data;

    @Schema(description = "Content-Type", example = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    private String contentType;

    @Schema(description = "Sheet名称", example = "测项列表")
    private String sheetName;
}
