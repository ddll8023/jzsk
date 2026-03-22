package com.jzsk.backendv2.service.impl.auth;

import com.jzsk.backendv2.exception.BusinessException;
import com.jzsk.backendv2.exception.ErrorCode;
import com.jzsk.backendv2.mapper.system.UserMapper;
import com.jzsk.backendv2.pojo.dto.auth.LoginRequestDTO;
import com.jzsk.backendv2.pojo.entity.system.UserEntity;
import com.jzsk.backendv2.pojo.vo.OptionVO;
import com.jzsk.backendv2.pojo.vo.auth.CurrentUserVO;
import com.jzsk.backendv2.pojo.vo.auth.LoginResponseVO;
import com.jzsk.backendv2.security.JwtTokenService;
import com.jzsk.backendv2.security.LoginUser;
import com.jzsk.backendv2.service.auth.AuthService;
import com.jzsk.backendv2.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证服务实现类
 * 职责：提供用户登录认证功能
 * 遵循 KISS 原则：方法简洁，职责单一
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserMapper userMapper;

    @Override
    public LoginResponseVO login(LoginRequestDTO request) {
        log.info("用户登录，username={}", request.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        LoginResponseVO response = LoginResponseVO.builder()
                .token(jwtTokenService.generateToken(loginUser))
                .user(buildCurrentUser(loginUser))
                .build();
        log.info("用户登录成功，username={}", request.getUsername());
        return response;
    }

    @Override
    public CurrentUserVO getCurrentUser() {
        return buildCurrentUser(SecurityUtils.getCurrentLoginUser());
    }

    private CurrentUserVO buildCurrentUser(LoginUser loginUser) {
        UserEntity user = userMapper.selectById(loginUser.getUserId());
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }

        List<OptionVO> roleOptions = userMapper.selectUserRoles(user.getId());
        List<CurrentUserVO.RoleInfo> roles = roleOptions == null
                ? Collections.emptyList()
                : roleOptions.stream()
                .map(role -> CurrentUserVO.RoleInfo.builder()
                        .id(Long.valueOf(role.getValue().toString()))
                        .name(role.getLabel())
                        .build())
                .collect(Collectors.toList());

        String displayName = StringUtils.hasText(user.getName()) ? user.getName() : loginUser.getDisplayName();

        return CurrentUserVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .displayName(displayName)
                .name(user.getName())
                .department(user.getDepartment())
                .position(user.getPosition())
                .type(user.getType())
                .gender(user.getGender())
                .idNumber(user.getIdNumber())
                .technicalTitle(user.getTechnicalTitle())
                .academicQualifications(user.getAcademicQualifications())
                .workingTime(user.getWorkingTime())
                .graduationInstitution(user.getGraduationInstitution())
                .major(user.getMajor())
                .address(user.getAddress())
                .birthplace(user.getBirthplace())
                .ethnicity(user.getEthnicity())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .politicalAppearance(user.getPoliticalAppearance())
                .phoneNumber(user.getPhoneNumber())
                .note(user.getNote())
                .createTime(user.getCreateTime())
                .updateTime(user.getUpdateTime())
                .roles(roles)
                .authorities(SecurityUtils.toAuthorityNames(loginUser.getAuthorities()))
                .build();
    }
}
