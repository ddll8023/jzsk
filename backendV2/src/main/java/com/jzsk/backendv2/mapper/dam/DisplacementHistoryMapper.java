package com.jzsk.backendv2.mapper.dam;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.pojo.entity.dam.GnssMonitoringDataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 位移历史Mapper
 * 数据源：gcdd（SQL Server）
 * 遵循纯 MyBatis 风格，不使用 MyBatis-Plus
 */
@Mapper
public interface DisplacementHistoryMapper {

    /**
     * 分页查询GNSS监测数据
     *
     * @param stationIds 站点ID列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param offset 偏移量
     * @param size 每页大小
     * @return GNSS监测数据列表
     */
    @DS("gcdd")
    List<GnssMonitoringDataEntity> selectPage(
            @Param("stationIds") List<String> stationIds,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("offset") long offset,
            @Param("size") int size);

    /**
     * 统计GNSS监测数据总数
     *
     * @param stationIds 站点ID列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 总数
     */
    @DS("gcdd")
    long countPage(
            @Param("stationIds") List<String> stationIds,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime);

    /**
     * 查询所有站点
     *
     * @return GNSS监测数据列表
     */
    @DS("gcdd")
    List<GnssMonitoringDataEntity> selectAll();
}
