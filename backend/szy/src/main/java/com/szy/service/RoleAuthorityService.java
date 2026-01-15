package com.szy.service;

import com.szy.entity.RoleAuthority;
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
public interface RoleAuthorityService extends IService<RoleAuthority> {

    //根据角色ID获取权限信息
    String getUserAuthorityInfo(Long roleId);
}
