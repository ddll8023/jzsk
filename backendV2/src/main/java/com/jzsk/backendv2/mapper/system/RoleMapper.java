package com.jzsk.backendv2.mapper.system;

import com.jzsk.backendv2.pojo.dto.system.role.RolePageQueryDTO;
import com.jzsk.backendv2.pojo.entity.system.RoleEntity;
import com.jzsk.backendv2.pojo.vo.OptionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 * 职责：角色表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
public interface RoleMapper {

    /**
     * 分页查询角色列表
     * @param queryDTO 查询条件
     * @param offset 偏移量
     * @param size 每页大小
     * @return 角色列表
     */
    List<RoleEntity> selectPage(@Param("query") RolePageQueryDTO queryDTO,
                                @Param("offset") long offset,
                                @Param("size") long size);

    /**
     * 统计分页总数
     * @param queryDTO 查询条件
     * @return 总数
     */
    long countPage(@Param("query") RolePageQueryDTO queryDTO);

    /**
     * 根据ID查询角色
     * @param id 角色ID
     * @return 角色实体
     */
    RoleEntity selectById(@Param("id") Long id);

    /**
     * 统计角色名称数量（用于唯一性校验）
     * @param name 角色名称
     * @param excludeId 排除的角色ID
     * @return 数量
     */
    int countByName(@Param("name") String name, @Param("excludeId") Long excludeId);

    /**
     * 统计角色编码数量（用于唯一性校验）
     * @param code 角色编码
     * @param excludeId 排除的角色ID
     * @return 数量
     */
    int countByCode(@Param("code") String code, @Param("excludeId") Long excludeId);

    /**
     * 新增角色
     * @param entity 角色实体
     * @return 影响行数
     */
    int insert(RoleEntity entity);

    /**
     * 更新角色
     * @param entity 角色实体
     * @return 影响行数
     */
    int update(RoleEntity entity);

    /**
     * 删除角色
     * @param id 角色ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 查询所有启用的角色（选项列表）
     * @return 角色选项列表
     */
    List<OptionVO> selectEnabledOptions();

    /**
     * 查询角色已分配的菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
