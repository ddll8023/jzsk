package com.jzsk.backendv2.service.impl.system;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.mapper.system.AuthorityMapper;
import com.jzsk.backendv2.mapper.system.RoleMapper;
import com.jzsk.backendv2.mapper.system.RoleMenuMapper;
import com.jzsk.backendv2.mapper.system.UserMapper;
import com.jzsk.backendv2.mapper.system.UserRoleMapper;
import com.jzsk.backendv2.pojo.dto.system.role.RoleCreateDTO;
import com.jzsk.backendv2.pojo.dto.system.role.RoleMenuAssignDTO;
import com.jzsk.backendv2.pojo.dto.system.role.RolePageQueryDTO;
import com.jzsk.backendv2.pojo.dto.system.role.RoleUpdateDTO;
import com.jzsk.backendv2.pojo.entity.system.AuthorityEntity;
import com.jzsk.backendv2.pojo.entity.system.RoleEntity;
import com.jzsk.backendv2.pojo.entity.system.RoleMenuEntity;
import com.jzsk.backendv2.pojo.entity.system.UserEntity;
import com.jzsk.backendv2.pojo.vo.OptionVO;
import com.jzsk.backendv2.pojo.vo.PageResultVO;
import com.jzsk.backendv2.pojo.vo.system.role.MenuTreeVO;
import com.jzsk.backendv2.pojo.vo.system.role.RoleMenuVO;
import com.jzsk.backendv2.pojo.vo.system.role.RoleVO;
import com.jzsk.backendv2.service.system.RoleService;
import com.jzsk.backendv2.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 * 职责：提供角色CRUD、菜单权限分配等业务逻辑
 * 遵循KISS原则：方法简洁，职责单一
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DS("jcxx")
public class RoleServiceImpl implements RoleService {

    private static final long MAX_PAGE_SIZE = 10000L;

    private final RoleMapper roleMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final AuthorityMapper authorityMapper;

    @Override
    public PageResultVO<RoleVO> page(RolePageQueryDTO queryDTO) {
        RolePageQueryDTO normalized = normalizePageQuery(queryDTO);
        long total = roleMapper.countPage(normalized);
        if (total <= 0L) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        long offset = (normalized.getPage() - 1L) * normalized.getSize();
        List<RoleEntity> entities = roleMapper.selectPage(normalized, offset, normalized.getSize());
        if (entities.isEmpty()) {
            return PageResultVO.empty(normalized.getPage(), normalized.getSize());
        }

        List<RoleVO> voList = entities.stream()
                .map(this::toRoleVO)
                .collect(Collectors.toList());

        return PageUtils.buildPage(voList, total, normalized.getPage(), normalized.getSize());
    }

    @Override
    public RoleVO getById(Long id) {
        RoleEntity entity = roleMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "角色不存在");
        }
        RoleVO vo = toRoleVO(entity);

        // 填充菜单ID列表
        List<Long> menuIds = roleMenuMapper.selectMenuIdsByRoleId(id);
        vo.setMenuIds(menuIds != null ? menuIds : new ArrayList<>());

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleVO create(RoleCreateDTO request) {
        // 校验角色名称和编码唯一性
        ensureRoleNameUnique(request.getName(), null);
        ensureRoleCodeUnique(request.getCode(), null);

        RoleEntity entity = new RoleEntity();
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setNote(request.getNote());
        entity.setType(request.getType());
        entity.setStatus(request.getStatus() != null ? request.getStatus() : "启用");
        entity.setSort(request.getSort());

        roleMapper.insert(entity);
        log.info("创建角色成功，roleId={}, name={}", entity.getId(), entity.getName());

        return getById(entity.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleVO update(RoleUpdateDTO request) {
        RoleEntity existing = roleMapper.selectById(request.getId());
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "角色不存在");
        }

        // 校验角色名称唯一性
        if (StringUtils.hasText(request.getName()) && !request.getName().equals(existing.getName())) {
            ensureRoleNameUnique(request.getName(), request.getId());
            existing.setName(request.getName());
        }

        // 校验角色编码唯一性
        if (StringUtils.hasText(request.getCode()) && !request.getCode().equals(existing.getCode())) {
            ensureRoleCodeUnique(request.getCode(), request.getId());
            existing.setCode(request.getCode());
        }

        // 更新字段
        if (request.getNote() != null) existing.setNote(request.getNote());
        if (request.getType() != null) existing.setType(request.getType());
        if (request.getStatus() != null) existing.setStatus(request.getStatus());
        if (request.getSort() != null) existing.setSort(request.getSort());

        roleMapper.update(existing);
        log.info("更新角色成功，roleId={}, name={}", existing.getId(), existing.getName());

        // 清除该角色下所有用户的权限缓存
        clearCacheByRoleId(existing.getId());

        return getById(existing.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        RoleEntity existing = roleMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "角色不存在");
        }

        // 删除角色-菜单关联
        roleMenuMapper.deleteByRoleId(id);

        // 删除用户-角色关联
        userRoleMapper.deleteByRoleId(id);

        // 删除角色
        roleMapper.deleteById(id);

        // 清除该角色下所有用户的权限缓存
        clearCacheByRoleId(id);

        log.info("删除角色成功，roleId={}, name={}", id, existing.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(RoleMenuAssignDTO request) {
        RoleEntity role = roleMapper.selectById(request.getRoleId());
        if (role == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "角色不存在");
        }

        // 删除旧关联，插入新关联
        roleMenuMapper.deleteByRoleId(request.getRoleId());
        if (request.getMenuIds() != null && !request.getMenuIds().isEmpty()) {
            List<RoleMenuEntity> roleMenus = request.getMenuIds().stream()
                    .map(menuId -> {
                        RoleMenuEntity rm = new RoleMenuEntity();
                        rm.setRoleId(request.getRoleId());
                        rm.setAuthorityId(menuId);
                        return rm;
                    })
                    .collect(Collectors.toList());
            roleMenuMapper.batchInsert(roleMenus);
        }

        // 清除该角色下所有用户的权限缓存
        clearCacheByRoleId(request.getRoleId());
        log.info("分配菜单权限成功，roleId={}, menuIds={}", request.getRoleId(), request.getMenuIds());
    }

    @Override
    public RoleMenuVO getRoleMenus(Long roleId) {
        RoleEntity role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "角色不存在");
        }

        List<Long> menuIds = roleMenuMapper.selectMenuIdsByRoleId(roleId);
        RoleMenuVO vo = new RoleMenuVO();
        vo.setRoleId(roleId);
        vo.setMenuIds(menuIds != null ? menuIds : new ArrayList<>());

        // 构建菜单树
        List<AuthorityEntity> allAuthorities = authorityMapper.selectAll();
        List<MenuTreeVO> menuTree = buildMenuTree(allAuthorities);
        vo.setMenuTree(menuTree);

        return vo;
    }

    @Override
    public List<OptionVO> listEnabledOptions() {
        List<OptionVO> options = roleMapper.selectEnabledOptions();
        return options != null ? options : new ArrayList<>();
    }

    /**
     * 清除该角色下所有用户的权限缓存
     */
    private void clearCacheByRoleId(Long roleId) {
        List<Long> userIds = userMapper.selectUserIdsByRoleId(roleId);
        for (Long userId : userIds) {
            UserEntity user = userMapper.selectById(userId);
            if (user != null) {
                clearUserAuthorityCache(user.getUsername());
            }
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
     * 校验角色名称唯一性
     */
    private void ensureRoleNameUnique(String name, Long excludeId) {
        if (roleMapper.countByName(name, excludeId) > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "角色名称已存在");
        }
    }

    /**
     * 校验角色编码唯一性
     */
    private void ensureRoleCodeUnique(String code, Long excludeId) {
        if (roleMapper.countByCode(code, excludeId) > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "角色编码已存在");
        }
    }

    /**
     * 标准化分页查询参数
     */
    private RolePageQueryDTO normalizePageQuery(RolePageQueryDTO queryDTO) {
        RolePageQueryDTO normalized = new RolePageQueryDTO();
        long page = queryDTO == null ? 1L : PageUtils.normalizePage(queryDTO.getPage());
        long size = (queryDTO == null || queryDTO.getSize() < 1L)
                ? 10L : Math.min(queryDTO.getSize(), MAX_PAGE_SIZE);

        normalized.setPage(page);
        normalized.setSize(size);
        if (queryDTO != null) {
            normalized.setName(queryDTO.getName());
            normalized.setCode(queryDTO.getCode());
            normalized.setStatus(queryDTO.getStatus());
            normalized.setType(queryDTO.getType());
        }
        return normalized;
    }

    /**
     * 构建菜单树
     * @param authorities 菜单权限列表
     * @return 菜单树形结构
     */
    private List<MenuTreeVO> buildMenuTree(List<AuthorityEntity> authorities) {
        if (authorities == null || authorities.isEmpty()) {
            return new ArrayList<>();
        }

        // 使用 LinkedHashMap 保持插入顺序
        Map<Long, MenuTreeVO> menuMap = new LinkedHashMap<>();
        List<MenuTreeVO> rootMenus = new ArrayList<>();

        // 第一遍：转换为 VO 并建立映射
        for (AuthorityEntity entity : authorities) {
            MenuTreeVO vo = new MenuTreeVO();
            vo.setId(entity.getId());
            vo.setName(entity.getName());
            vo.setCode(entity.getCode());
            vo.setPath(entity.getPath());
            vo.setOrderNum(entity.getOrdernum());
            vo.setChildren(new ArrayList<>());
            menuMap.put(entity.getId(), vo);
        }

        // 第二遍：建立父子关系（subsystemid = 0 表示顶级菜单）
        for (AuthorityEntity entity : authorities) {
            MenuTreeVO vo = menuMap.get(entity.getId());
            if (entity.getSubsystemid() == null || entity.getSubsystemid() == 0L) {
                // 顶级菜单
                rootMenus.add(vo);
            } else {
                // 非顶级菜单，找到父节点
                MenuTreeVO parent = menuMap.get(entity.getSubsystemid());
                if (parent != null) {
                    parent.getChildren().add(vo);
                } else {
                    // 父节点不存在，当作顶级菜单
                    rootMenus.add(vo);
                }
            }
        }

        return rootMenus;
    }

    /**
     * 转换为RoleVO
     */
    private RoleVO toRoleVO(RoleEntity entity) {
        RoleVO vo = new RoleVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setCode(entity.getCode());
        vo.setNote(entity.getNote());
        vo.setType(entity.getType());
        vo.setStatus(entity.getStatus());
        vo.setSort(entity.getSort());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        vo.setMenuIds(new ArrayList<>());
        return vo;
    }
}
