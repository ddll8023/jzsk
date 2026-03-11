package com.szy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.szy.common.exception.BusinessException;
import com.szy.mapper.RoleAuthorityMapper;
import com.szy.mapper.RoleMapper;
import com.szy.pojo.dto.RoleDTO;
import com.szy.pojo.dto.RoleQueryDTO;
import com.szy.pojo.entity.Role;
import com.szy.pojo.entity.RoleAuthority;
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
    private final RoleAuthorityMapper roleAuthorityMapper;

    @Override
    public PageInfo<Role> list(RoleQueryDTO queryDTO) {
        // 设置分页默认值
        int currentPage = queryDTO.getCurrentPage() == null ? 1 : queryDTO.getCurrentPage();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(currentPage, pageSize);
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
        vo.setMenuIds(roleAuthorityMapper.selectAuthorityIdsByRoleId(id));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RoleDTO dto) {
        Role role = BeanUtil.copyProperties(dto, Role.class);
        // 处理前端传递的description字段，映射到note
        if (StrUtil.isBlank(role.getNote()) && StrUtil.isNotBlank(dto.getDescription())) {
            role.setNote(dto.getDescription());
        }
        // 处理status字段，确保是字符串类型
        role.setStatus(StrUtil.isBlank(dto.getStatus()) ? "1" : dto.getStatus());
        role.setType("1");
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
        // 处理前端传递的description字段，映射到note
        if (StrUtil.isBlank(role.getNote()) && StrUtil.isNotBlank(dto.getDescription())) {
            role.setNote(dto.getDescription());
        }
        role.setStatus(StrUtil.isBlank(dto.getStatus()) ? "1" : dto.getStatus());
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
        roleAuthorityMapper.deleteByRoleId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateMenu(Long roleId, List<Long> menuIds) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        // 先删除旧的关联
        roleAuthorityMapper.deleteByRoleId(roleId);
        // 再插入新的关联
        if (menuIds != null && !menuIds.isEmpty()) {
            List<RoleAuthority> list = new ArrayList<>();
            for (Long menuId : menuIds) {
                RoleAuthority rm = new RoleAuthority();
                rm.setRoleId(roleId);
                rm.setAuthorityId(menuId);
                list.add(rm);
            }
            roleAuthorityMapper.batchInsert(list);
        }
    }

    @Override
    public List<Long> getRoleMenus(Long roleId) {
        return roleAuthorityMapper.selectAuthorityIdsByRoleId(roleId);
    }

    @Override
    public List<Role> listByUserId(Long userId) {
        return roleMapper.selectByUserId(userId);
    }
}