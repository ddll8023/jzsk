package com.jzsk.backendv2.mapper.system;

import com.jzsk.backendv2.pojo.entity.system.AuthorityEntity;
import com.jzsk.backendv2.pojo.entity.system.AuthorityEntity;

import java.util.List;

/**
 * 菜单权限Mapper接口
 * 职责：菜单权限表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
public interface AuthorityMapper {

    /**
     * 查询所有菜单权限（用于构建菜单树）
     * @return 菜单权限列表
     */
    List<AuthorityEntity> selectAll();
}
