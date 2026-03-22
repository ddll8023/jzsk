package com.jzsk.backendv2.pojo.vo.system.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "用户VO", description = "用户列表和详情视图")
public class UserVO {

    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "性别", example = "男")
    private String gender;

    @Schema(description = "所属部门", example = "技术部")
    private String department;

    @Schema(description = "岗位", example = "工程师")
    private String position;

    @Schema(description = "手机号", example = "13800138000")
    private String phoneNumber;

    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    @Schema(description = "身份证号", example = "110101199001011234")
    private String idNumber;

    @Schema(description = "技术职称", example = "高级工程师")
    private String technicalTitle;

    @Schema(description = "学历", example = "本科")
    private String academicQualifications;

    @Schema(description = "用户类型", example = "只读用户")
    private String type;

    @Schema(description = "备注", example = "备注信息")
    private String note;

    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    private Date createTime;

    @Schema(description = "修改时间", example = "2024-01-01 10:00:00")
    private Date updateTime;

    @Schema(description = "分配的角色列表")
    private List<RoleInfo> roles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "角色信息", description = "用户分配的角色信息")
    public static class RoleInfo {

        @Schema(description = "角色ID", example = "1")
        private Long id;

        @Schema(description = "角色名称", example = "系统管理员")
        private String name;

        @Schema(description = "角色编码", example = "ADMIN")
        private String code;
    }
}
