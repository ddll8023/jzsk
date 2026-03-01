package com.szy.service;

import com.szy.pojo.entity.User;
import com.szy.pojo.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 根据用户名查询用户
     */
    User getByUsername(String username);

    /**
     * 根据用户ID查询用户
     */
    User getById(Long id);

    /**
     * 获取用户权限信息（角色+权限）
     */
    String getUserAuthorityInfo(Long userId);

    /**
     * 清除用户权限缓存
     */
    void clearUserAuthorityCache(String username);

    /**
     * 修改用户密码
     */
    void updatePassword(Long userId, String newPassword);

    /**
     * 转换为UserVO
     */
    UserVO toUserVO(User user);
}