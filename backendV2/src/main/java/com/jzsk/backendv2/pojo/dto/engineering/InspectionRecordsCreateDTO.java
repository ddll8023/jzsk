package com.jzsk.backendv2.pojo.dto.engineering;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 巡检记录创建请求
 * 用途：创建新巡检记录
 */
@Data
@Schema(name = "巡检记录创建请求", description = "创建新巡检记录")
public class InspectionRecordsCreateDTO {

    @Schema(description = "巡检站点", example = "测站1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "巡检站点不能为空")
    @Size(max = 255, message = "巡检站点长度不能超过255个字符")
    private String project;

    @Schema(description = "经度", example = "114.6186324", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    @Schema(description = "纬度", example = "30.4627869", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    @Schema(description = "巡检类型", example = "日常巡检", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "巡检类型不能为空")
    @Size(max = 255, message = "巡检类型长度不能超过255个字符")
    private String type;

    @Schema(description = "异常情况", example = "有异常", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "异常情况不能为空")
    @Size(max = 10, message = "异常情况长度不能超过10个字符")
    private String abnormal;

    @Schema(description = "巡检情况", example = "设备正常", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "巡检情况长度不能超过255个字符")
    private String situation;

    @Schema(description = "图片路径", example = "abc123.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String image;

    @Schema(description = "负责人", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "负责人不能为空")
    @Size(max = 255, message = "负责人长度不能超过255个字符")
    private String person;

    @Schema(description = "巡检日期", example = "2025-06-13 01:38:50", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "日期不能为空")
    private LocalDateTime date;
}
