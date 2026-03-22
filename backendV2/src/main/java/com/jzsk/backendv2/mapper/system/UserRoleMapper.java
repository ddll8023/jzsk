package com.jzsk.backendv2.mapper.system;

import com.jzsk.backendv2.pojo.entity.system.UserRoleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联Mapper接口
 * 职责：用户角色关联表的数据库操作
 */
public interface UserRoleMapper {

    /**
     * 批量插入用户角色关联
     * @param list 关联列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<UserRoleEntity> list);

    /**
     * 根据用户ID删除关联
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID删除关联
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询第一个角色ID
     * @param userId 用户ID
     * @return 角色ID
     */
    Long selectFirstRoleIdByUserId(@Param("userId") Long userId);
}
