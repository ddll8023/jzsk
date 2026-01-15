package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.szy.entity.Authority;
import com.szy.entity.RoleAuthority;
import com.szy.mapper.RoleAuthorityMapper;
import com.szy.mapper.RoleMapper;
import com.szy.service.AuthorityService;
import com.szy.service.RoleAuthorityService;
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
public class RoleAuthorityServiceImpl extends ServiceImpl<RoleAuthorityMapper, RoleAuthority> implements RoleAuthorityService {

    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AuthorityService authorityService;

    //根据角色id获取权限信息
    @Override
    public String getUserAuthorityInfo(Long roleId) {
        String authority = "";
        if(redisUtil.hasKey("RoleAuthority:" + roleId)){
            //先从redis缓存中寻找
            authority = (String) redisUtil.get("RoleAuthority:" + roleId);
        }else {
            //根据角色id获取权限信息
            List<RoleAuthority> roleAuthorityList = roleAuthorityMapper.selectList(
                    new QueryWrapper<RoleAuthority>().eq("role_id", roleId));
            if(roleAuthorityList.size() > 0){
                //根据每个权限id获取权限列表
                List<Authority> authorities = authorityService.listByIds(roleAuthorityList.stream().
                        map(RoleAuthority::getAuthorityId).collect(Collectors.toList()));
                String authorityCodes = authorities.stream().map(Authority::getCode).collect(Collectors.joining(","));
                authority = authority.concat(authorityCodes);
            }
            //将角色id获取的权限列表存放到redis中
            redisUtil.set("RoleAuthority:" + roleId, authority, 60 * 60 * 24);
        }
        return authority;
    }
}
