package com.jzsk.backendv2.service.impl.system;

import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.mapper.system.UserMapper;
import com.jzsk.backendv2.mapper.system.UserRoleMapper;
import com.jzsk.backendv2.mapper.system.RoleMapper;
import com.jzsk.backendv2.pojo.dto.system.user.UserCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.user.UserPageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.user.UserPasswordUpdateDTO;
import com.jzsk.backendv2.pojo.dto.system.user.UserRoleAssignDTO;
import com.jzsk.backendv2.pojo.dto.system.user.UserUpdateDTO;
import com.jzsk.backendv2.pojo.entity.system.RoleEntity;
import com.jzsk.backendv2.pojo.entity.system.UserEntity;
import com.jzsk.backendv2.pojo.entity.system.UserRoleEntity;
import com.jzsk.backendv2.pojo.vo.OptionVO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.user.UserVO;
import com.jzsk.backendv2.service.system.UserService;
import com.jzsk.backendv2.utils.PageUtils;
import com.jzsk.backendv2.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * 职责：提供用户CRUD、角色分配、密码管理等业务逻辑
 * 遵循KISS原则：方法简洁，职责单一
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final long MAX_PAGE_SIZE = 10000L;
    private static final String DEFAULT_PASSWORD = "123456";
    private static final String ROLE_CODE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_CODE_DATA_MAINTAINER = "ROLE_DATA_MAINTAINER";

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResultVO<UserVO> page(UserPageQueryDTO queryDTO) {
        UserPageQueryDTO normalized = normalizePageQuery(queryDTO);
        long total = userMapper.countPage(normalized);
        if (total <= 0L) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        long offset = (normalized.getPage() - 1L) * normalized.getSize();
        List<UserEntity> entities = userMapper.selectPage(normalized, offset, normalized.getSize());
        if (entities.isEmpty()) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        List<UserVO> voList = entities.stream()
                .map(this::toUserVO)
                .collect(Collectors.toList());

        return PageUtils.buildPage(voList, total, normalized.getPage(), normalized.getSize());
    }

    @Override
    public UserVO getById(Long id) {
        UserEntity entity = userMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        UserVO vo = toUserVO(entity);
        // 填充角色信息
        List<OptionVO> roles = userMapper.selectUserRoles(id);
        if (roles != null && !roles.isEmpty()) {
            vo.setRoles(roles.stream()
                    .map(opt -> {
                        UserVO.RoleInfo roleInfo = new UserVO.RoleInfo();
                        roleInfo.setId(Long.valueOf(opt.getValue().toString()));
                        roleInfo.setName(opt.getLabel());
                        roleInfo.setCode(null);
                        return roleInfo;
                    })
                    .collect(Collectors.toList()));
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO create(UserCreateDTO request) {
        // 校验用户名唯一性
        ensureUsernameUnique(request.getUsername(), null);

        // 获取最大排序号
        Integer maxOrder = userMapper.selectMaxUserOrder();
        int newOrder = (maxOrder == null ? 0 : maxOrder) + 1;

        // 构建用户实体
        UserEntity entity = new UserEntity();
        entity.setUsername(request.getUsername());
        entity.setPassword(passwordEncoder.encode(
                StringUtils.hasText(request.getPassword()) ? request.getPassword() : DEFAULT_PASSWORD));
        entity.setName(request.getName());
        entity.setGender(request.getGender());
        entity.setDepartment(request.getDepartment());
        entity.setPosition(request.getPosition());
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setEmail(request.getEmail());
        entity.setIdNumber(request.getIdNumber());
        entity.setTechnicalTitle(request.getTechnicalTitle());
        entity.setAcademicQualifications(request.getAcademicQualifications());
        entity.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        entity.setNote(request.getNote());
        entity.setUserOrder(newOrder);

        userMapper.insert(entity);
        log.info("创建用户成功，userId={}, username={}", entity.getId(), entity.getUsername());

        // 分配角色
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            assignRolesToUser(entity.getId(), request.getRoleIds());
        }

        return getById(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO update(UserUpdateDTO request) {
        UserEntity existing = userMapper.selectById(request.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }

        // 校验用户名唯一性
        if (StringUtils.hasText(request.getUsername())
                && !request.getUsername().equals(existing.getUsername())) {
            ensureUsernameUnique(request.getUsername(), request.getId());
            existing.setUsername(request.getUsername());
        }

        // 更新字段
        if (request.getName() != null) existing.setName(request.getName());
        if (request.getGender() != null) existing.setGender(request.getGender());
        if (request.getDepartment() != null) existing.setDepartment(request.getDepartment());
        if (request.getPosition() != null) existing.setPosition(request.getPosition());
        if (request.getPhoneNumber() != null) existing.setPhoneNumber(request.getPhoneNumber());
        if (request.getEmail() != null) existing.setEmail(request.getEmail());
        if (request.getIdNumber() != null) existing.setIdNumber(request.getIdNumber());
        if (request.getTechnicalTitle() != null) existing.setTechnicalTitle(request.getTechnicalTitle());
        if (request.getAcademicQualifications() != null) existing.setAcademicQualifications(request.getAcademicQualifications());
        if (request.getStatus() != null) existing.setStatus(request.getStatus());
        if (request.getNote() != null) existing.setNote(request.getNote());

        userMapper.update(existing);
        log.info("更新用户成功，userId={}, username={}", existing.getId(), existing.getUsername());

        // 更新角色关联
        if (request.getRoleIds() != null) {
            assignRolesToUser(existing.getId(), request.getRoleIds());
        }

        return getById(existing.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        UserEntity existing = userMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }

        userRoleMapper.deleteByUserId(id);
        userMapper.deleteById(id);

        // 清除用户缓存
        clearUserAuthorityCache(existing.getUsername());
        log.info("删除用户成功，userId={}, username={}", id, existing.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(UserRoleAssignDTO request) {
        UserEntity user = userMapper.selectById(request.getUserId());
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }

        // 校验所有角色存在
        for (Long roleId : request.getRoleIds()) {
            RoleEntity role = roleMapper.selectById(roleId);
            if (role == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND, "角色不存在，roleId=" + roleId);
            }
        }

        // 删除旧关联，插入新关联
        userRoleMapper.deleteByUserId(request.getUserId());
        assignRolesToUser(request.getUserId(), request.getRoleIds());

        // 更新用户类型
        updateUserType(request.getUserId(), request.getRoleIds());

        // 清除用户缓存
        clearUserAuthorityCache(user.getUsername());
        log.info("分配角色成功，userId={}, roleIds={}", request.getUserId(), request.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id) {
        UserEntity user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }

        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        userMapper.update(user);
        log.info("重置密码成功，userId={}, username={}", id, user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(UserPasswordUpdateDTO request) {
        String currentUsername = SecurityUtils.getCurrentUsername();
        UserEntity user = userMapper.selectByUsername(currentUsername);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }

        // 校验旧密码
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "旧密码不正确");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userMapper.update(user);
        log.info("修改密码成功，username={}", currentUsername);
    }

    /**
     * 分配角色到用户（内部方法）
     */
    private void assignRolesToUser(Long userId, List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }
        List<UserRoleEntity> userRoles = roleIds.stream()
                .map(roleId -> {
                    UserRoleEntity ur = new UserRoleEntity();
                    ur.setUserId(userId);
                    ur.setRoleId(roleId);
                    return ur;
                })
                .collect(Collectors.toList());
        userRoleMapper.batchInsert(userRoles);
    }

    /**
     * 根据第一个角色编码更新用户类型
     */
    private void updateUserType(Long userId, List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            return;
        }

        Long firstRoleId = roleIds.get(0);
        RoleEntity role = roleMapper.selectById(firstRoleId);
        if (role == null) {
            return;
        }

        String newType;
        String roleCode = role.getCode();
        if (ROLE_CODE_ADMIN.equals(roleCode)) {
            newType = "超级管理员";
        } else if (ROLE_CODE_DATA_MAINTAINER.equals(roleCode)) {
            newType = "数据维护人员";
        } else {
            newType = "只读用户";
        }

        if (!newType.equals(user.getType())) {
            user.setType(newType);
            userMapper.update(user);
        }
    }

    /**
     * 清除用户权限缓存
     * 说明：当前版本采用无状态JWT认证，权限变更通过重新登录生效，
     * 无需主动清理Spring Security缓存。每次请求均从token重新加载用户信息。
     */
    private void clearUserAuthorityCache(String username) {
        log.debug("清除用户权限缓存，username={}", username);
    }

    /**
     * 校验用户名唯一性
     */
    private void ensureUsernameUnique(String username, Long excludeId) {
        if (userMapper.countByUsername(username, excludeId) > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "用户名已存在");
        }
    }

    /**
     * 标准化分页查询参数
     */
    private UserPageQueryDTO normalizePageQuery(UserPageQueryDTO queryDTO) {
        UserPageQueryDTO normalized = new UserPageQueryDTO();
        long page = queryDTO == null ? 1L : PageUtils.normalizePage(queryDTO.getPage());
        long size = (queryDTO == null || queryDTO.getSize() < 1L)
                ? 10L : Math.min(queryDTO.getSize(), MAX_PAGE_SIZE);

        normalized.setPage(page);
        normalized.setSize(size);
        if (queryDTO != null) {
            normalized.setUsername(queryDTO.getUsername());
            normalized.setName(queryDTO.getName());
            normalized.setDepartment(queryDTO.getDepartment());
            normalized.setType(queryDTO.getType());
            normalized.setStatus(queryDTO.getStatus());
        }
        return normalized;
    }

    /**
     * 转换为UserVO
     */
    private UserVO toUserVO(UserEntity entity) {
        UserVO vo = new UserVO();
        vo.setId(entity.getId());
        vo.setUsername(entity.getUsername());
        // 不返回密码
        vo.setName(entity.getName());
        vo.setGender(entity.getGender());
        vo.setDepartment(entity.getDepartment());
        vo.setPosition(entity.getPosition());
        vo.setPhoneNumber(entity.getPhoneNumber());
        vo.setEmail(entity.getEmail());
        vo.setIdNumber(entity.getIdNumber());
        vo.setTechnicalTitle(entity.getTechnicalTitle());
        vo.setAcademicQualifications(entity.getAcademicQualifications());
        vo.setType(entity.getType());
        vo.setStatus(entity.getStatus());
        vo.setNote(entity.getNote());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        vo.setRoles(Collections.emptyList());
        return vo;
    }
}
