package com.jzsk.backendv2.pojo.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜单权限实体类
 * 对应数据库表：authority
 * 用途：菜单权限的数据模型映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 父资源/子系统ID（顶级菜单为0） */
    private Long subsystemid;

    /** 菜单名称 */
    private String name;

    /** 菜单编码 */
    private String code;

    /** 菜单路径 */
    private String path;

    /** 资源类型 */
    private String type;

    /** 资源顺序 */
    private Integer ordernum;

    /** 状态：启用/禁用 */
    private String status;

    /** 资源备注 */
    private String note;
}
