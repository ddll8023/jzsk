package com.jzsk.backendv2.mapper.monitor;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.pojo.entity.monitor.StPptnHourEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 逐小时降雨量Mapper
 * 数据源说明：
 * - 所有查询方法统一使用 SQL Server 数据源(dbo)，直接读取实时监测数据
 * - 不需要数据同步，直接从SQL Server读取最新数据
 * 遵循纯 MyBatis 风格，不使用 MyBatis-Plus
 */
@Mapper
public interface StPptnHourMapper {

    /**
     * 查询指定测站的最新一条雨量数据（SQL Server语法）
     * 数据源：dbo（SQL Server）
     *
     * @param stcd 测站编码
     * @return 最新雨量数据
     */
    @DS("dbo")
    StPptnHourEntity selectLatestByStcd(@Param("stcd") String stcd);

    /**
     * 统计小时雨量数据总数（SQL Server语法）
     * 数据源：dbo（SQL Server）
     *
     * @param startDate 开始时间(可选)
     * @param endDate 结束时间(可选)
     * @return 总数
     */
    @DS("dbo")
    long countPage(@Param("startDate") LocalDateTime startDate,
                   @Param("endDate") LocalDateTime endDate);

    /**
     * 查询所有小时雨量数据（SQL Server语法）
     * 数据源：dbo（SQL Server）
     *
     * @return 小时雨量数据列表
     */
    @DS("dbo")
    List<StPptnHourEntity> selectAll();

    /**
     * 根据时间范围查询小时雨量数据（SQL Server语法）
     * 数据源：dbo（SQL Server）
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 小时雨量数据列表
     */
    @DS("dbo")
    List<StPptnHourEntity> selectByTimeRange(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    /**
     * 分页查询小时雨量数据（SQL Server语法）
     * 数据源：dbo（SQL Server）
     *
     * @param startDate 开始时间(可选)
     * @param endDate 结束时间(可选)
     * @param offset 偏移量
     * @param size 每页大小
     * @return 小时雨量数据列表
     */
    @DS("dbo")
    List<StPptnHourEntity> selectPage(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       @Param("offset") long offset,
                                       @Param("size") long size);
}
