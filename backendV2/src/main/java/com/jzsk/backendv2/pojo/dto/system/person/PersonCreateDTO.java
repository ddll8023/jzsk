package com.jzsk.backendv2.pojo.dto.system.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 人员信息创建请求
 * 用途：创建新人员信息的请求参数
 */
@Data
@Schema(name = "人员信息创建请求", description = "创建新人员信息的请求参数")
public class PersonCreateDTO {

    @Schema(description = "姓名", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "姓名不能为空")
    @Size(max = 255, message = "姓名长度不能超过255个字符")
    private String name;

    @Schema(description = "年龄", example = "28")
    @Min(value = 0, message = "年龄不能小于0岁")
    @Max(value = 150, message = "年龄不能超过150岁")
    private Integer age;

    @Schema(description = "性别", example = "男", allowableValues = {"男", "女", "未知"})
    @Size(max = 10, message = "性别长度不能超过10个字符")
    private String gender;

    @Schema(description = "电话", example = "17635123456")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Size(max = 20, message = "电话长度不能超过20个字符")
    private String phone;

    @Schema(description = "所属机构", example = "希望村组织部")
    @Size(max = 255, message = "所属机构长度不能超过255个字符")
    private String organization;

    @Schema(description = "职位", example = "组长")
    @Size(max = 255, message = "职位长度不能超过255个字符")
    private String position;

    @Schema(description = "职责", example = "进行巡检")
    @Size(max = 255, message = "职责长度不能超过255个字符")
    private String duty;
}
