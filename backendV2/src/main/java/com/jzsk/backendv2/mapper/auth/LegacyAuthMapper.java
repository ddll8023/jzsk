package com.jzsk.backendv2.mapper.auth;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.pojo.entity.auth.LegacyAuthUserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@DS("jcxx")
public interface LegacyAuthMapper {

    LegacyAuthUserEntity selectByUsername(@Param("username") String username);

    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    List<String> selectAuthorityCodesByUserId(@Param("userId") Long userId);
}
