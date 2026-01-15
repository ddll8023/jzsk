package com.szy.controller;


import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  用户-角色控制器
 * </p>
 *
 * @author l
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/user-role")
@DS("jcxx")
public class UserRoleController extends BaseController{


}
