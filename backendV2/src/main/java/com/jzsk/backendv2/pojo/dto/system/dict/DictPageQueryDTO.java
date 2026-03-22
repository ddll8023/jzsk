package com.jzsk.backendv2.pojo.dto.system.dict;

import com.jzsk.backendv2.pojo.dto.BasePageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "字典分页查询参数", description = "字典管理分页查询参数")
public class DictPageQueryDTO extends BasePageQueryDTO {

    @Schema(description = "模糊搜索关键字", example = "预警")
    private String blurry;
}
