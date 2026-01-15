package com.szy.security;

import cn.hutool.core.util.StrUtil;
import com.szy.entity.User;
import com.szy.service.UserService;
import com.szy.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final List<AntPathRequestMatcher> publicMatchers;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    UserService userService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String[] publicUrls) {
        super(authenticationManager);
        this.publicMatchers = Arrays.stream(publicUrls)
                                    .map(AntPathRequestMatcher::new)
                                    .collect(Collectors.toList());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        for (AntPathRequestMatcher matcher : publicMatchers) {
            if (matcher.matches(request)) {
                return true; 
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取JWT令牌，若令牌为空，则调用下一个过滤器
        String jwt = request.getHeader(jwtUtils.getHeader());
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }
        Claims claim = jwtUtils.getClaimByToken(jwt);;
        if (claim == null) {
            throw new JwtException("token 异常");
        }
        if (jwtUtils.isTokenExpired(claim)) {
            throw new JwtException("token已过期");
        }

        String username = claim.getSubject();
        // 获取用户的权限等信息

        User user = userService.getByUsername(username);
        //通过用户实体对象 创建一个包含用户名、空密码和用户权限的认证
        //权限认证是根据用户id来获取
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(username, null, userDetailService.getUserAuthority(user.getId()));
        //将创建好的认证令牌设置到Spring Security的核心组件SecurityContextHolder的上下文对象中代表通过身份验证
        SecurityContextHolder.getContext().setAuthentication(token);

        chain.doFilter(request, response);
    }
}
