package com.szy.service;

import com.github.pagehelper.PageInfo;
import com.szy.pojo.dto.UserDTO;
import com.szy.pojo.dto.UserQueryDTO;
import com.szy.pojo.entity.User;
import com.szy.pojo.vo.UserDetailVO;
import com.szy.pojo.vo.UserVO;

import java.util.List;

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

    /**
     * 分页查询用户
     */
    PageInfo<UserDetailVO> list(UserQueryDTO queryDTO);

    /**
     * 获取用户详情
     */
    UserDetailVO getDetail(Long id);

    /**
     * 新增用户
     */
    void save(UserDTO dto);

    /**
     * 更新用户
     */
    void update(UserDTO dto);

    /**
     * 删除用户
     */
    void delete(Long id);

    /**
     * 分配角色
     */
    void allocateRole(Long userId, List<Long> roleIds);

    /**
     * 重置密码
     */
    void resetPassword(Long userId);

    /**
     * 修改密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);
}