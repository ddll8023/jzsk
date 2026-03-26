package com.jzsk.backendv2.pojo.vo.dam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 渗压数据视图对象
 * 用途: 返回给前端的渗压监测数据（基于data_new表）
 * 数据来源: PostgreSQL data_new 表
 */
@Schema(name = "渗压数据VO", description = "渗压监测数据视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeepageVO {

    @Schema(description = "测点ID", example = "10108248")
    private String pointId;

    @Schema(description = "采集时间", example = "2026-03-25 14:00:00")
    private LocalDateTime time;

    @Schema(description = "原始数据（JSON，含模数、温度等字段）", example = "{\"模数\":\"125.5\",\"温度\":\"25.6\"}")
    private String originalData;

    @Schema(description = "结果数据（JSON，含水位高程、水位、水压等字段）", example = "{\"水位高程\":\"125.5\",\"水位\":\"100.0\",\"水压\":\"0.5\"}")
    private String resultData;

    @Schema(description = "测点名称", example = "UPb1-1")
    private String pointName;
}
