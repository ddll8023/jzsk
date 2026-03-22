package com.jzsk.backendv2.pojo.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色实体类
 * 对应数据库表：sys_role
 * 用途：角色数据模型映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 角色ID（主键，数据库自增） */
    private Long id;

    /** 角色名称 */
    private String name;

    /** 角色编码 */
    private String code;

    /** 角色备注 */
    private String note;

    /** 角色类型 */
    private String type;

    /** 状态：1启用 0禁用 */
    private String status;

    /** 排序号 */
    private Integer sort;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;
}
