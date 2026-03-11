package com.szy.controller;

import com.github.pagehelper.PageInfo;
import com.szy.common.exception.BusinessException;
import com.szy.common.lang.Result;
import com.szy.pojo.dto.ResetPasswordDTO;
import com.szy.pojo.dto.UserDTO;
import com.szy.pojo.dto.UserQueryDTO;
import com.szy.pojo.entity.User;
import com.szy.pojo.vo.UserDetailVO;
import com.szy.pojo.vo.UserVO;
import com.szy.security.AccountUser;
import com.szy.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取当前用户信息
     */
    @ApiOperation("获取当前用户信息")
    @GetMapping("/userInfo")
    public Result<UserVO> userInfo(@AuthenticationPrincipal AccountUser accountUser) {
        User user = userService.getById(accountUser.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        UserVO userVO = userService.toUserVO(user);
        return Result.success(userVO);
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    @ApiOperation("获取用户列表")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<Map<String, Object>> list(UserQueryDTO queryDTO) {
        PageInfo<UserDetailVO> pageInfo = userService.list(queryDTO);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("current", pageInfo.getPageNum());
        result.put("size", pageInfo.getPageSize());
        result.put("pages", pageInfo.getPages());
        return Result.success(result);
    }

    /**
     * 搜索用户（按姓名）
     */
    @GetMapping("/search-list")
    @ApiOperation("搜索用户（按姓名）")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<Map<String, Object>> searchList(UserQueryDTO queryDTO) {
        PageInfo<UserDetailVO> pageInfo = userService.list(queryDTO);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("current", pageInfo.getPageNum());
        result.put("size", pageInfo.getPageSize());
        result.put("pages", pageInfo.getPages());
        return Result.success(result);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/info/{id}")
    @ApiOperation("获取用户详情")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<UserDetailVO> info(@PathVariable Long id) {
        return Result.success(userService.getDetail(id));
    }

    /**
     * 新增用户
     */
    @PostMapping("/save")
    @ApiOperation("新增用户")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<Void> save(@Validated UserDTO dto) {
        userService.save(dto);
        return Result.success("操作成功");
    }

    /**
     * 更新用户
     */
    @PostMapping("/update")
    @ApiOperation("更新用户")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<Void> update(@Validated UserDTO dto) {
        userService.update(dto);
        return Result.success("操作成功");
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete/{id}")
    @ApiOperation("删除用户")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success("操作成功");
    }

    /**
     * 分配角色
     */
    @PostMapping("/role/{userId}")
    @ApiOperation("分配角色")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<Void> allocateRole(@PathVariable Long userId, List<Long> roleIds) {
        userService.allocateRole(userId, roleIds);
        return Result.success("操作成功");
    }

    /**
     * 重置密码
     */
    @PostMapping("/repass")
    @ApiOperation("重置密码")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<Void> repass(@RequestParam("id") Long id) {
        userService.resetPassword(id);
        return Result.success("密码已重置为123456");
    }

    /**
     * 修改当前用户密码
     */
    @PutMapping("/updatePassword")
    @ApiOperation("修改当前用户密码")
    public Result<Void> updatePassword(@Validated ResetPasswordDTO dto, Principal principal) {
        userService.changePassword(principal.getName(), dto.getOldPassword(), dto.getNewPassword());
        return Result.success("密码修改成功");
    }
}