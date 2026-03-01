package com.szy.controller;

import com.github.pagehelper.PageInfo;
import com.szy.common.exception.BusinessException;
import com.szy.common.lang.Result;
import com.szy.pojo.dto.ResetPasswordDTO;
import com.szy.pojo.dto.UpdatePasswordDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * 用户控制器
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

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
     * 修改密码
     */
    @ApiOperation("修改密码")
    @PostMapping("/updatePass")
    public Result<Void> updatePassword(@Validated @RequestBody UpdatePasswordDTO dto,
                                  @AuthenticationPrincipal AccountUser accountUser) {
        // 验证新密码和确认密码是否一致
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("新密码与确认密码不一致");
        }

        // 获取当前用户
        User user = userService.getById(accountUser.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证原密码
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException("原密码不正确");
        }

        // 更新密码
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        userService.updatePassword(user.getId(), encodedPassword);

        // 清除权限缓存
        userService.clearUserAuthorityCache(user.getUsername());

        return Result.success("密码修改成功");
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    @ApiOperation("获取用户列表")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<PageInfo<UserDetailVO>> list(UserQueryDTO queryDTO) {
        return Result.success(userService.list(queryDTO));
    }

    /**
     * 搜索用户（按姓名）
     */
    @GetMapping("/search-list")
    @ApiOperation("搜索用户（按姓名）")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<PageInfo<UserDetailVO>> searchList(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("name") String name) {
        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setCurrentPage(currentPage);
        queryDTO.setPageSize(pageSize);
        queryDTO.setName(name);
        return Result.success(userService.list(queryDTO));
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
    public Result<Void> save(@Validated @RequestBody UserDTO dto) {
        userService.save(dto);
        return Result.success("操作成功");
    }

    /**
     * 更新用户
     */
    @PostMapping("/update")
    @ApiOperation("更新用户")
    @PreAuthorize("hasAuthority('xtgl')")
    public Result<Void> update(@Validated @RequestBody UserDTO dto) {
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
    public Result<Void> allocateRole(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
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
     * 修改当前用户密码（新版本）
     */
    @PutMapping("/updatePassword")
    @ApiOperation("修改当前用户密码")
    public Result<Void> updatePassword(@Validated @RequestBody ResetPasswordDTO dto, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        userService.changePassword(user.getId(), dto.getOldPassword(), dto.getNewPassword());
        return Result.success("密码修改成功");
    }
}