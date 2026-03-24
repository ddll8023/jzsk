package com.jzsk.backendv2.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.jzsk.backendv2.pojo.vo.ExcelExportData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel工具类
 * 职责：提供EasyExcel的导入导出封装
 * 遵循KISS原则：简单封装的工具类
 */
@Slf4j
public class ExcelUtils {

    private static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /**
     * 生成Excel字节数据
     * @param list 数据列表
     * @param clazz Excel映射实体类
     * @param sheetName sheet名称
     * @return ExcelExportData 包含文件名和字节数据
     */
    public static <T> ExcelExportData generateExcelData(List<T> list, Class<T> clazz, String sheetName) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            EasyExcel.write(baos, clazz)
                    .sheet(sheetName)
                    .doWrite(list);

            log.info("Excel生成成功，sheet名称：{}", sheetName);
            return new ExcelExportData(null, baos.toByteArray(), EXCEL_CONTENT_TYPE, sheetName);
        } catch (IOException e) {
            log.error("Excel生成失败：{}", e.getMessage(), e);
            throw new RuntimeException("Excel生成失败：" + e.getMessage());
        }
    }

    /**
     * 导出Excel（流式响应）
     * @param list 数据列表
     * @param clazz Excel映射实体类
     * @param fileName 文件名（不含扩展名）
     * @param sheetName sheet名称
     * @param response HTTP响应
     */
    public static <T> void exportExcel(List<T> list, Class<T> clazz,
                                       String fileName, String sheetName,
                                       HttpServletResponse response) {
        try {
            response.setContentType(EXCEL_CONTENT_TYPE);
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition",
                    "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");

            try (OutputStream outputStream = response.getOutputStream()) {
                EasyExcel.write(outputStream, clazz)
                        .sheet(sheetName)
                        .doWrite(list);
            }

            log.info("Excel导出成功，文件名：{}", fileName);
        } catch (IOException e) {
            log.error("Excel导出失败：{}", e.getMessage(), e);
            throw new RuntimeException("Excel导出失败：" + e.getMessage());
        }
    }

    /**
     * 将ExcelExportData写入HTTP响应
     * @param exportData 导出数据
     * @param fileName 文件名（不含扩展名）
     * @param response HTTP响应
     */
    public static void writeToResponse(ExcelExportData exportData, String fileName, HttpServletResponse response) {
        try {
            response.setContentType(exportData.getContentType());
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition",
                    "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
            response.setContentLengthLong(exportData.getData().length);

            try (OutputStream outputStream = response.getOutputStream()) {
                outputStream.write(exportData.getData());
                outputStream.flush();
            }

            log.info("Excel响应写入成功，文件名：{}", fileName);
        } catch (IOException e) {
            log.error("Excel响应写入失败：{}", e.getMessage(), e);
            throw new RuntimeException("Excel响应写入失败：" + e.getMessage());
        }
    }

    /**
     * 从Excel导入
     * @param file 上传的文件
     * @param clazz 映射实体类
     * @param headerRows 表头行数（默认1）
     * @return 数据列表
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> clazz, int headerRows) {
        try {
            List<T> result = new ArrayList<>();
            EasyExcel.read(file.getInputStream(), clazz, new ReadListener<T>() {
                @Override
                public void invoke(T data, AnalysisContext context) {
                    result.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    // nothing to do
                }
            }).headRowNumber(headerRows).sheet().doRead();
            return result;
        } catch (IOException e) {
            log.error("Excel导入失败：{}", e.getMessage(), e);
            throw new RuntimeException("Excel导入失败：" + e.getMessage());
        }
    }
}
