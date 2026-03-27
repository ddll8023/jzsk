package com.jzsk.backendv2.pojo.vo.dam;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 渗流量视图对象
 * 用途: 返回给前端的渗流量监测数据
 * 数据来源: seepage_data 表（gcdd数据库）
 */
@Schema(name = "渗流量VO", description = "渗流量监测数据视图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeepageFlowVO {

    @Schema(description = "记录ID", example = "1")
    private Integer id;

    @Schema(description = "测站编码", example = "4211823043")
    private String stcd;

    @Schema(description = "监测时间", example = "2026-03-25T14:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime tm;

    @Schema(description = "渗流量", example = "10.5")
    private Float q1;

    @Schema(description = "备注", example = "无特殊情况")
    private String remarks;
}
