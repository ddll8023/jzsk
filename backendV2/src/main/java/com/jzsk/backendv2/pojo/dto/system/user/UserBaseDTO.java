package com.jzsk.backendv2.pojo.dto.system.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户基础字段（创建和更新共用）
 * 用途：封装用户基本信息字段
 */
@Data
@Schema(name = "用户基础字段", description = "用户基本信息字段")
public class UserBaseDTO {

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "性别", example = "男")
    private String gender;

    @Schema(description = "所属部门", example = "技术部")
    private String department;

    @Schema(description = "岗位", example = "工程师")
    private String position;

    @Schema(description = "手机号码", example = "13800138000")
    private String phoneNumber;

    @Schema(description = "电子邮件", example = "zhangsan@example.com")
    private String email;

    @Schema(description = "身份证号码", example = "110101199001011234")
    private String idNumber;

    @Schema(description = "技术职称", example = "高级工程师")
    private String technicalTitle;

    @Schema(description = "学历", example = "本科")
    private String academicQualifications;

    @Schema(description = "状态：1启用 0禁用", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "备注信息")
    private String note;
}
