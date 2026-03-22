package com.jzsk.backendv2.pojo.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "当前登录用户", description = "当前登录用户完整信息")
public class CurrentUserVO {

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "显示名称", example = "管理员")
    private String displayName;

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "所属部门", example = "技术部")
    private String department;

    @Schema(description = "岗位", example = "工程师")
    private String position;

    @Schema(description = "用户类型", example = "只读用户")
    private String type;

    @Schema(description = "性别", example = "男")
    private String gender;

    @Schema(description = "身份证号", example = "110101199001011234")
    private String idNumber;

    @Schema(description = "技术职称", example = "高级工程师")
    private String technicalTitle;

    @Schema(description = "学历", example = "本科")
    private String academicQualifications;

    @Schema(description = "开始工作时间")
    private Date workingTime;

    @Schema(description = "毕业院校", example = "某某大学")
    private String graduationInstitution;

    @Schema(description = "专业", example = "水利工程")
    private String major;

    @Schema(description = "家庭住址", example = "湖北省武汉市")
    private String address;

    @Schema(description = "出生地", example = "湖北省武汉市")
    private String birthplace;

    @Schema(description = "民族", example = "汉族")
    private String ethnicity;

    @Schema(description = "电子邮箱", example = "admin@example.com")
    private String email;

    @Schema(description = "出生年月", example = "1990-01")
    private String birthday;

    @Schema(description = "政治面貌", example = "中共党员")
    private String politicalAppearance;

    @Schema(description = "手机号", example = "13800138000")
    private String phoneNumber;

    @Schema(description = "备注", example = "备注信息")
    private String note;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "角色列表")
    private List<RoleInfo> roles;

    @Schema(description = "权限编码列表")
    private List<String> authorities;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "当前用户角色", description = "当前用户拥有的角色信息")
    public static class RoleInfo {

        @Schema(description = "角色ID", example = "1")
        private Long id;

        @Schema(description = "角色名称", example = "系统管理员")
        private String name;
    }
}
