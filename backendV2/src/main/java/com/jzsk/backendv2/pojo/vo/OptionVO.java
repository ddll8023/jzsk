package com.jzsk.backendv2.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "选项对象", description = "通用下拉选项返回结构")
public class OptionVO {

    @Schema(description = "显示标签", example = "监测站A")
    private String label;

    @Schema(description = "选项值")
    private Object value;
}
