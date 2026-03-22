package com.jzsk.backendv2.mapper.system;

import com.jzsk.backendv2.pojo.entity.system.RoleMenuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色菜单关联Mapper接口
 * 职责：角色菜单关联表的数据库操作
 */
public interface RoleMenuMapper {

    /**
     * 批量插入角色菜单关联
     * @param list 关联列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<RoleMenuEntity> list);

    /**
     * 根据角色ID删除关联
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色ID查询菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
