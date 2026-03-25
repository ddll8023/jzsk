package com.jzsk.backendv2.mapper.monitor;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jzsk.backendv2.pojo.entity.monitor.StRiversREntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 河道水情Mapper
 * 数据源：dbo（SQL Server）- 直接读取实时监测数据
 * 遵循纯 MyBatis 风格，不使用 MyBatis-Plus
 */
@Mapper
public interface StRiversRMapper {

    /**
     * 查询指定测站的最新一条水位数据（SQL Server语法）
     * 数据源：dbo（SQL Server）
     *
     * @param stcd 测站编码
     * @return 最新水位数据
     */
    @DS("dbo")
    StRiversREntity selectLatestByStcd(@Param("stcd") String stcd);

    /**
     * 统计水位数据总数（SQL Server语法）
     * 数据源：dbo（SQL Server）
     * @param stcd 测站编码(可选)
     * @return 总数
     */
    @DS("dbo")
    long countPage(@Param("stcd") String stcd);

    /**
     * 查询所有水位数据（SQL Server语法）
     * 数据源：dbo（SQL Server）
     * @return 水位数据列表
     */
    @DS("dbo")
    List<StRiversREntity> selectAll();

    /**
     * 根据测站编码查询水位数据（SQL Server语法）
     * 数据源：dbo（SQL Server）
     * @param stcd 测站编码
     * @return 水位数据列表
     */
    @DS("dbo")
    List<StRiversREntity> selectByStcd(@Param("stcd") String stcd);

    /**
     * 分页查询水位数据（SQL Server语法）
     * 数据源：dbo（SQL Server）
     * @param stcd 测站编码(可选)
     * @param offset 偏移量
     * @param size 每页大小
     * @return 水位数据列表
     */
    @DS("dbo")
    List<StRiversREntity> selectPage(@Param("stcd") String stcd,
                                      @Param("offset") long offset,
                                      @Param("size") long size);
}
