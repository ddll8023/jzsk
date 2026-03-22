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
@Schema(name = "字典详情响应", description = "字典详情信息")
public class DictDetailVO {

    @Schema(description = "字典详情ID", example = "1")
    private Long id;

    @Schema(description = "所属字典ID", example = "1")
    private Long dictId;

    @Schema(description = "字典标签", example = "一级预警")
    private String label;

    @Schema(description = "字典值", example = "1")
    private String value;

    @Schema(description = "排序号", example = "1")
    private Integer dictSort;
}
