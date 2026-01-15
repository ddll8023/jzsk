package com.szy.mapper;

import com.szy.entity.StRiversR;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.dynamic.datasource.annotation.DS;

/**
 * <p>
 * 河道水情表 Mapper 接口
 * </p>
 *
 * @author l
 * @since 2025-06-16
 */
@DS("dbo")
@Mapper
public interface StRiversRMapper extends BaseMapper<StRiversR> {
    /**
     * 查询指定测站的最新数据
     * @param stcd 测站编码
     * @return 最新数据
     */
    @Select("SELECT TOP 1 * FROM ST_RIVER_R WHERE STCD = #{stcd} ORDER BY TM DESC")
    StRiversR selectLatestByStcd(@Param("stcd") String stcd);
} 