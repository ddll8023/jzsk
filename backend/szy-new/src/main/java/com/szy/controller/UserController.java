package com.szy.controller;

import com.szy.common.exception.BusinessException;
import com.szy.common.lang.Result;
import com.szy.pojo.dto.UpdatePasswordDTO;
import com.szy.pojo.entity.User;
import com.szy.pojo.vo.UserVO;
import com.szy.security.AccountUser;
import com.szy.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Result userInfo(@AuthenticationPrincipal AccountUser accountUser) {
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
    public Result updatePassword(@Validated @RequestBody UpdatePasswordDTO dto,
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
}