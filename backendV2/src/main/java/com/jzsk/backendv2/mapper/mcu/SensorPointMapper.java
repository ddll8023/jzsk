package com.jzsk.backendv2.mapper.mcu;

import com.jzsk.backendv2.pojo.entity.mcu.SensorPointEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    SensorPointEntity selectById(@Param("id") Long id);
}
