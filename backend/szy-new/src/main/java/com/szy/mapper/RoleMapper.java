package com.szy.mapper;

import com.szy.pojo.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper
 */
@Mapper
public interface RoleMapper {

    /**
     * 分页查询角色
     */
    List<Role> selectList(@Param("name") String name);

    /**
     * 根据ID查询
     */
    Role selectById(@Param("id") Long id);

    /**
     * 新增角色
     */
    int insert(Role role);

    /**
     * 更新角色
     */
    int update(Role role);

    /**
     * 删除角色
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据用户ID查询角色列表
     */
    List<Role> selectByUserId(@Param("userId") Long userId);
}