package com.szy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.szy.common.exception.BusinessException;
import com.szy.mapper.UserMapper;
import com.szy.mapper.UserRoleMapper;
import com.szy.pojo.dto.UpdatePasswordDTO;
import com.szy.pojo.dto.UserDTO;
import com.szy.pojo.dto.UserQueryDTO;
import com.szy.pojo.entity.Role;
import com.szy.pojo.entity.User;
import com.szy.pojo.vo.RoleVO;
import com.szy.pojo.vo.UserDetailVO;
import com.szy.pojo.vo.UserVO;
import com.szy.service.RoleService;
import com.szy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
@DS("jcxx")
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    /** 默认密码 */
    private static final String DEFAULT_PASSWORD = "123456";

    @Override
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return "";
        }

        // 查询角色
        List<String> roleCodes = userMapper.selectRoleCodesByUserId(userId);
        String roles = roleCodes.stream()
                .map(code -> "ROLE_" + code)
                .collect(Collectors.joining(","));

        // 查询权限
        List<String> authorityCodes = userMapper.selectAuthorityCodesByUserId(userId);
        String authorities = String.join(",", authorityCodes);

        // 拼接角色和权限
        String authorityInfo = roles;
        if (!authorities.isEmpty()) {
            if (!authorityInfo.isEmpty()) {
                authorityInfo += ",";
            }
            authorityInfo += authorities;
        }

        return authorityInfo;
    }

    @Override
    public void clearUserAuthorityCache(String username) {
        // 暂不实现（Redis已移除）
    }

    @Override
    public void updatePassword(Long userId, String newPassword) {
        userMapper.updatePassword(userId, newPassword);
    }

    @Override
    public UserVO toUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setName(user.getName());
        vo.setType(user.getType());
        vo.setDepartment(user.getDepartment());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        return vo;
    }

    @Override
    public PageInfo<UserDetailVO> list(UserQueryDTO queryDTO) {
        // 设置分页默认值
        int currentPage = queryDTO.getCurrentPage() == null ? 1 : queryDTO.getCurrentPage();
        int pageSize = queryDTO.getPageSize() == null ? 10 : queryDTO.getPageSize();
        PageHelper.startPage(currentPage, pageSize);
        List<User> users = userMapper.selectList(queryDTO.getUsername(), queryDTO.getName());
        PageInfo<User> pageInfo = new PageInfo<>(users);

        // 转换为UserDetailVO
        List<UserDetailVO> voList = users.stream()
                .map(this::toUserDetailVO)
                .collect(Collectors.toList());

        PageInfo<UserDetailVO> result = new PageInfo<>();
        result.setList(voList);
        result.setTotal(pageInfo.getTotal());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setPages(pageInfo.getPages());
        return result;
    }

    @Override
    public UserDetailVO getDetail(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return toUserDetailVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserDTO dto) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(dto.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.setName(dto.getName());
        // 处理空字符串，转换为null
        user.setType(StrUtil.isBlank(dto.getType()) ? null : dto.getType());
        user.setDepartment(StrUtil.isBlank(dto.getDepartment()) ? null : dto.getDepartment());
        user.setPhone(StrUtil.isBlank(dto.getPhoneNumber()) ? null : dto.getPhoneNumber());
        user.setEmail(StrUtil.isBlank(dto.getEmail()) ? null : dto.getEmail());

        userMapper.insert(user);

        // 处理角色关联
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            userRoleMapper.batchInsert(user.getId(), dto.getRoleIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserDTO dto) {
        // 检查用户是否存在
        User existUser = userMapper.selectById(dto.getId());
        if (existUser == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查用户名是否被其他用户占用
        User userByUsername = userMapper.selectByUsername(dto.getUsername());
        if (userByUsername != null && !userByUsername.getId().equals(dto.getId())) {
            throw new BusinessException("用户名已存在");
        }

        // 更新用户
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setType(dto.getType());
        user.setDepartment(dto.getDepartment());
        user.setPhone(dto.getPhoneNumber());
        user.setEmail(dto.getEmail());

        userMapper.update(user);

        // 更新角色关联
        userRoleMapper.deleteByUserId(dto.getId());
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            userRoleMapper.batchInsert(dto.getId(), dto.getRoleIds());
        }

        // 清除权限缓存
        clearUserAuthorityCache(existUser.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 删除用户角色关联
        userRoleMapper.deleteByUserId(id);

        // 删除用户
        userMapper.deleteById(id);

        // 清除权限缓存
        clearUserAuthorityCache(user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateRole(Long userId, List<Long> roleIds) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 删除旧关联
        userRoleMapper.deleteByUserId(userId);

        // 插入新关联
        if (roleIds != null && !roleIds.isEmpty()) {
            userRoleMapper.batchInsert(userId, roleIds);
        }

        // 清除权限缓存
        clearUserAuthorityCache(user.getUsername());
    }

    @Override
    public void resetPassword(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        String encodedPassword = passwordEncoder.encode(DEFAULT_PASSWORD);
        userMapper.updatePassword(userId, encodedPassword);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 更新新密码
        String encodedPassword = passwordEncoder.encode(newPassword);
        userMapper.updatePassword(userId, encodedPassword);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        changePassword(user.getId(), oldPassword, newPassword);
    }

    @Override
    public void changePassword(Long userId, UpdatePasswordDTO dto) {
        // 验证新密码和确认密码是否一致
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("新密码与确认密码不一致");
        }

        // 获取用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证原密码
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException("原密码不正确");
        }

        // 更新密码
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        userMapper.updatePassword(userId, encodedPassword);

        // 清除权限缓存
        clearUserAuthorityCache(user.getUsername());
    }

    /**
     * 转换为UserDetailVO
     */
    private UserDetailVO toUserDetailVO(User user) {
        if (user == null) {
            return null;
        }
        UserDetailVO vo = new UserDetailVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setName(user.getName());
        vo.setType(user.getType());
        vo.setDepartment(user.getDepartment());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());

        // 查询角色列表
        List<Role> roles = roleService.listByUserId(user.getId());
        List<RoleVO> roleVOList = roles.stream()
                .map(this::toRoleVO)
                .collect(Collectors.toList());
        vo.setRoles(roleVOList);

        return vo;
    }

    /**
     * 转换为RoleVO
     */
    private RoleVO toRoleVO(Role role) {
        if (role == null) {
            return null;
        }
        RoleVO vo = new RoleVO();
        vo.setId(role.getId());
        vo.setName(role.getName());
        vo.setCode(role.getCode());
        vo.setNote(role.getNote());
        vo.setStatus(role.getStatus());
        return vo;
    }
}