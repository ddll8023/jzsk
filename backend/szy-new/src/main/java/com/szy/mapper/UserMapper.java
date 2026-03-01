package com.szy.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
@DS("jcxx")
public interface UserMapper {

    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询用户
     */
    User selectById(@Param("id") Long id);

    /**
     * 更新用户密码
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 根据用户ID查询权限编码
     */
    List<String> selectAuthorityCodesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询角色编码
     */
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    /**
     * 分页查询用户
     */
    List<User> selectList(@Param("username") String username, @Param("name") String name);

    /**
     * 新增用户
     */
    int insert(User user);

    /**
     * 更新用户
     */
    int update(User user);

    /**
     * 删除用户
     */
    int deleteById(@Param("id") Long id);
}