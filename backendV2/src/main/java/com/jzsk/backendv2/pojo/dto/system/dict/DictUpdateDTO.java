package com.jzsk.backendv2.pojo.dto.system.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "字典更新请求", description = "更新字典请求参数")
public class DictUpdateDTO extends DictCreateDTO {

    @NotNull(message = "字典ID不能为空")
    @Schema(description = "字典ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
}
