package com.szy.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.szy.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author l
 * @since 2022-01-12
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<Long> getAuthorityIDs(Long userID);

    List<User> listByAuthorityID(Long authorityID);

    @Select("SELECT MAX(user_order) FROM user;")
    Integer findMaxUserOrder();
}
