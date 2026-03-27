package com.jzsk.backendv2.mapper.dam;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.pojo.entity.dam.SeepageDataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 渗流量数据Mapper
 * 数据源：gcdd（MySQL）
 * 遵循纯 MyBatis 风格，不使用 MyBatis-Plus
 */
@Mapper
public interface SeepageDataMapper {

    /**
     * 分页查询渗流量数据
     * 数据源：gcdd（MySQL）
     *
     * @param stationId 测站ID（可选）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @param offset 偏移量
     * @param size 每页大小
     * @return 渗流量数据列表
     */
    @DS("gcdd")
    List<SeepageDataEntity> selectPage(@Param("stationId") Integer stationId,
                                       @Param("startTime") String startTime,
                                       @Param("endTime") String endTime,
                                       @Param("offset") long offset,
                                       @Param("size") int size);

    /**
     * 统计渗流量数据总数
     * 数据源：gcdd（MySQL）
     *
     * @param stationId 测站ID（可选）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 总数
     */
    @DS("gcdd")
    long countPage(@Param("stationId") Integer stationId,
                   @Param("startTime") String startTime,
                   @Param("endTime") String endTime);

    /**
     * 获取所有渗流量数据列表
     * 数据源：gcdd（MySQL）
     *
     * @return 渗流量数据列表
     */
    @DS("gcdd")
    List<SeepageDataEntity> selectAll();
}
