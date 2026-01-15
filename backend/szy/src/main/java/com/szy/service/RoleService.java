package com.szy.service;

import com.szy.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-01-12
 */
public interface RoleService extends IService<Role> {
    List<Role> listRolesByUserId(Long id);


}
