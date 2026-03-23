package com.jzsk.backendv2.mapper.system;

import com.jzsk.backendv2.pojo.dto.system.organization.OrganizationPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.system.OrganizationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织机构Mapper接口
 * 职责：组织机构表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
public interface OrganizationMapper {

    /**
     * 分页查询组织机构列表
     * @param queryDTO 查询条件
     * @param offset 偏移量
     * @param size 每页大小
     * @return 组织机构列表
     */
    List<OrganizationEntity> selectPage(@Param("query") OrganizationPageQueryDTO queryDTO,
                                        @Param("offset") long offset,
                                        @Param("size") long size);

    /**
     * 统计分页总数
     * @param queryDTO 查询条件
     * @return 总数
     */
    long countPage(@Param("query") OrganizationPageQueryDTO queryDTO);

    /**
     * 根据ID查询组织机构
     * @param id 组织机构ID
     * @return 组织机构实体
     */
    OrganizationEntity selectById(@Param("id") Long id);

    /**
     * 统计组织机构名称数量（用于唯一性校验）
     * @param organizationName 组织机构名称
     * @param excludeId 排除的组织机构ID
     * @return 数量
     */
    int countByOrganizationName(@Param("organizationName") String organizationName, @Param("excludeId") Long excludeId);

    /**
     * 统计组织机构代码数量（用于唯一性校验）
     * @param organizationCode 组织机构代码
     * @param excludeId 排除的组织机构ID
     * @return 数量
     */
    int countByOrganizationCode(@Param("organizationCode") String organizationCode, @Param("excludeId") Long excludeId);

    /**
     * 新增组织机构
     * @param entity 组织机构实体
     * @return 影响行数
     */
    int insert(OrganizationEntity entity);

    /**
     * 更新组织机构
     * @param entity 组织机构实体
     * @return 影响行数
     */
    int update(OrganizationEntity entity);

    /**
     * 删除组织机构（物理删除）
     * @param id 组织机构ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
