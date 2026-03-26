package com.jzsk.backendv2.mapper.mcu;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.pojo.entity.mcu.SensorPointEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 传感器测点Mapper
 * 数据源：pgsql（PostgreSQL）
 * 遵循纯 MyBatis 风格，不使用 MyBatis-Plus
 */
@Mapper
public interface SensorPointMapper {

    /**
     * 根据ID查询测点
     *
     * @param id 测点ID
     * @return 测点实体
     */
    @DS("pgsql")
    SensorPointEntity selectById(@Param("id") Long id);

    /**
     * 查询所有测点
     *
     * @return 所有测点列表
     */
    @DS("pgsql")
    List<SensorPointEntity> selectAll();

    /**
     * 根据名称查询测点
     *
     * @param name 测点名称
     * @return 测点实体
     */
    @DS("pgsql")
    SensorPointEntity selectByName(@Param("name") String name);

    /**
     * 根据ID列表查询测点名称映射
     *
     * @param ids ID列表
     * @return 测点实体列表
     */
    @DS("pgsql")
    List<SensorPointEntity> selectNameByIds(@Param("ids") List<Long> ids);
}
