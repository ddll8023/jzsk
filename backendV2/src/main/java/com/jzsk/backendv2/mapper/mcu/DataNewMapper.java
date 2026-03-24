package com.jzsk.backendv2.mapper.mcu;

import com.jzsk.backendv2.pojo.entity.mcu.DataNewEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MCU传感器数据Mapper
 * 数据源：pgsql（PostgreSQL）
 * 遵循纯 MyBatis 风格，不使用 MyBatis-Plus
 */
@Mapper
public interface DataNewMapper {

    /**
     * 查询每个point_id最新一条数据
     *
     * @return 各测点最新数据列表
     */
    List<DataNewEntity> selectLatestForAllPoints();

    /**
     * 根据测点编号查询最新一条数据
     *
     * @param pointId 测点编号
     * @return 最新数据
     */
    DataNewEntity selectLatestByPointId(@Param("pointId") String pointId);
}
