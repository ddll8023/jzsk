package com.jzsk.backendv2.service.system;

import com.jzsk.backendv2.pojo.dto.system.user.UserCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.user.UserPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.user.UserPasswordUpdateDTO;
import com.jzsk.backendv2.pojo.dto.system.user.UserRoleAssignDTO;
import com.jzsk.backendv2.pojo.dto.system.user.UserUpdateDTO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.user.UserVO;

/**
 * 用户服务接口
 * 职责：提供用户管理的业务逻辑
 */
public interface UserService {

    /**
     * 分页查询用户列表
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResultVO<UserVO> page(UserPageQueryDTO queryDTO);

    /**
     * 根据ID查询用户详情
     * @param id 用户ID
     * @return 用户详情
     */
    UserVO getById(Long id);

    /**
     * 创建用户
     * @param request 创建请求
     * @return 创建的用户
     */
    UserVO create(UserCreateDTO request);

    /**
     * 更新用户
     * @param request 更新请求
     * @return 更新后的用户
     */
    UserVO update(UserUpdateDTO request);

    /**
     * 删除用户
     * @param id 用户ID
     */
    void delete(Long id);

    /**
     * 分配角色
     * @param request 分配请求
     */
    void assignRoles(UserRoleAssignDTO request);

    /**
     * 重置密码
     * @param id 用户ID
     */
    void resetPassword(Long id);

    /**
     * 修改当前用户密码
     * @param request 密码修改请求
     */
    void updatePassword(UserPasswordUpdateDTO request);
}
