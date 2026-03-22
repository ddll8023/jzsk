package com.jzsk.backendv2.mapper.system;

import com.jzsk.backendv2.pojo.dto.system.user.UserPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.system.UserEntity;
import com.jzsk.backendv2.pojo.vo.OptionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper 接口
 */
public interface UserMapper {

    List<UserEntity> selectPage(@Param("query") UserPageQueryDTO queryDTO,
                                @Param("offset") long offset,
                                @Param("size") long size);

    long countPage(@Param("query") UserPageQueryDTO queryDTO);

    UserEntity selectById(@Param("id") Long id);

    UserEntity selectByUsername(@Param("username") String username);

    int countByUsername(@Param("username") String username, @Param("excludeId") Long excludeId);

    Integer selectMaxUserOrder();

    int insert(UserEntity entity);

    int update(UserEntity entity);

    int deleteById(@Param("id") Long id);

    List<OptionVO> selectUserRoles(@Param("userId") Long userId);

    List<String> selectAuthorityCodesByUserId(@Param("userId") Long userId);

    List<Long> selectUserIdsByRoleId(@Param("roleId") Long roleId);
}
