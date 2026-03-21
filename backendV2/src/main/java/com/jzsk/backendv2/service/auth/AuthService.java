package com.jzsk.backendv2.service.auth;

import com.jzsk.backendv2.pojo.dto.auth.LoginRequestDTO;
import com.jzsk.backendv2.pojo.vo.auth.CurrentUserVO;
import com.jzsk.backendv2.pojo.vo.auth.LoginResponseVO;

public interface AuthService {

    LoginResponseVO login(LoginRequestDTO request);

    CurrentUserVO getCurrentUser();
}
