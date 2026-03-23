package com.jzsk.backendv2.pojo.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 部门实体类
 * 对应数据库表：department
 * 用途：部门数据模型映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 部门ID（主键，数据库自增） */
    private Long id;

    /** 部门名称 */
    private String departmentName;

    /** 部门职责 */
    private String departmentResponsibility;

    /** 部门级别 */
    private String level;

    /** 所属公司 */
    private String company;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime updateTime;

}
