package com.jzsk.backendv2.mapper.monitor;

import com.jzsk.backendv2.pojo.entity.monitor.StPptnHourEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 逐小时降雨量Mapper
 * 数据源：dbo（SQL Server）
 * 遵循纯 MyBatis 风格，不使用 MyBatis-Plus
 */
@Mapper
public interface StPptnHourMapper {

    /**
     * 查询指定测站的最新一条雨量数据
     *
     * @param stcd 测站编码
     * @return 最新雨量数据
     */
    StPptnHourEntity selectLatestByStcd(@Param("stcd") String stcd);
}
