package com.jzsk.backendv2.pojo.vo.warning;

import com.jzsk.backendv2.pojo.vo.OptionVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 预警指标选项VO
 * 用途：承载预警指标页面所需的测点、监测项和绑定关系选项
 */
@Data
@Schema(name = "预警指标选项VO", description = "承载预警指标页面所需的测点、监测项和绑定关系选项")
public class WarningIndicatorOptionsVO {

    @Schema(description = "测点选项列表")
    private List<OptionVO> positionOptions = new ArrayList<>();

    @Schema(description = "监测项选项列表")
    private List<OptionVO> typeOptions = new ArrayList<>();

    @Schema(description = "测点与监测项绑定关系")
    private List<WarningIndicatorBindingVO> bindings = new ArrayList<>();
}
