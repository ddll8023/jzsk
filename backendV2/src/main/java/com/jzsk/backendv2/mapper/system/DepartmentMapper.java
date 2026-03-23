package com.jzsk.backendv2.mapper.system;

import com.jzsk.backendv2.pojo.dto.system.department.DepartmentPageQueryDTO;
import com.jzsk.backendv2.pojo.entity.system.DepartmentEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门Mapper接口
 * 职责：部门表的数据库操作
 * 遵循KISS原则：只负责数据访问，不包含业务逻辑
 */
public interface DepartmentMapper {

    /**
     * 分页查询部门列表
     * @param queryDTO 查询条件
     * @param offset 偏移量
     * @param size 每页大小
     * @return 部门列表
     */
    List<DepartmentEntity> selectPage(@Param("query") DepartmentPageQueryDTO queryDTO,
                                      @Param("offset") long offset,
                                      @Param("size") long size);

    /**
     * 统计分页总数
     * @param queryDTO 查询条件
     * @return 总数
     */
    long countPage(@Param("query") DepartmentPageQueryDTO queryDTO);

    /**
     * 根据ID查询部门
     * @param id 部门ID
     * @return 部门实体
     */
    DepartmentEntity selectById(@Param("id") Long id);

    /**
     * 统计部门名称数量（用于唯一性校验）
     * @param departmentName 部门名称
     * @param excludeId 排除的部门ID
     * @return 数量
     */
    int countByDepartmentName(@Param("departmentName") String departmentName, @Param("excludeId") Long excludeId);

    /**
     * 新增部门
     * @param entity 部门实体
     * @return 影响行数
     */
    int insert(DepartmentEntity entity);

    /**
     * 更新部门
     * @param entity 部门实体
     * @return 影响行数
     */
    int update(DepartmentEntity entity);

    /**
     * 删除部门
     * @param id 部门ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
