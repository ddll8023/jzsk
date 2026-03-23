package com.jzsk.backendv2.pojo.vo.warning;

import com.jzsk.backendv2.pojo.vo.OptionVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 预警指标测点绑定关系
 * 用途：描述单个测点可选的监测项列表
 */
@Data
@Schema(name = "预警指标测点绑定关系", description = "描述单个测点可选的监测项列表")
public class WarningIndicatorBindingVO {

    @Schema(description = "测点名称", example = "两河口水库")
    private String position;

    @Schema(description = "监测项选项列表")
    private List<OptionVO> typeOptions = new ArrayList<>();
}
