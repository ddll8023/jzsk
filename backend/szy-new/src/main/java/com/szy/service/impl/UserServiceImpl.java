package com.szy.service.impl;

import com.szy.mapper.UserMapper;
import com.szy.pojo.entity.User;
import com.szy.pojo.vo.UserVO;
import com.szy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final StringRedisTemplate stringRedisTemplate;

    /** 权限缓存Key前缀 */
    private static final String AUTHORITY_CACHE_PREFIX = "GrantedAuthority:";

    /** 权限缓存过期时间（小时） */
    private static final long AUTHORITY_CACHE_EXPIRE_HOURS = 1;

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
}