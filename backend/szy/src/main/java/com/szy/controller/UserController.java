package com.szy.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.szy.common.dto.PassDto;
import com.szy.common.lang.Const;
import com.szy.common.lang.ResponseCode;
import com.szy.common.lang.Result;
import com.szy.entity.Role;
import com.szy.entity.User;
import com.szy.entity.UserRole;
import com.szy.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

import static com.szy.common.lang.Const.PROJECT_VIDEO_URL;

/**
 * <p>
 * 用户控制器
 * </p>
 *
 * @author l
 * @since 2022-01-12
 */
@RestController
@RequestMapping("/user")
@DS("jcxx")
public class UserController extends BaseController {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 获取用户信息接口
     *
     * @param principal 身份认证
     * @return 成功信息，用户信息json
     */
    @GetMapping("/userInfo")
    public Result userInfo(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        return Result.ok(user);
    }

    /**
     * 指定用户信息
     *
     * @param id 用户id
     * @return 成功信息，用户json
     */
    @PreAuthorize("hasAuthority('xtgl')")
    @GetMapping("/info/{id}")
    @Transactional
    public Result info(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        Assert.notNull(user, "找不到用户");
        //根据用户id找到这个用户所属的角色
        List<Role> roles = roleService.listRolesByUserId(id);
        user.setRoles(roles);
        return Result.ok(user);
    }

    /**
     * 用户列表
     * <p>
     * //@param username 用户名
     *
     * @return 成功信息（pageData分页信息）
     */
    @PreAuthorize("hasAuthority('xtgl')")
    @GetMapping("/list")
    public Result list(@RequestParam("currentPage") Integer currentPage,
                       @RequestParam("pageSize") Integer pageSize,
                       String username
    ) {
        Page<User> page = new Page<>(currentPage, pageSize);
        Page<User> pageData = userService.page(page, new QueryWrapper<User>()
                .like(StrUtil.isNotBlank(username), "username", username).orderByAsc("user_order"));
        pageData.getRecords().forEach(user -> {
            user.setRoles(roleService.listRolesByUserId(user.getId()));
        });
        return Result.ok(pageData);
    }

    /**
     * 数据维护人员获取用户列表
     * @param currentPage
     * @param pageSize
     * @param username
     * @return com.szy.common.lang.Result
     * @author admin
     * @date 2024/07/07 19:28
     */
    @PreAuthorize("hasAuthority('xtgl')")
    @GetMapping("/list-data")
    public Result listdata(@RequestParam("currentPage") Integer currentPage,
                           @RequestParam("pageSize") Integer pageSize,
                           String username) {
        // 打印当前用户的所有权限
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getAuthorities() != null) {
//            authentication.getAuthorities().forEach(authority -> {
//                System.out.println("Authority: " + authority.getAuthority());
//            });
//        }
        Page<User> page = new Page<>(currentPage, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        queryWrapper.eq("type", "只读用户");
        Page<User> pageData = userService.page(page, queryWrapper.orderByAsc("user_order"));
        pageData.getRecords().forEach(user -> {
            user.setRoles(roleService.listRolesByUserId(user.getId()));
        });
        return Result.ok(pageData);
    }

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 成功信息，用户json
     */
    @PreAuthorize("hasAuthority('xtgl')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user) {
        String password = passwordEncoder.encode(Const.DEFAULT_PASSWORD);
        //找到user表中user_order最大值
        Integer maxUserOrder = userService.findMaxUserOrder();
        if (maxUserOrder == null) {
            maxUserOrder = 0; // 如果没有数据，则设置为0
        } else {
            maxUserOrder++;
        }
        user.setUserOrder(maxUserOrder);
        user.setPassword(password);
        userService.save(user);
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                UserRole userRole = UserRole.builder()
                        .userId(user.getId())
                        .roleId(role.getId())
                        .build();
                userRoleService.save(userRole);
            }
        }
        return Result.ok(user);
    }

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 成功信息（user json)
     */
    @PreAuthorize("hasAuthority('xtgl')")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody User user) {
        User byId = userService.getById(user.getId());
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "更新用户不存在");
        }
        String password = passwordEncoder.encode(Const.DEFAULT_PASSWORD);
        user.setPassword(password);
        userService.updateById(user);
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                UserRole userRole = UserRole.builder()
                        .userId(user.getId())
                        .roleId(role.getId())
                        .build();
                userRoleService.updateById(userRole);
            }
        }
        return Result.ok(user);
    }

    /**
     * 删除用户
     *
     * @param id 删除用户id数组
     * @return 成功信息
     */
    @PreAuthorize("hasAuthority('xtgl')")
    @Transactional
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        User byId = userService.getById(id);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "删除用户不存在");
        }
        userService.removeById(id);
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", id));
        return Result.ok("删除成功");
    }

    /**
     * 给用户分配角色
     *
     * @param userId  用户id
     * @param roleIds 角色数组
     * @return 成功信息
     */
    @PreAuthorize("hasAuthority('xtgl')")
    @Transactional
    @PostMapping("/role/{userId}")
    public Result roleDistribute(@PathVariable("userId") Long userId, @RequestBody Long[] roleIds) {
        User byId = userService.getById(userId);
        if (byId == null) {
            return Result.fail(ResponseCode.NOT_EXIST, "待分配角色用户不存在");
        }
        for (Long id : roleIds) {
            Role role = roleService.getById(id);
            if (role == null) {
                return Result.fail(ResponseCode.NOT_EXIST, "待分配角色不存在");
            }
        }
        List<UserRole> userRoles = new ArrayList<>();
        Arrays.stream(roleIds).forEach(r -> {
            System.out.println(r);
            Role role = roleService.getById(r);
            System.out.println(role);
            Assert.notNull(role);
            UserRole userRole = new UserRole();
            userRole.setRoleId(r);
            userRole.setUserId(userId);
            userRoles.add(userRole);
        });
        // 删除之前的记录
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", userId));
        userRoleService.saveBatch(userRoles);

        // 删除缓存
        User user = userService.getById(userId);
        userService.clearUserAuthorityInfo(user.getUsername());

        // 更新user表
        Long roleId = userRoles.get(0).getRoleId();
        if (roleId == 5) {
            user.setType("超级管理员");
        }
        else if (roleId == 2) {
            user.setType("数据维护人员");
        }
        else {
            user.setType("只读用户");
        }
        userService.updateById(user);
        return Result.ok();
    }

    /**
     * 重置用户密码
     *
     * @param userId 用户id
     * @return 成功信息
     */
    @PreAuthorize("hasAuthority('xtgl')")
    @PostMapping("/repass")
    public Result repass(@RequestParam("id") Long userId) {
        User user = userService.getById(userId);
        user.setPassword(passwordEncoder.encode(Const.DEFAULT_PASSWORD));
        userService.updateById(user);
        return Result.ok();
    }

    /**
     * 修改用户密码
     *
     * @param passDto   密码数据
     * @param principal 身份验证
     * @return 成功信息
     */
    @PreAuthorize("hasAuthority('xtgl')")
    @PostMapping("/updatePass")
    public Result updatePass(@Validated @RequestBody PassDto passDto, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        boolean matches = passwordEncoder.matches(passDto.getCurrentPassword(), user.getPassword());
        if (!matches) {
            return Result.fail(400, "旧密码不正确");
        }
        user.setPassword(passwordEncoder.encode(passDto.getPassword()));
        userService.updateById(user);
        return Result.ok();
    }

    /**
     * 分页返回根据输入名字查询的信息
     *
     * @param name 姓名
     * @return 成功信息，分页角色信息json
     */
    @GetMapping("/search-list")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result searchList(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("name") String name) {
        Page<User> page = new Page<>(currentPage, pageSize);
        Page<User> pageData = userService.page(page, new QueryWrapper<User>().like(StrUtil.isNotBlank(name), "name", name));
        pageData.getRecords().forEach(user -> {
            user.setRoles(roleService.listRolesByUserId(user.getId()));
        });
        return Result.ok(pageData);
    }

}
