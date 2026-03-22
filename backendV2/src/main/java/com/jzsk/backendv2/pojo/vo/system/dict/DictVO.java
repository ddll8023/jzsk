package com.jzsk.backendv2.pojo.vo.system.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "字典响应", description = "字典信息")
public class DictVO {

    @Schema(description = "字典ID", example = "1")
    private Long id;

    @Schema(description = "字典名称", example = "预警等级")
    private String name;

    @Schema(description = "字典描述", example = "用于维护预警等级选项")
    private String description;

    @Schema(description = "详情数量", example = "3")
    private Integer detailCount;
}
