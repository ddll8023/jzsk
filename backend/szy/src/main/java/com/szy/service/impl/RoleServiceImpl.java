package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.szy.entity.Role;
import com.szy.mapper.RoleMapper;
import com.szy.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Override
    public List<Role> listRolesByUserId(Long userID){
        List<Role> roles = this.list(new QueryWrapper<Role>().inSql("id","select role_id from user_role where user_id = " + userID));
        return roles;
    }

}
