package com.szy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联Mapper
 */
@Mapper
public interface UserRoleMapper {

    /**
     * 根据用户ID查询角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID删除关联
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 批量插入
     */
    int batchInsert(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}