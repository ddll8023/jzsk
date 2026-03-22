package com.jzsk.backendv2.service.system;

import com.jzsk.backendv2.pojo.dto.system.role.RoleCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.role.RoleMenuAssignDTO;
import com.jzsk.backendv2.pojo.dto.system.role.RolePageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.role.RoleUpdateDTO;
import com.jzsk.backendv2.pojo.vo.OptionVO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.role.RoleMenuVO;
import com.jzsk.backendv2.pojo.vo.system.role.RoleVO;

import java.util.List;

/**
 * 角色服务接口
 * 职责：提供角色管理的业务逻辑
 */
public interface RoleService {

    /**
     * 分页查询角色列表
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResultVO<RoleVO> page(RolePageQueryDTO queryDTO);

    /**
     * 根据ID查询角色详情
     * @param id 角色ID
     * @return 角色详情
     */
    RoleVO getById(Long id);

    /**
     * 创建角色
     * @param request 创建请求
     * @return 创建的角色
     */
    RoleVO create(RoleCreateDTO request);

    /**
     * 更新角色
     * @param request 更新请求
     * @return 更新后的角色
     */
    RoleVO update(RoleUpdateDTO request);

    /**
     * 删除角色
     * @param id 角色ID
     */
    void delete(Long id);

    /**
     * 分配菜单权限
     * @param request 分配请求
     */
    void assignMenus(RoleMenuAssignDTO request);

    /**
     * 获取角色菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    RoleMenuVO getRoleMenus(Long roleId);

    /**
     * 获取所有启用的角色选项
     * @return 角色选项列表
     */
    List<OptionVO> listEnabledOptions();
}
