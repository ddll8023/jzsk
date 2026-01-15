package com.szy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.UserRole;
import com.szy.mapper.UserRoleMapper;
import com.szy.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
