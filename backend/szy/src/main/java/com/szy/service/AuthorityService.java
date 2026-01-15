package com.szy.service;

import com.szy.common.dto.MenuDto;
import com.szy.entity.Authority;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author l
 * @since 2022-01-12
 */
public interface AuthorityService extends IService<Authority> {
    List<MenuDto> getCurrentUserNav();

    List<Authority> tree();

    List<MenuDto> getCurrentNavByRoleID(Long roleId);

}
