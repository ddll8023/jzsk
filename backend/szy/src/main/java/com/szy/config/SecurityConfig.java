package com.szy.config;

import com.szy.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(), URL_WHITELIST);
        return jwtAuthenticationFilter;
    }

    /**
     * 创建BCryptPasswordEncoder注入容器
     * 主要是给密码进行加密
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private static final String[] URL_WHITELIST = {
            "/",
            "/shipin/**",
            "/login",
            "/logout",
            "/photo/**",
            "/pic/**",
            "/test/**",
            "/api/test/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/rainfall-stations",
            "/menu/nav/**",
            "/st-pextremum-b",
            "/daily-rainfall",
            "/water-storage",
            "/gate-alert",
            "/gate-info",
            "/gate-control",
            "/gate-status",
            "/gate-operation",
            "/gate-report",
            "/vertical-displacement",
            "/horizontal-displacement",
            "/seepage-data",
            "/seepage-water-level",
            "/monitoring-stations",
            "/station/list",
            "/station/add",
            "/station/update",
            "/station/delete",
            "/station/batch-delete",
            "/river-station",
            "/annual-water-situation",
            "/duty-log",
            "/duty-schedule",
            "/external-data/**",
            "/st-pptn-hour/**",
            "/st-rivers-r/**",
            "/data-new/**",
            "/app/**",
            "/warning-information/list",
            "/zkxt/dgq/**",
            "/zkxt/dzdf/**",
            "/zkxt/qst/**",
            "/zkxt/yhd/**",
            "/zkxt/xgq/**",
            "/icon/**","/doc.html", "/swagger-ui/**", "/swagger-resources/**", "/v2/api-docs", "/v3/api-docs", "/webjars/**"
    };

    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .and()
                .logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()  // 使用 permitAll 替代 anonymous
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .addFilter(jwtAuthenticationFilter());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        //自定义实现
        auth.userDetailsService(userDetailService);
    }
}
