package com.szy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.szy.common.exception.BusinessException;
import com.szy.mapper.RoleMapper;
import com.szy.mapper.RoleMenuMapper;
import com.szy.pojo.dto.RoleDTO;
import com.szy.pojo.dto.RoleQueryDTO;
import com.szy.pojo.entity.Role;
import com.szy.pojo.entity.RoleMenu;
import com.szy.pojo.vo.RoleVO;
import com.szy.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色服务实现
 */
@Service
@RequiredArgsConstructor
@DS("jcxx")
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final RoleMenuMapper roleMenuMapper;

    @Override
    public PageInfo<Role> list(RoleQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getCurrentPage(), queryDTO.getPageSize());
        List<Role> roles = roleMapper.selectList(queryDTO.getName());
        return new PageInfo<>(roles);
    }

    @Override
    public RoleVO getInfo(Long id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        RoleVO vo = BeanUtil.copyProperties(role, RoleVO.class);
        vo.setMenuIds(roleMenuMapper.selectMenuIdsByRoleId(id));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RoleDTO dto) {
        Role role = BeanUtil.copyProperties(dto, Role.class);
        role.setStatus(StrUtil.isBlank(dto.getStatus()) ? "1" : dto.getStatus());
        roleMapper.insert(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleDTO dto) {
        Role existing = roleMapper.selectById(dto.getId());
        if (existing == null) {
            throw new BusinessException("角色不存在");
        }
        Role role = BeanUtil.copyProperties(dto, Role.class);
        roleMapper.update(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Role existing = roleMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("角色不存在");
        }
        roleMapper.deleteById(id);
        // 删除角色菜单关联
        roleMenuMapper.deleteByRoleId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateMenu(Long roleId, List<Long> menuIds) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        // 先删除旧的关联
        roleMenuMapper.deleteByRoleId(roleId);
        // 再插入新的关联
        if (menuIds != null && !menuIds.isEmpty()) {
            List<RoleMenu> list = new ArrayList<>();
            for (Long menuId : menuIds) {
                RoleMenu rm = new RoleMenu();
                rm.setRoleId(roleId);
                rm.setMenuId(menuId);
                list.add(rm);
            }
            roleMenuMapper.batchInsert(list);
        }
    }

    @Override
    public List<Long> getRoleMenus(Long roleId) {
        return roleMenuMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    public List<Role> listByUserId(Long userId) {
        return roleMapper.selectByUserId(userId);
    }
}