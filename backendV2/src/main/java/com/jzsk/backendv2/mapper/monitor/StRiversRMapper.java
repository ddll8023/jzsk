package com.jzsk.backendv2.mapper.monitor;

import com.jzsk.backendv2.pojo.entity.monitor.StRiversREntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 河道水情Mapper
 * 数据源：dbo（SQL Server）
 * 遵循纯 MyBatis 风格，不使用 MyBatis-Plus
 */
@Mapper
public interface StRiversRMapper {

    /**
     * 查询指定测站的最新一条水位数据
     *
     * @param stcd 测站编码
     * @return 最新水位数据
     */
    StRiversREntity selectLatestByStcd(@Param("stcd") String stcd);
}
