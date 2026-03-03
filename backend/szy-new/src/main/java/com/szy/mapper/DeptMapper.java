package com.szy.mapper;

import com.szy.pojo.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门Mapper
 */
@Mapper
public interface DeptMapper {

    /**
     * 分页查询部门
     */
    List<Department> selectList(@Param("departmentName") String departmentName);

    /**
     * 根据ID查询
     */
    Department selectById(@Param("id") Long id);

    /**
     * 新增部门
     */
    int insert(Department department);

    /**
     * 更新部门
     */
    int update(Department department);

    /**
     * 删除部门
     */
    int deleteById(@Param("id") Long id);
}
