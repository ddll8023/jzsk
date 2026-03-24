package com.jzsk.backendv2.pojo.vo.engineering;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 巡检记录VO
 * 用途：巡检记录详情和列表返回
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "巡检记录VO", description = "巡检记录详情和列表返回")
public class InspectionRecordsVO {

    @Schema(description = "巡检记录ID", example = "1")
    private Long id;

    @Schema(description = "巡检站点", example = "测站1")
    private String project;

    @Schema(description = "经度", example = "114.6186324")
    private BigDecimal longitude;

    @Schema(description = "纬度", example = "30.4627869")
    private BigDecimal latitude;

    @Schema(description = "巡检类型", example = "日常巡检")
    private String type;

    @Schema(description = "异常情况", example = "有异常")
    private String abnormal;

    @Schema(description = "巡检情况", example = "设备正常")
    private String situation;

    @Schema(description = "处理状态", example = "已处理")
    private String solve;

    @Schema(description = "图片路径", example = "abc123.jpg")
    private String image;

    @Schema(description = "负责人", example = "张三")
    private String person;

    @Schema(description = "巡检日期", example = "2025-06-13 01:38:50")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime date;

    @Schema(description = "创建时间", example = "2025-06-13 01:38:50")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2025-06-17 21:55:40")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
