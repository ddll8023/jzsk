package com.szy.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 人员DTO
 */
@Schema(name = "人员DTO", description = "人员创建/更新请求参数")
@Data
public class PersonDTO {

    @Schema(description = "人员ID", example = "1", required = false)
    private Long id;

    @Schema(description = "姓名", example = "张三", required = true)
    @NotBlank(message = "姓名不能为空")
    @Size(max = 100, message = "姓名长度不能超过100个字符")
    private String name;

    @Schema(description = "年龄", example = "28", required = false)
    @Min(value = 1, message = "年龄最小为1")
    @Max(value = 120, message = "年龄最大为120")
    private Integer age;

    @Schema(description = "性别", example = "男", required = false)
    @Size(max = 10, message = "性别长度不能超过10个字符")
    private String gender;

    @Schema(description = "电话", example = "17635123456", required = false)
    @Size(max = 20, message = "电话长度不能超过20个字符")
    private String phone;

    @Schema(description = "所属机构", example = "希望村组织部", required = false)
    @Size(max = 255, message = "所属机构长度不能超过255个字符")
    private String organization;

    @Schema(description = "职位", example = "组长", required = false)
    @Size(max = 100, message = "职位长度不能超过100个字符")
    private String position;

    @Schema(description = "职责", example = "进行巡检", required = false)
    @Size(max = 500, message = "职责长度不能超过500个字符")
    private String duty;
}
