package com.jzsk.backendv2.pojo.dto.engineering;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡检记录分页查询请求
 * 用途：分页查询巡检记录列表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "巡检记录分页查询请求", description = "分页查询巡检记录列表")
public class InspectionRecordsPageQueryDTO extends BasePageQueryDTO {

    @Schema(description = "巡检站点（模糊搜索）", example = "测站", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String project;

    @Schema(description = "异常情况", example = "有异常", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String abnormal;

    @Schema(description = "负责人（模糊搜索）", example = "张三", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String person;

    @Schema(description = "处理状态", example = "已处理", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String solve;

    @Schema(description = "开始时间", example = "2025-01-01 00:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String startTime;

    @Schema(description = "结束时间", example = "2025-12-31 23:59:59", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String endTime;
}
