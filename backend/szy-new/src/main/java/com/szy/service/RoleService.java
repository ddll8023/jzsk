package com.szy.service;

import com.github.pagehelper.PageInfo;
import com.szy.pojo.dto.RoleDTO;
import com.szy.pojo.dto.RoleQueryDTO;
import com.szy.pojo.entity.Role;
import com.szy.pojo.vo.RoleVO;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService {

    /**
     * 分页查询角色
     */
    PageInfo<Role> list(RoleQueryDTO queryDTO);

    /**
     * 获取角色详情（含菜单ID）
     */
    RoleVO getInfo(Long id);

    /**
     * 新增角色
     */
    void save(RoleDTO dto);

    /**
     * 更新角色
     */
    void update(RoleDTO dto);

    /**
     * 删除角色
     */
    void delete(Long id);

    /**
     * 分配菜单权限
     */
    void allocateMenu(Long roleId, List<Long> menuIds);

    /**
     * 获取角色菜单ID列表
     */
    List<Long> getRoleMenus(Long roleId);

    /**
     * 根据用户ID查询角色列表
     */
    List<Role> listByUserId(Long userId);
}