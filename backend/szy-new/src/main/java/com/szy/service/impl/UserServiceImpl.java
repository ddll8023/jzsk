package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.szy.common.exception.BusinessException;
import com.szy.mapper.UserMapper;
import com.szy.mapper.UserRoleMapper;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
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
    private final StringRedisTemplate stringRedisTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    /** 权限缓存Key前缀 */
    private static final String AUTHORITY_CACHE_PREFIX = "GrantedAuthority:";

    /** 权限缓存过期时间（小时） */
    private static final long AUTHORITY_CACHE_EXPIRE_HOURS = 1;

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
        // 先从缓存获取
        User user = userMapper.selectById(userId);
        if (user == null) {
            return "";
        }

        String cacheKey = AUTHORITY_CACHE_PREFIX + user.getUsername();
        String cachedAuthority = stringRedisTemplate.opsForValue().get(cacheKey);
        if (cachedAuthority != null) {
            return cachedAuthority;
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

        // 缓存结果
        stringRedisTemplate.opsForValue().set(
                cacheKey,
                authorityInfo,
                AUTHORITY_CACHE_EXPIRE_HOURS,
                TimeUnit.HOURS
        );

        return authorityInfo;
    }

    @Override
    public void clearUserAuthorityCache(String username) {
        String cacheKey = AUTHORITY_CACHE_PREFIX + username;
        stringRedisTemplate.delete(cacheKey);
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
        PageHelper.startPage(queryDTO.getCurrentPage(), queryDTO.getPageSize());
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
        user.setType(dto.getType());
        user.setDepartment(dto.getDepartment());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());

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
        user.setName(dto.getName());
        user.setType(dto.getType());
        user.setDepartment(dto.getDepartment());
        user.setPhone(dto.getPhone());
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