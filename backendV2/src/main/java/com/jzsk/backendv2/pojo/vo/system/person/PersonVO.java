package com.jzsk.backendv2.pojo.vo.system.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人员信息视图对象
 * 用途：人员信息响应数据封装
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "人员信息VO", description = "人员信息视图对象")
public class PersonVO {

    @Schema(description = "人员ID", example = "1")
    private Long id;

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "年龄", example = "28")
    private Integer age;

    @Schema(description = "性别", example = "男")
    private String gender;

    @Schema(description = "电话", example = "17635123456")
    private String phone;

    @Schema(description = "所属机构", example = "希望村组织部")
    private String organization;

    @Schema(description = "职位", example = "组长")
    private String position;

    @Schema(description = "职责", example = "进行巡检")
    private String duty;
}
