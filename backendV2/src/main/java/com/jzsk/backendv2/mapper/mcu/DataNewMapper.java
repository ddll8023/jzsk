package com.jzsk.backendv2.mapper.mcu;

import com.baomidou.dynamic.datasource.annotation.DS;
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
    @DS("pgsql")
    List<DataNewEntity> selectLatestForAllPoints();

    /**
     * 根据测点编号查询最新一条数据
     *
     * @param pointId 测点编号
     * @return 最新数据
     */
    @DS("pgsql")
    DataNewEntity selectLatestByPointId(@Param("pointId") String pointId);

    /**
     * 统计渗流数据总数
     *
     * @param pointId 测点编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 总数
     */
    @DS("pgsql")
    long countPage(
            @Param("pointId") String pointId,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime);

    /**
     * 按时序查询传感器数据（用于图表展示）
     *
     * @param pointId 测点编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 传感器数据列表
     */
    @DS("pgsql")
    List<DataNewEntity> selectByTimeRange(
            @Param("pointId") String pointId,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime);

    /**
     * 分页查询渗流数据
     *
     * @param pointId 测点编号
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param offset 偏移量
     * @param size 每页大小
     * @return 渗流数据列表
     */
    @DS("pgsql")
    List<DataNewEntity> selectPage(
            @Param("pointId") String pointId,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("offset") long offset,
            @Param("size") int size);
}
