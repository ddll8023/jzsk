package com.szy.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-01-12
 */
@DS("jcxx")
public interface UserService extends IService<User> {
    User getByUsername(String username);
    User getByType(String type);
    String getUserAuthorityInfo(Long userID);

    void clearUserAuthorityInfo(String username);//权限发生改变时删除原来的权限

    void clearUserAuthorityInfoByRoleID(Long roleID);

    void clearUserAuthorityInfoByAuthorityID(Long authorityID);

    Integer findMaxUserOrder();
}
