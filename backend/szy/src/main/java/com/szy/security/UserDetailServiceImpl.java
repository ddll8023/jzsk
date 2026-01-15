package com.szy.security;

import com.szy.entity.User;
import com.szy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    /**
     * 根据用户名获取用户信息
     * 如果不存在抛异常
     * 存在创建一个AccountUser对象
     *
     * @param username 用户名
     * @return 用户信息
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        return new AccountUser(user.getId(), user.getUsername(), user.getPassword(), getUserAuthority(user.getId()));
    }

    /**
     * 根据用户id获取用户权限信息（角色、菜单权限）
     *
     * @param userID 用户id
     * @return 权限信息
     */
    public List<GrantedAuthority> getUserAuthority(Long userID) {
        //角色(e.g.:ROLE_admin)、权限(权限编码)
        String authority = userService.getUserAuthorityInfo(userID);
        //将逗号分隔的字符串转化成GrantedAuthority对象
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);//封装成GrantedAuthority
    }
}
