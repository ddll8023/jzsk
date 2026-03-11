package com.szy.mapper;

import com.szy.pojo.entity.RoleAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限关联Mapper（复用jcxx.role_authority表）
 */
@Mapper
public interface RoleAuthorityMapper {

    /**
     * 根据角色ID查询权限ID列表
     */
    List<Long> selectAuthorityIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色ID删除关联
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量插入
     */
    int batchInsert(@Param("list") List<RoleAuthority> list);
}
