package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.szy.entity.Authority;
import com.szy.entity.Role;
import com.szy.entity.User;
import com.szy.mapper.UserMapper;
import com.szy.service.AuthorityService;
import com.szy.service.RoleService;
import com.szy.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.szy.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    RoleService roleService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public User getByUsername(String username) {
        return getOne(new QueryWrapper<User>().eq("username",username));
    }

    public User getByType(String type) {
        return getOne(new QueryWrapper<User>().eq("type",type));
    }

    //返回权限的字符串，用逗号隔开
    //格式为【角色:权限】
    @Override
    public String getUserAuthorityInfo(Long userID) {
        //根据id获取用户，看是否缓存中有
        User user = userMapper.selectById(userID);
        String authority = "";

        if (redisUtil.hasKey("GrantedAuthority:" + user.getUsername())) { //如果有缓存，调用缓存中的权限
            authority = (String) redisUtil.get("GrantedAuthority:" + user.getUsername());
        } else {
            //获取角色
            List<Role> roles = roleService.list(new QueryWrapper<Role>()
                    .inSql("id", "select role_id from user_role where user_id =" + userID));
            if (roles.size() > 0) {
                String roleCodes = roles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }
            //获取权限
            List<Long> authorityIDs = userMapper.getAuthorityIDs(userID);
            if (authorityIDs.size() > 0) {
                List<Authority> authorities = authorityService.listByIds(authorityIDs);
                String authorityCodes = authorities.stream().map(Authority::getCode).collect(Collectors.joining(","));
                authority = authority.concat(authorityCodes);
            }
            redisUtil.set("GrantedAuthority:" + user.getUsername(), authority, 60 * 60);//缓存权限，避免频繁查库，暂定1h
        }
        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String username){
        redisUtil.del("GrantedAuthority:"+username);
    }

    /**
     * 角色改变时，将该角色相关的所有缓存都删除
     * @param roleID 角色编号
     */
    @Override
    public void clearUserAuthorityInfoByRoleID(Long roleID){
        List<User> users = this.list(new QueryWrapper<User>()
                .inSql("id","select user_id from user_role where role_id = " + roleID));
        users.forEach(u -> this.clearUserAuthorityInfo(u.getUsername()));
    }

    @Override
    public void clearUserAuthorityInfoByAuthorityID(Long authorityID){
        List<User> users = userMapper.listByAuthorityID(authorityID);
        users.forEach(user -> this.clearUserAuthorityInfo(user.getUsername()));
    }

    @Override
    public Integer findMaxUserOrder() {
        return userMapper.findMaxUserOrder();
    }
}
