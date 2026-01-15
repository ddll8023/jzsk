package com.szy.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.RoleAuthority;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-01-12
 */
@DS("jcxx")
@Mapper
public interface RoleAuthorityMapper extends BaseMapper<RoleAuthority> {

    List<Long> selectListByRoleID(Long roleId);
}
