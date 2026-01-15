package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.szy.common.dto.MenuDto;
import com.szy.entity.Authority;
import com.szy.entity.User;
import com.szy.mapper.AuthorityMapper;
import com.szy.mapper.RoleAuthorityMapper;
import com.szy.mapper.UserMapper;
import com.szy.service.AuthorityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author l
 * @since 2022-01-12
 */
@Service
@DS("jcxx")
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority> implements AuthorityService {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;

    //获取当前用户的导航栏信息
    @Override
    public List<MenuDto> getCurrentUserNav() {
        try {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.getByUsername(username);
            List<Long> menuIds = userMapper.getAuthorityIDs(user.getId());
            List<Authority> authorities = this.listByIds(menuIds);
            // 根据 subsystemID 和 orderNum 排序
            authorities.sort((a1, a2) -> {
                int subsystemCompare = Long.compare(a1.getSubsystemid(), a2.getSubsystemid());
                if (subsystemCompare == 0) {
                    return Integer.compare(a1.getOrdernum(), a2.getOrdernum());
                }
                return subsystemCompare;
            });
            // 转树状结构
            List<Authority> menuTree = buildTreeMenu(authorities);
            // 实体转VO
            return convert(menuTree);
        } catch (Exception e) {
            // 如果未认证或发生其他错误，返回所有菜单
            List<Authority> allMenus = this.list(new QueryWrapper<Authority>().orderByAsc("ordernum"));
            List<Authority> menuTree = buildTreeMenu(allMenus);
            return convert(menuTree);
        }
    }

    @Override
    public List<Authority> tree(){
        //获取所有菜单信息
        List<Authority> menus = this.list(new QueryWrapper<Authority>().orderByAsc("orderNum"));
        //转成树状结构
        return buildTreeMenu(menus);
    }

    //根据角色id来获取当前的导航栏信息
    @Override
    public List<MenuDto> getCurrentNavByRoleID(Long roleId) {
        List<Long> menuIds = roleAuthorityMapper.selectListByRoleID(roleId);
        List<Authority> authorities = this.listByIds(menuIds);
        // 根据 subsystemID 和 orderNum 排序
        authorities.sort((a1, a2) -> {
            int subsystemCompare = Long.compare(a1.getSubsystemid(), a2.getSubsystemid()); // 先按 subsystemID 排序
            if (subsystemCompare == 0) {
                return Integer.compare(a1.getOrdernum(), a2.getOrdernum()); // 若 subsystemID 相同，则按 orderNum 排序
            }
            return subsystemCompare;
        });
        // 转树状结构
        List<Authority> menuTree = buildTreeMenu(authorities);
        // 实体转VO
        return convert(menuTree);
    }

    private List<MenuDto> convert(List<Authority> menuTree) {
        List<MenuDto> menuVos = new ArrayList<>();
        menuTree.forEach(m->{
            MenuDto vo = new MenuDto();
            vo.setId(m.getId());
            vo.setName(m.getName());
            vo.setPath(m.getPath());
            if(m.getChildren().size() > 0){
                // 子节点调用当前方法进行再次转换
                vo.setChildren(convert(m.getChildren()));
            }
            menuVos.add(vo);
        });
        return menuVos;
    }

    private List<Authority> buildTreeMenu(List<Authority> authorities){
        List<Authority> finalMenus = new ArrayList<>();
        
        // 先清空所有节点的children，防止重复添加
        for(Authority menu : authorities){
            menu.getChildren().clear();
        }
        
        // 使用Map优化查找效率，按父ID分组
        Map<Long, List<Authority>> childrenMap = authorities.stream()
                .collect(Collectors.groupingBy(Authority::getSubsystemid));
        
        // 构建树结构
        for(Authority menu : authorities){
            // 从Map中获取子节点列表
            List<Authority> children = childrenMap.get(menu.getId());
            if(children != null){
                menu.getChildren().addAll(children);
            }
            // 顶级菜单加入结果
            if(menu.getSubsystemid() == 0){
                finalMenus.add(menu);
            }
        }
        return finalMenus;
    }
}
